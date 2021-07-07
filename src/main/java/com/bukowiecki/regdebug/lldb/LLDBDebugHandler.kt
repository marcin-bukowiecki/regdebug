/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.lldb

import com.bukowiecki.regdebug.parsers.RegistersParser
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.ProtobufMessageFactory
import com.jetbrains.cidr.execution.debugger.backend.lldb.auto_generated.Protocol
import com.jetbrains.cidr.execution.debugger.backend.lldb.auto_generated.ProtocolResponses
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @author Marcin Bukowiecki
 */
class LLDBDebugHandler(private val sessionTab: RegDebugSessionTab, private val driver: LLDBDriver) : DebugHandler {

    private val timeout = RegDebugSettings.getInstance(sessionTab.project).registersLoadingTimeout
    private var request: Protocol.CompositeRequest? = null
    private var killed = false
    private val executionId: Long = sessionTab.executionId.incrementAndGet()

    override fun handle() {
        val views = sessionTab.views
        val request = ProtobufMessageFactory.handleConsoleCommand(-1, -1, "register read --all")
        this.request = request

        val reply = try {
            CompletableFuture.supplyAsync {
                driver.sendMessageAndWaitForReply(
                    request,
                    ProtocolResponses.HandleConsoleCommand_Res::class.java,
                )
            }.get(timeout, TimeUnit.SECONDS)
        } catch (e: TimeoutException) {
            handleError(e)
            return
        } catch (e: ExecutionException) {
            handleError(e)
            return
        }

        if (sessionTab.executionId.get() == executionId) {
            synchronized(sessionTab) {
                if (sessionTab.executionId.get() == executionId) {
                    if (reply.hasErr()) {
                        views.forEach { it.addErrorMessages(reply.err) }
                    } else {
                        val parseResult = RegistersParser.parse(reply.out)
                         views.forEach { it.rebuildView(parseResult) }
                    }
                }
            }
        }
    }

    private fun handleError(e: Exception) {
        if (sessionTab.executionId.get() == executionId) {
            synchronized(sessionTab) {
                if (sessionTab.executionId.get() == executionId) {
                    kill()
                    sessionTab.views.forEach { it.addErrorMessages(e.message) }
                }
            }
        }
    }

    private fun kill() {
        killed = true
        request?.kill
    }
}