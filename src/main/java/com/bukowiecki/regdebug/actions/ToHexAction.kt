/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.presentation.DefaultPresentation
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * @author Marcin Bukowiecki
 */
class ToHexAction : RegDebugBaseAction() {

    override fun actionPerformed(e: AnActionEvent) {
        getRegisterCellContainer(e)?.let { registerCellContainer ->
            registerCellContainer.presentation = DefaultPresentation.instance
            registerCellContainer.refresh()
        }
    }
}
