/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.exception

import com.bukowiecki.regdebug.parsers.ExceptionStateRegisters
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
import javax.swing.BorderFactory

/**
 * @author Marcin Bukowiecki
 */
class ExceptionStateView(project: Project) : RegDebugView<ExceptionStateRegisters>(project), Disposable {

    private lateinit var myExceptionStateRegisters: ExceptionStateRegisters

    override fun getRegistersHolder(): ExceptionStateRegisters {
        return myExceptionStateRegisters
    }

    override fun extractParseResult(parseResult: ParseResult) {
        myExceptionStateRegisters = parseResult.exceptionState
    }

    override fun getCellCreateProvider(): CellCreateProvider {
        return object : CellCreateProvider() {

            override fun create(project: Project, register: Register, presentation: RegisterPresentation): RegisterCell {
                return ExceptionStateRegisterCell(register)
            }
        }
    }

    override fun getActionGroupId(): String {
        return "RegDebug.ExceptionStateRegisters"
    }

    override fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
    }

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun getHeaderForm(): BaseFilterForm<ExceptionStateRegisters>? = null
}