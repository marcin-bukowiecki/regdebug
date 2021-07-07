/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.intellij.execution.ui.RunnerLayoutUi
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess

/**
 * @author Marcin Bukowiecki
 */
class ShowMemoryViewAction : ShowMemoryViewActionBase() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = isEnabled(e)
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val address = getAddress(e) ?: return

        val currentSession = XDebuggerManagerImpl.getInstance(project).currentSession ?: return
        val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return

        val ui: RunnerLayoutUi = currentSession.ui
        val gdbContent = ui.findContent("DEBUGGER_MEMORY_VIEW")
        ui.selectAndFocus(gdbContent, true, true)

        val hexdumpViewPanel = debugProcess.hexdumpViewPanel ?: return

        handleAddressPanel(address, hexdumpViewPanel.gotoAddressPanel)
    }
}