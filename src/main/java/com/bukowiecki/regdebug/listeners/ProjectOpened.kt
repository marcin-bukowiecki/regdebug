/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.listeners

import com.bukowiecki.regdebug.backend.DebugHandler
import com.bukowiecki.regdebug.backend.gdb.GDBDebugHandler
import com.bukowiecki.regdebug.backend.lldb.LLDBDebugHandler
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.ui.floating.FloatingPointView
import com.bukowiecki.regdebug.ui.general.GeneralPurposeView
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.bukowiecki.regdebug.ui.exception.ExceptionStateView
import com.bukowiecki.regdebug.ui.other.OtherRegistersView
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupActivity
import com.intellij.openapi.util.Disposer
import com.intellij.xdebugger.XDebugProcess
import com.intellij.xdebugger.XDebugSessionListener
import com.intellij.xdebugger.XDebuggerManager
import com.intellij.xdebugger.XDebuggerManagerListener
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.gdb.GDBDriver
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver

/**
 * @author Marcin Bukowiecki
 */
class ProjectOpened : StartupActivity {

    @Suppress("UnstableApiUsage")
    override fun runActivity(project: Project) {
        val simpleConnect = project.messageBus.connect()
        Disposer.register(project, simpleConnect)

        simpleConnect.subscribe(
            XDebuggerManager.TOPIC, object : XDebuggerManagerListener {

                override fun processStarted(debugProcess: XDebugProcess) {
                    if (debugProcess is CidrDebugProcess) {
                        ApplicationManager.getApplication().invokeLater {
                            addSessionListener(project, debugProcess)
                        }
                    }
                }
            }
        )

        //debug process launched after this project activity was triggered
        XDebuggerManager.getInstance(project).getDebugProcesses(CidrDebugProcess::class.java).forEach { process ->
            val sessionTab = process.getUserData(DataKeys.sessionTab)
            if (sessionTab == null) {
                ApplicationManager.getApplication().invokeLater {
                    addSessionListener(project, process)
                }
            }
        }
    }

    private fun addSessionListener(project: Project, debugProcess: CidrDebugProcess) {
        synchronized(debugProcess) {
            val regDebugSessionTab = RegDebugSessionTab(debugProcess)
            debugProcess.putUserData(DataKeys.sessionTab, regDebugSessionTab)

            val debugHandler = createHandler(regDebugSessionTab, debugProcess) ?: return
            debugProcess.putUserData(DataKeys.debugHandler, debugHandler)

            regDebugSessionTab.registerGeneralPurposeView(GeneralPurposeView(project))

            if (RegDebugSettings.getInstance(project).showFloatingPointRegisters) {
                regDebugSessionTab.registerFloatingPointView(FloatingPointView(project))
            }

            if (debugProcess.driverInTests is LLDBDriver) {
                if (RegDebugSettings.getInstance(project).showExceptionStateRegisters) {
                    regDebugSessionTab.registerExceptionStateView(ExceptionStateView(project))
                }
            }
            else if (debugProcess.driverInTests is GDBDriver) {
                if (RegDebugSettings.getInstance(project).showOtherRegisters) {
                    regDebugSessionTab.registerOtherRegistersView(OtherRegistersView(project))
                }
            }

            debugProcess.session.addSessionListener(object : XDebugSessionListener {

                override fun sessionPaused() {
                    regDebugSessionTab.rebuildViews(debugHandler)
                }
            })
        }
    }

    private fun createHandler(sessionTab: RegDebugSessionTab,
                              debugProcess: CidrDebugProcess): DebugHandler? {

        return when (val driverInTests = debugProcess.driverInTests) {
            is LLDBDriver -> {
                LLDBDebugHandler(sessionTab, driverInTests)
            }
            is GDBDriver -> {
                GDBDebugHandler(sessionTab, driverInTests)
            }
            else -> {
                if (driverInTests == null || driverInTests.javaClass.canonicalName == null) {
                    RegDebugSessionTab.log.info("Debug process driver is null")
                } else {
                    RegDebugSessionTab.log.info("Debug process driver: ${driverInTests.javaClass.canonicalName} is not supported")
                }
                null
            }
        }
    }
}
