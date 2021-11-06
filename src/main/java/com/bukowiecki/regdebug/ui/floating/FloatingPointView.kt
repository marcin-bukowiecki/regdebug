/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.floating

import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.bukowiecki.regdebug.ui.BaseFilterForm
import com.bukowiecki.regdebug.ui.CellCreateProvider
import com.bukowiecki.regdebug.ui.RegDebugView
import com.bukowiecki.regdebug.ui.RegisterCell
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import java.awt.BorderLayout
import javax.swing.BorderFactory

/**
 * @author Marcin Bukowiecki
 */
class FloatingPointView(project: Project) : RegDebugView<FloatingPointRegisters>(project), Disposable {

    private lateinit var myFloatingPointRegisters: FloatingPointRegisters

    private val myHeaderForm = FloatingPointRegistersHeaderForm(this)

    override fun getRegistersHolder(): FloatingPointRegisters {
        return myFloatingPointRegisters
    }

    override fun extractParseResult(parseResult: ParseResult) {
        myFloatingPointRegisters = parseResult.floatingPoint
    }

    override fun getCellCreateProvider(): CellCreateProvider {
        return object : CellCreateProvider() {

            override fun create(project: Project, register: Register, presentation: RegisterPresentation): RegisterCell {
                return FloatingPointRegisterCell(register)
            }
        }
    }

    override fun getActionGroupId(): String {
        return "RegDebug.FloatingPointRegisters"
    }

    override fun getHeaderForm(): BaseFilterForm<FloatingPointRegisters> {
        return myHeaderForm
    }

    override fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
        myHeaderForm.mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        myMainPanel.add(myHeaderForm.mainPanel, BorderLayout.NORTH)
        super.initialize()
    }

    override fun dispose() {
        Disposer.dispose(this)
    }
}
