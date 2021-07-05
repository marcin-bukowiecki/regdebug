/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBColor
import java.awt.BorderLayout
import java.awt.Color
import javax.swing.BorderFactory

/**
 * @author Marcin Bukowiecki
 */
class GeneralPurposeView(project: Project) : RegDebugView(project), Disposable {

    private lateinit var generalPurposeRegisters: GeneralPurposeRegisters

    private val flagsForm = FlagsCell()

    override fun rebuildView(parseResult: ParseResult) {
        flagsForm.headerLabel.text = "Loading registers..."
        flagsForm.headerLabel.foreground = EditorColorsManager.getInstance().schemeForCurrentUITheme.defaultForeground

        generalPurposeRegisters = parseResult.generalPurpose ?: return

        this.myMainPanel.removeAll()

        if (!cellsSetup) {
            setupCells(generalPurposeRegisters.registers)
        }

        initialize()
        createCells(generalPurposeRegisters.registers)
        buildCellsView()
        addActions()

        generalPurposeRegisters.findFlagsRegister()?.let {
            val flagsParsed = FLAGSParser.parse(it.hex)
            val content = if (flagsParsed.isEmpty()) {
                ""
            } else {
                flagsParsed.joinToString(", ") { entry -> entry.symbol }
            }
            flagsForm.flagsTextField.text = content
        }

        flagsForm.headerLabel.text = ""
    }

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun getCellCreateProvider(): CellCreateProvider {
        return object : CellCreateProvider() {

            override fun create(project: Project, register: Register, presentation: RegisterPresentation): RegisterCell {
                return GeneralPurposeRegisterCell(register, presentation)
            }
        }
    }

    override fun addErrorMessages(message: String?) {
        flagsForm.headerLabel.text = RegDebugBundle.message("regdebug.error", message ?: "")
        val errorColor = JBColor(Color(188, 63, 60), Color(188, 63, 60))
        flagsForm.headerLabel.foreground = errorColor
    }

    override fun getActionGroupId(): String {
        return "RegDebug.GeneralPurposeRegisters"
    }

    private fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
        flagsForm.mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        myMainPanel.add(flagsForm.mainPanel, BorderLayout.NORTH)
    }
}