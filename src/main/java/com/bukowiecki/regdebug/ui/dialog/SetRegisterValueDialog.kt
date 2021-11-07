/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.dialog

import com.bukowiecki.regdebug.backend.DebugHandler
import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.ui.RegisterCellContainer
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.icons.AllIcons
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import javax.swing.JComponent

/**
 * @author Marcin Bukowiecki
 */
class SetRegisterValueDialog(private val registerCellContainer: RegisterCellContainer,
                             private val handler: DebugHandler,
                             private val project: Project) : DialogWrapper(project) {

  private val registerName = registerCellContainer.myRegisterName
  private val mainView = SetRegisterValuePanel(handler.backendType)

  init {
    setOKButtonText(RegDebugBundle.message("regdebug.register.set"))
    title = RegDebugBundle.message("regdebug.register.setValue", registerName)
    init()
  }

  override fun doOKAction() {
    val operator = mainView.operatorComboBox.selectedItem as String
    val value = mainView.registerValueTextField.text
    val currentSession = XDebuggerManagerImpl.getInstance(project).currentSession ?: kotlin.run {
      super.doOKAction()
      return
    }
    val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return
    val err = handler.handleSetCommand(registerName, operator, value)
    if (err != null) {
      mainView.messageLabel.icon = AllIcons.Actions.IntentionBulb
      mainView.messageLabel.text = err
      return
    }

    debugProcess.getUserData(DataKeys.debugHandler)?.handle()
    super.doOKAction()
  }

  override fun createCenterPanel(): JComponent {
    return mainView.mainPanel
  }
}
