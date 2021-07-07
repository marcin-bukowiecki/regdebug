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
class RegisterCellContainer(val myRegisterName: String) {

    lateinit var myCell: RegisterCell
    private var myText: String? = null

    var presentation: RegisterPresentation = DefaultPresentation.instance

    fun createCell(project: Project, register: Register, createProvider: CellCreateProvider): RegisterCell {
        this.myCell = createProvider.create(project, register, presentation)
        this.myText = register.hex
        return this.myCell
    }

    fun updateCell(register: Register) {
        myCell.register = register

        val registerLabel = this.myCell.registerLabel
        if (myText != null && myText != register.hex) {
            registerLabel.background = backgroundColor
            registerLabel.isOpaque = true
        } else {
            registerLabel.background = EditorColorsManager.getInstance().schemeForCurrentUITheme.defaultBackground
            registerLabel.isOpaque = false
        }
        registerLabel.revalidate()
        this.myText = register.hex

        refresh()
    }

    fun getMainPanel(): JPanel = myCell.mainPanel

    fun getHexTextField(): JTextField {
        return myCell.hexTextField
    }

    fun isFloatingPoint(): Boolean {
        return myCell.getRegisterType() == RegisterType.FloatingPoint
    }

    fun refresh() {
        myCell.hexTextField.text = presentation.getText(myCell.register)
    }

    fun getOriginalText(): String {
        return myCell.register.hex
    }

    override fun toString(): String {
        return myRegisterName + " " + myCell.hexTextField.text
    }
}