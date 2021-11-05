/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.general

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.bukowiecki.regdebug.ui.BaseFilterForm
import com.bukowiecki.regdebug.ui.CellCreateProvider
import com.bukowiecki.regdebug.ui.RegDebugView
import com.bukowiecki.regdebug.ui.RegisterCell
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
class GeneralPurposeView(project: Project) : RegDebugView<GeneralPurposeRegisters>(project), Disposable {

    private lateinit var myGeneralPurposeRegisters: GeneralPurposeRegisters

    private val myHeaderForm = GeneralPurposeRegistersHeaderForm(this)

    override fun numberOfColumns(): Int {
        return 3
    }

    override fun rebuildView(parseResult: ParseResult) {
        myHeaderForm.statusLabel.text = "Loading registers..."
        myHeaderForm.statusLabel.foreground = EditorColorsManager.getInstance().schemeForCurrentUITheme.defaultForeground
        super.rebuildView(parseResult)
    }

    override fun afterRebuild() {
        myGeneralPurposeRegisters.findFlagsRegister()?.let {
            val flagsParsed = FLAGSParser.parse(it.hex)
            val content = if (flagsParsed.isEmpty()) {
                ""
            } else {
                flagsParsed.joinToString(", ") { entry -> entry.symbol }
            }
            myHeaderForm.flagsTextField.text = content
        }

        myHeaderForm.statusLabel.text = ""

        cellsPanel.revalidate()
    }

    override fun extractParseResult(parseResult: ParseResult) {
        myGeneralPurposeRegisters = parseResult.generalPurpose
    }

    override fun getRegistersHolder(): GeneralPurposeRegisters {
        return myGeneralPurposeRegisters
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
        myHeaderForm.statusLabel.text = RegDebugBundle.message("regdebug.error", message ?: "")
        val errorColor = JBColor(Color(188, 63, 60), Color(188, 63, 60))
        myHeaderForm.statusLabel.foreground = errorColor
    }

    override fun getActionGroupId(): String {
        return "RegDebug.GeneralPurposeRegisters"
    }

    override fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
        myHeaderForm.mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        myMainPanel.add(myHeaderForm.mainPanel, BorderLayout.NORTH)
    }

    override fun getHeaderForm(): BaseFilterForm<GeneralPurposeRegisters> {
        return myHeaderForm
    }
}
