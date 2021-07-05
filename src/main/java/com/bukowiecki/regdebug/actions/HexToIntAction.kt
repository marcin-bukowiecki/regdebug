/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.presentation.IntegerPresentation
import com.bukowiecki.regdebug.ui.RegisterCellContainer
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * @author Marcin Bukowiecki
 */
class HexToIntAction : AnAction() {

    override fun update(e: AnActionEvent) {
        e.presentation.isEnabled = isEnabled(e)
        e.presentation.isVisible = e.presentation.isEnabled
        super.update(e)
    }

    override fun actionPerformed(e: AnActionEvent) {
        getRegisterCellContainer(e)?.let { registerCellContainer ->
            registerCellContainer.presentation = IntegerPresentation.instance
            registerCellContainer.refresh()
        }
    }

    private fun getRegisterCellContainer(e: AnActionEvent): RegisterCellContainer? {
        return e.getData(DataKeys.registerCellContainer)
    }

    private fun isEnabled(e: AnActionEvent): Boolean {
        e.project ?: return false

        val registerCellContainer = getRegisterCellContainer(e) ?: return false

        if (registerCellContainer.presentation is IntegerPresentation) return false

        if (registerCellContainer.isFloatingPoint()) return false

        return true
    }
}