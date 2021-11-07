/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.presentation.DefaultPresentation
import com.bukowiecki.regdebug.ui.RegisterCellContainer
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

/**
 * @author Marcin Bukowiecki
 */
abstract class RegDebugBaseAction : AnAction() {

  override fun update(e: AnActionEvent) {
    e.presentation.isEnabled = isEnabled(e)
    e.presentation.isVisible = e.presentation.isEnabled
    super.update(e)
  }

  fun getRegisterCellContainer(e: AnActionEvent): RegisterCellContainer? {
    return e.getData(DataKeys.registerCellContainer)
  }

  open fun isEnabled(e: AnActionEvent): Boolean {
    val registerCellContainer = getRegisterCellContainer(e) ?: return false
    if (registerCellContainer.presentation is DefaultPresentation) return false
    if (registerCellContainer.isFloatingPoint() || registerCellContainer.isExceptionState()) return false
    return true
  }
}
