/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.backend.gdb

import com.bukowiecki.regdebug.backend.BaseDebugHandler
import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.parsers.gdb.GDBRegistersParser
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.jetbrains.cidr.execution.debugger.backend.gdb.GDBDriver
import java.util.concurrent.CompletableFuture
import java.util.concurrent.TimeUnit

/**
 * @author Marcin Bukowiecki
 */
class GDBDebugHandler(sessionTab: RegDebugSessionTab, private val driver: GDBDriver) : BaseDebugHandler(sessionTab) {

    private val getGroupsCommand = "maint print reggroups"
    private val getRegisterGroupCommand = "info registers "

    override fun handle() {
        val views = sessionTab.views
        var generalPurposeRegisters = GeneralPurposeRegisters.empty
        var floatingPointRegisters = FloatingPointRegisters.empty
        var otherRegistersAccumulator = emptyList<OtherRegister>()

        val groupsContent = CompletableFuture.supplyAsync {
            driver.executeInterpreterCommand(getGroupsCommand)
        }.get(timeout, TimeUnit.SECONDS)

        val groups = GDBRegistersParser.parseGroups(groupsContent)
        groups.forEach { group ->
            val registersContent = getRegisterGroup(group)
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

    private fun getRegisterGroup(group: String): String {
        return CompletableFuture.supplyAsync {
            driver.executeInterpreterCommand(getRegisterGroupCommand + group)
        }.get(timeout, TimeUnit.SECONDS)
    }
}
