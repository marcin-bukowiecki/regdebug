/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.util.ReflectionUtil
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.intellij.xdebugger.impl.breakpoints.XExpressionImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.memory.GotoAddressInputComponent
import com.jetbrains.cidr.execution.debugger.memory.GotoAddressPanel
import javax.swing.JButton
import javax.swing.SwingUtilities

/**
 * @author Marcin Bukowiecki
 */
abstract class ShowMemoryViewActionBase : AnAction() {

    fun getAddress(e: AnActionEvent): String? {
        val cellContainer = e.getData(DataKeys.registerCellContainer) ?: return null
        return cellContainer.getOriginalText()
    }

    fun isEnabled(e: AnActionEvent): Boolean {
        val project = e.project ?: return false

        val data = e.getData(DataKeys.registerCellContainer)
        if (data == null || data.isFloatingPoint() || data.isExceptionState() || getAddress(e) == null) {
            return false
        }

        val currentSession = XDebuggerManagerImpl.getInstance(project).currentSession ?: return false
        val debugProcess = currentSession.debugProcess as? CidrDebugProcess ?: return false
        debugProcess.getUserData(DataKeys.sessionTab) ?: return false

        return true
    }

    fun handleAddressPanel(address: String, gotoAddressPanel: GotoAddressPanel) {
        gotoAddressPanel.components?.forEach { component ->
            if (component is GotoAddressInputComponent) {
                component.expressionInput.expression = XExpressionImpl(
                    address,
                    Language.findLanguageByID("ObjectiveC"), "", EvaluationMode.EXPRESSION
                )
                component.expressionInput.comboBox.putClientProperty("JComponent.outline", null as Any?)
                component.expressionInput.saveTextInHistory()

                val declaredField =
                    ReflectionUtil.getDeclaredField(
                        GotoAddressInputComponent::class.java,
                        "evaluateButton"
                    ) ?: return

                declaredField.isAccessible = true
                val btn = declaredField.get(component) as? JButton ?: return
                SwingUtilities.invokeLater { btn.doClick() }
                return
            }
        }
    }
}
