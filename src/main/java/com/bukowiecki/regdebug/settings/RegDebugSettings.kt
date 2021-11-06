/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.settings

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.project.Project

/**
 * @author Marcin Bukowiecki
 */
@State(
    name = "com.bukowiecki.regdebug.settings.RegDebugSettings",
    storages = [Storage("RegDebugSettings.xml")]
)
class RegDebugSettings : PersistentStateComponent<RegDebugSettings>, Disposable {

    var generalRegistersToSelect: String = ""

    var floatingRegistersToSelect: String = ""

    var otherRegistersToSelect: String = ""

    var showFloatingPointRegisters: Boolean = true

    var showExceptionStateRegisters: Boolean = true

    var showOtherRegisters: Boolean = true

    var openOnStartup: Boolean = false

    var registersLoadingTimeout = 5L

    var numberOfGeneralPurposeTables: Int = 3

    var numberOfFloatingPointTables: Int = 3

    var numberOfExceptionStateTables: Int = 1

    var numberOfOtherTables: Int = 2

    override fun getState(): RegDebugSettings {
        return this
    }

    override fun loadState(state: RegDebugSettings) {
        this.generalRegistersToSelect = state.generalRegistersToSelect
        this.floatingRegistersToSelect = state.floatingRegistersToSelect
        this.showFloatingPointRegisters = state.showFloatingPointRegisters
        this.registersLoadingTimeout = state.registersLoadingTimeout
        this.showExceptionStateRegisters = state.showExceptionStateRegisters
        this.showOtherRegisters = state.showOtherRegisters
        this.openOnStartup = state.openOnStartup
        this.otherRegistersToSelect = state.otherRegistersToSelect
        this.numberOfGeneralPurposeTables = state.numberOfGeneralPurposeTables
        this.numberOfFloatingPointTables = state.numberOfFloatingPointTables
        this.numberOfOtherTables = state.numberOfOtherTables
        this.numberOfExceptionStateTables = state.numberOfExceptionStateTables
    }

    override fun dispose() {

    }

    companion object {

        @JvmStatic
        fun getInstance(project: Project): RegDebugSettings {
            return project.getService(RegDebugSettings::class.java)
        }
    }
}
