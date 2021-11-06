/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.settings

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.intellij.openapi.options.Configurable
import com.intellij.openapi.project.Project
import javax.swing.JComponent

/**
 * @author Marcin Bukowiecki
 */
class RegDebugConfigurable(private val project: Project) : Configurable {

    private var settingsComponent: RegDebugSettingsPanel? = null

    override fun createComponent(): JComponent? {
        settingsComponent = RegDebugSettingsPanel()
        return settingsComponent?.mainPanel
    }

    override fun isModified(): Boolean {
        val settings = RegDebugSettings.getInstance(project)
        var modified: Boolean = settingsComponent?.registersLoadingTimeout?.text != settings.registersLoadingTimeout.toString()
        modified = modified || settingsComponent?.showFloatingPointRegisters?.isSelected != settings.showFloatingPointRegisters
        modified = modified || settingsComponent?.showExceptionStateRegisters?.isSelected != settings.showExceptionStateRegisters
        modified = modified || settingsComponent?.showOtherRegisters?.isSelected != settings.showOtherRegisters
        modified = modified || settingsComponent?.openOnStartup?.isSelected != settings.openOnStartup
        modified = modified || settingsComponent?.numberOfGeneralPurposeTables?.text != settings.numberOfGeneralPurposeTables.toString()
        modified = modified || settingsComponent?.numberOfFloatingPointTables?.text != settings.numberOfFloatingPointTables.toString()
        modified = modified || settingsComponent?.numberOfExceptionsStateTables?.text != settings.numberOfExceptionStateTables.toString()
        modified = modified || settingsComponent?.numberOfOtherTables?.text != settings.numberOfOtherTables.toString()
        return modified
    }

    override fun apply() {
        val settings = RegDebugSettings.getInstance(project)
        settings.registersLoadingTimeout = settingsComponent?.registersLoadingTimeout?.text?.toLong() ?: return
        settings.showFloatingPointRegisters = settingsComponent?.showFloatingPointRegisters?.isSelected ?: false
        settings.showExceptionStateRegisters = settingsComponent?.showExceptionStateRegisters?.isSelected ?: false
        settings.showOtherRegisters = settingsComponent?.showOtherRegisters?.isSelected ?: false
        settings.openOnStartup = settingsComponent?.openOnStartup?.isSelected ?: false
        settings.numberOfGeneralPurposeTables = settingsComponent?.numberOfGeneralPurposeTables?.text?.toInt() ?: return
        settings.numberOfFloatingPointTables = settingsComponent?.numberOfFloatingPointTables?.text?.toInt() ?: return
        settings.numberOfOtherTables = settingsComponent?.numberOfOtherTables?.text?.toInt() ?: return
        settings.numberOfExceptionStateTables = settingsComponent?.numberOfExceptionsStateTables?.text?.toInt() ?: return
    }

    override fun getDisplayName(): String {
        return RegDebugBundle.getMessage("regdebug.settings")
    }

    override fun reset() {
        val instance = RegDebugSettings.getInstance(project)

        settingsComponent?.registersLoadingTimeout?.text = instance.registersLoadingTimeout.toString()
        settingsComponent?.showFloatingPointRegisters?.isSelected = instance.showFloatingPointRegisters
        settingsComponent?.showExceptionStateRegisters?.isSelected = instance.showExceptionStateRegisters
        settingsComponent?.showOtherRegisters?.isSelected = instance.showOtherRegisters
        settingsComponent?.openOnStartup?.isSelected = instance.openOnStartup

        settingsComponent?.numberOfGeneralPurposeTables?.text = instance.numberOfGeneralPurposeTables.toString()
        settingsComponent?.numberOfFloatingPointTables?.text = instance.numberOfFloatingPointTables.toString()
        settingsComponent?.numberOfOtherTables?.text = instance.numberOfOtherTables.toString()
        settingsComponent?.numberOfExceptionsStateTables?.text = instance.numberOfExceptionStateTables.toString()
    }

    companion object {

        fun getInstance(project: Project): RegDebugConfigurable {
            return RegDebugConfigurable(project)
        }
    }
}
