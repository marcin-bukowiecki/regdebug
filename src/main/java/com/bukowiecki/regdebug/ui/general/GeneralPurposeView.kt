/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.general

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.parsers.FLAGSParser
import com.bukowiecki.regdebug.parsers.GeneralPurposeRegisters
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.Register
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.ui.*
import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.ui.JBColor
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import java.awt.Color
import java.awt.Dimension
import javax.swing.JLabel
import javax.swing.JTextField

/**
 * @author Marcin Bukowiecki
 */
class GeneralPurposeView(project: Project) : RegDebugView<GeneralPurposeRegisters>(project), Disposable {

    private lateinit var myGeneralPurposeRegisters: GeneralPurposeRegisters

    private val flagsTextField = JTextField()

    init {
        flagsTextField.isEnabled = false
        val flagsPanel = BorderLayoutPanel()
        flagsPanel.add(JLabel(RegDebugBundle.message("regdebug.flags")), BorderLayout.WEST)
        flagsTextField.columns = 30
        flagsPanel.add(flagsTextField, BorderLayout.CENTER)
        toolboxPanel.add(flagsPanel, BorderLayout.EAST)
    }

    override fun getViewClass(): Class<*> {
        return GeneralPurposeView::class.java
    }

    override fun numberOfColumns(): Int {
        return 3
    }

    override fun numberOfTables(): Int {
        return RegDebugSettings.getInstance(project).numberOfGeneralPurposeTables
    }

    override fun rebuildView(parseResult: ParseResult) {
        statusLabel.text = "Loading registers..."
        statusLabel.foreground = EditorColorsManager.getInstance().schemeForCurrentUITheme.defaultForeground
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
            flagsTextField.text = content
        }
        statusLabel.text = ""
        myMainPanel.revalidate()
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
        statusLabel.text = RegDebugBundle.message("regdebug.error", message ?: "")
        val errorColor = JBColor(Color(188, 63, 60), Color(188, 63, 60))
        statusLabel.foreground = errorColor
    }

    override fun getActionGroupId(): String {
        return "RegDebug.GeneralPurposeRegisters"
    }

    override fun tableModelProvider(registerCellContainers: List<RegisterCellContainer>): () -> RegDebugRegisterTableModel {
        return {
            GeneralPurposeTableModel(registerCellContainers, numberOfColumns())
        }
    }

    override fun loadSettings(settings: RegDebugSettings) {
        filterTextField.text = settings.generalRegistersToSelect
        registerGroupsTextField.text = settings.numberOfGeneralPurposeTables.toString()
    }

    override fun updateSettings(settings: RegDebugSettings) {
        settings.generalRegistersToSelect = filterTextField.text
        try {
            var i: Int = registerGroupsTextField.text.toInt()
            if (i > 10) {
                i = 10
            }
            settings.numberOfGeneralPurposeTables = i
        } catch (ignored: NumberFormatException) {
        }
    }
}
