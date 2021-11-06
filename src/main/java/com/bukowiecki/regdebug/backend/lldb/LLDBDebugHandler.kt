/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.backend.lldb

import com.bukowiecki.regdebug.backend.BackendType
import com.bukowiecki.regdebug.backend.BaseDebugHandler
import com.bukowiecki.regdebug.parsers.lldb.LLDBRegistersParser
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.ProtobufMessageFactory
import com.jetbrains.cidr.execution.debugger.backend.lldb.auto_generated.ProtocolResponses
import java.util.concurrent.CompletableFuture
import java.util.concurrent.ExecutionException
import java.util.concurrent.TimeUnit
import java.util.concurrent.TimeoutException

/**
 * @author Marcin Bukowiecki
 */
class LLDBDebugHandler(sessionTab: RegDebugSessionTab,
                       private val driver: LLDBDriver) : BaseDebugHandler(sessionTab) {

    private val command = "register read --all"

    override val backendType: BackendType
        get() = BackendType.LLDB

    override fun handleSetCommand(register: String, operator: String, value: String): String? {
        val request = ProtobufMessageFactory
            .handleConsoleCommand(-1, -1, "register write $register $value")
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
            return null
        } catch (e: ExecutionException) {
            handleError(e)
            return null
        }

        return if (reply.hasErr()) {
            reply.err
        } else {
            null
        }
    }

    override fun handle() {
        val views = sessionTab.views
        val request = ProtobufMessageFactory.handleConsoleCommand(-1, -1, command)
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
                        val parseResult = LLDBRegistersParser.parse(reply.out)
                         views.forEach { it.rebuildView(parseResult) }
                    }
                }
            }
        }
    }
}
