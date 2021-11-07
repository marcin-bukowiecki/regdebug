/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.backend.gdb

import com.bukowiecki.regdebug.backend.BackendType
import com.bukowiecki.regdebug.backend.BaseDebugHandler
import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.parsers.gdb.GDBRegistersParser
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.intellij.execution.process.mediator.util.blockingGet
import com.intellij.openapi.diagnostic.Logger
import com.jetbrains.cidr.execution.debugger.backend.gdb.GDBDriver
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout

/**
 * @author Marcin Bukowiecki
 */
class GDBDebugHandler(sessionTab: RegDebugSessionTab,
                      private val driver: GDBDriver) : BaseDebugHandler(sessionTab) {

    override val backendType: BackendType
        get() = BackendType.GDB

    private val getGroupsCommand = "maint print reggroups"
    private val getRegisterGroupCommand = "info registers "

    override fun handleSetCommand(register: String, operator: String, value: String): String? {
        val cmd = "set $$register$operator$value"
        val setResponse = runBlocking {
            withTimeout(timeout * 1000) {
                async {
                    driver.executeInterpreterCommand(cmd)
                }.blockingGet()
            }
        }
        log.info("Set command $cmd result: $setResponse")
        return null
    }

    override fun handle() {
        val views = sessionTab.views
        var generalPurposeRegisters = GeneralPurposeRegisters.empty
        var floatingPointRegisters = FloatingPointRegisters.empty
        var otherRegistersAccumulator = emptyList<OtherRegister>()
        val groupsContent = runBlocking {
            withTimeout(timeout * 1000) {
                try {
                    async {
                        driver.executeInterpreterCommand(getGroupsCommand)
                    }.blockingGet()
                } catch (t: Throwable) {
                    log.info("Exception while executing GDB command: $getGroupsCommand", t)
                    handleError(t)
                    return@withTimeout null
                }
            }
        } ?: return

        val groups = GDBRegistersParser.parseGroups(groupsContent)
        for (group in groups) {
            val registersContent = getRegisterGroup(group) ?: continue
            when(group) {
                "general" -> {
                    generalPurposeRegisters = GDBRegistersParser.parseGeneralPurposeRegisters(registersContent)
                }
                "float" -> {
                    floatingPointRegisters = GDBRegistersParser.parseFloatingPointRegisters(registersContent)
                }
                else -> {
                    otherRegistersAccumulator = otherRegistersAccumulator +
                            GDBRegistersParser.parseOther(registersContent).registers
                }
            }
        }

        val parseResult = ParseResult(
            generalPurposeRegisters,
            floatingPointRegisters,
            exceptionState = ExceptionStateRegisters.empty,
            OtherRegisters(otherRegistersAccumulator.distinctBy { it.registerName })
        )

        if (sessionTab.executionId.get() == executionId) {
            synchronized(sessionTab) {
                if (sessionTab.executionId.get() == executionId) {
                    views.forEach { it.rebuildView(parseResult) }
                }
            }
        }
    }

    private fun getRegisterGroup(group: String): String? {
        return runBlocking {
            withTimeout(timeout * 1000) {
                try {
                    async {
                        driver.executeInterpreterCommand(getRegisterGroupCommand + group)
                    }.blockingGet()
                } catch (t: Throwable) {
                    log.info("Exception while executing GDB group command for: $group", t)
                    handleError(t)
                    return@withTimeout null
                }
            }
        }
    }

    companion object {
        val log = Logger.getInstance(GDBDebugHandler::class.java)
    }
}

