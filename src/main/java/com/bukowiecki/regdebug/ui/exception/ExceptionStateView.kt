/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.exception

import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.bukowiecki.regdebug.settings.RegDebugSettings
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
class ExceptionStateView(project: Project) : RegDebugView<RegistersHolder<ExceptionStateRegister>>(project), Disposable {

    private lateinit var myExceptionStateRegisters: RegistersHolder<ExceptionStateRegister>

    override fun numberOfTables(): Int {
        return RegDebugSettings.getInstance(project).numberOfExceptionStateTables
    }

    override fun getRegistersHolder(): RegistersHolder<ExceptionStateRegister> {
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
        super.initialize()
    }

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun getHeaderForm(): BaseFilterForm<RegistersHolder<ExceptionStateRegister>>? = null
}
