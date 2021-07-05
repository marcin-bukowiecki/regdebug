/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.intellij.execution.ui.RunnerLayoutUi
import com.intellij.lang.Language
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.util.ReflectionUtil
import com.intellij.xdebugger.evaluation.EvaluationMode
import com.intellij.xdebugger.impl.XDebuggerManagerImpl
import com.intellij.xdebugger.impl.breakpoints.XExpressionImpl
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.memory.GotoAddressInputComponent
import javax.swing.JButton
import javax.swing.SwingUtilities

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

        hexdumpViewPanel.gotoAddressPanel.components?.forEach { component ->
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