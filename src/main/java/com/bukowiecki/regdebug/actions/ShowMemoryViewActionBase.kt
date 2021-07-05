/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess

/**
 * @author Marcin Bukowiecki
 */
abstract class ShowMemoryViewActionBase : AnAction() {

    fun getAddress(e: AnActionEvent): String? {
        val cellContainer = e.getData(DataKeys.registerCellContainer) ?: return null
        return cellContainer.getOriginalText();
    }

    fun isEnabled(e: AnActionEvent): Boolean {
        val project = e.project ?: return false

        val data = e.getData(DataKeys.registerCellContainer)
        if (data == null || data.isFloatingPoint() || getAddress(e) == null) {
            return false
        }

        val currentSession = XDebuggerManagerImpl.getInstance(project).currentSession ?: return false
        val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return false
        debugProcess.getUserData(DataKeys.sessionTab) ?: return false

        return true
    }
}