/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.other

import com.bukowiecki.regdebug.parsers.OtherRegisters
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.Register
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
class OtherRegistersView(project: Project) : RegDebugView<OtherRegisters>(project), Disposable {

    private lateinit var myOtherRegisters: OtherRegisters

    private val myHeaderForm = OtherRegistersHeaderForm(this)

    override fun getRegistersHolder(): OtherRegisters {
        return myOtherRegisters
    }

    override fun extractParseResult(parseResult: ParseResult) {
        myOtherRegisters = parseResult.other
    }

    override fun getCellCreateProvider(): CellCreateProvider {
        return object : CellCreateProvider() {

            override fun create(project: Project, register: Register, presentation: RegisterPresentation): RegisterCell {
                return OtherRegisterCell(register)
            }
        }
    }

    override fun getActionGroupId(): String {
        return "RegDebug.OtherRegisters"
    }

    override fun getHeaderForm(): BaseFilterForm<OtherRegisters> = myHeaderForm

    override fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
        myHeaderForm.mainPanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)
        myMainPanel.add(myHeaderForm.mainPanel, BorderLayout.NORTH)
    }

    override fun dispose() {
        Disposer.dispose(this)
    }
}
