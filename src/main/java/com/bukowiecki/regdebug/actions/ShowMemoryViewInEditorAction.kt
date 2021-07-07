/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.memory.GotoAddressPanel
import com.jetbrains.cidr.execution.debugger.memory.MemoryViewFile

/**
 * @author Marcin Bukowiecki
 */
class ShowMemoryViewInEditorAction : ShowMemoryViewActionBase() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = isEnabled(e)
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val address = getAddress(e) ?: return
        val action = ActionManager.getInstance().getAction("CIDR.Debugger.ShowMemoryViewInEditor")
        action.actionPerformed(e)

        val selectedEditors = FileEditorManager.getInstance(project).selectedEditors

        for (selectedEditor in selectedEditors) {
            if (selectedEditor is PsiAwareTextEditorImpl && selectedEditor.file is MemoryViewFile) {
                val gotoAddressPanel = selectedEditor.getUserData(GotoAddressPanel.GOTO_ADDRESS_FILE_EDITOR_KEY) ?: return
                val currentSession = XDebuggerManagerImpl.getInstance(project).currentSession ?: return
                val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return
                val sessionTab = debugProcess.getUserData(DataKeys.sessionTab) ?: return

                synchronized(sessionTab) {
                    sessionTab.memoryEditorViews.add(selectedEditor)
                }

                handleAddressPanel(address, gotoAddressPanel)
            }
        }
    }
}