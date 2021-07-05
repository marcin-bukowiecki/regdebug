/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.FloatingPointRegisters
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.Register
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import javax.swing.BorderFactory

/**
 * @author Marcin Bukowiecki
 */
class FloatingPointView(project: Project) : RegDebugView(project), Disposable {

    private lateinit var floatingPointRegisters: FloatingPointRegisters

    override fun rebuildView(parseResult: ParseResult) {
        floatingPointRegisters = parseResult.floatingPoint ?: return

        this.myMainPanel.removeAll()

        if (!cellsSetup) {
            setupCells(floatingPointRegisters.registers)
        }

        initialize()
        createCells(floatingPointRegisters.registers)
        buildCellsView()
        addActions()
    }

    override fun dispose() {
        Disposer.dispose(this)
    }

    private fun initialize() {
        myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
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
}