/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.ui.dialog.SetRegisterValueDialog
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess

/**
 * @author Marcin Bukowiecki
 */
class SetRegisterValueAction : RegDebugBaseAction() {

  override fun actionPerformed(e: AnActionEvent) {
    val currentSession = XDebuggerManagerImpl.getInstance(e.project ?: return).currentSession ?: return
    val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return
    val handler = debugProcess.getUserData(DataKeys.debugHandler) ?: return
    val registerCellContainer = getRegisterCellContainer(e) ?: return
    val setRegisterValueDialog = SetRegisterValueDialog(registerCellContainer, handler, e.project ?: return)
    setRegisterValueDialog.show()
  }

  override fun isEnabled(e: AnActionEvent): Boolean {
    return getRegisterCellContainer(e) != null
  }
}
