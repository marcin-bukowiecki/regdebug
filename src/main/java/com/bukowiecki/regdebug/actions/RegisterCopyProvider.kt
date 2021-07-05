/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.actions

import com.bukowiecki.regdebug.ui.RegisterCell
import com.intellij.ide.CopyProvider
import com.intellij.openapi.actionSystem.DataContext
import com.intellij.openapi.ide.CopyPasteManager
import java.awt.datatransfer.StringSelection

/**
 * @author Marcin Bukowiecki
 */
class RegisterCopyProvider(private val registerCell: RegisterCell) : CopyProvider {

    override fun performCopy(dataContext: DataContext) {
        CopyPasteManager.getInstance().setContents(StringSelection(registerCell.hexTextField.text))
    }

    override fun isCopyEnabled(dataContext: DataContext): Boolean = true

    override fun isCopyVisible(dataContext: DataContext): Boolean = true
}