/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.Register
import com.bukowiecki.regdebug.parsers.RegisterType
import com.bukowiecki.regdebug.presentation.DefaultPresentation
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import java.awt.Color
import javax.swing.JPanel
import javax.swing.JTextField

val backgroundColor = Color(50, 89, 61)

/**
 * @author Marcin Bukowiecki
 */
class RegisterCellContainer {

    lateinit var cell: RegisterCell

    private var text: String? = null

    var presentation: RegisterPresentation = DefaultPresentation.instance

    fun createCell(project: Project, register: Register, createProvider: CellCreateProvider): RegisterCell {
        this.cell = createProvider.create(project, register, presentation)

        val registerLabel = this.cell.registerLabel
        if (text != null && text != this.cell.getText()) {
            registerLabel.background = backgroundColor
            registerLabel.isOpaque = true
        } else {
            registerLabel.background = EditorColorsManager.getInstance().schemeForCurrentUITheme.defaultBackground
            registerLabel.isOpaque = false
        }
        registerLabel.revalidate()

        this.text = cell.getText()

        return this.cell
    }

    fun getMainPanel(): JPanel = cell.mainPanel

    fun getHexTextField(): JTextField {
        return cell.hexTextField
    }

    fun isFloatingPoint(): Boolean {
        return cell.getRegisterType() == RegisterType.FloatingPoint
    }

    fun refresh() {
        cell.hexTextField.text = presentation.getText(cell.register)
    }

    fun getOriginalText(): String {
        return cell.register.hex
    }
}