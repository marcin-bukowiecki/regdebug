/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.other

import com.bukowiecki.regdebug.parsers.OtherRegisters
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.Register
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
class OtherRegistersView(project: Project) : RegDebugView<OtherRegisters>(project), Disposable {

    private lateinit var myOtherRegisters: OtherRegisters

    override fun getViewClass(): Class<*> {
        return OtherRegistersView::class.java
    }

    override fun numberOfTables(): Int {
        return RegDebugSettings.getInstance(project).numberOfOtherTables
    }

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

    override fun dispose() {
        Disposer.dispose(this)
    }

    override fun loadSettings(settings: RegDebugSettings) {
        filterTextField.text = settings.otherRegistersToSelect
        registerGroupsTextField.text = settings.numberOfOtherTables.toString()
    }

    override fun updateSettings(settings: RegDebugSettings) {
        settings.otherRegistersToSelect = filterTextField.text
        try {
            var i: Int = registerGroupsTextField.text.toInt()
            if (i > 10) {
                i = 10
            }
            settings.numberOfOtherTables = i
        } catch (ignored: NumberFormatException) {
        }
    }
}
