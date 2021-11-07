/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.floating

import com.bukowiecki.regdebug.parsers.*
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.ui.CellCreateProvider
import com.bukowiecki.regdebug.ui.RegDebugView
import com.bukowiecki.regdebug.ui.RegisterCell
import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer

/**
 * @author Marcin Bukowiecki
 */
class FloatingPointView(project: Project) : RegDebugView<FloatingPointRegisters>(project), Disposable {

    private lateinit var myFloatingPointRegisters: FloatingPointRegisters

    override fun getViewClass(): Class<*> {
        return FloatingPointView::class.java
    }

    override fun numberOfTables(): Int {
        return RegDebugSettings.getInstance(project).numberOfFloatingPointTables
    }

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

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun loadSettings(settings: RegDebugSettings) {
        filterTextField.text = settings.floatingRegistersToSelect
        registerGroupsTextField.text = settings.numberOfFloatingPointTables.toString()
    }

    override fun updateSettings(settings: RegDebugSettings) {
        settings.floatingRegistersToSelect = filterTextField.text
        try {
            var i: Int = registerGroupsTextField.text.toInt()
            if (i > 10) {
                i = 10
            }
            settings.numberOfFloatingPointTables = i
        } catch (ignored: NumberFormatException) {
        }
    }
}
