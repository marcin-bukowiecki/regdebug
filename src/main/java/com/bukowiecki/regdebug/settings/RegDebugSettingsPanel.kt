/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.settings

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBTextField
import com.intellij.util.ui.FormBuilder
import javax.swing.JPanel

/**
 * @author Marcin Bukowiecki
 */
class RegDebugSettingsPanel {

    val mainPanel: JPanel
    val registersLoadingTimeout = JBTextField()
    val showFloatingPointRegisters = JBCheckBox()
    val showExceptionStateRegisters = JBCheckBox()
    val showOtherRegisters = JBCheckBox()
    val openOnStartup = JBCheckBox()
    val numberOfGeneralPurposeTables = JBTextField()
    val numberOfFloatingPointTables = JBTextField()
    val numberOfExceptionsStateTables = JBTextField()
    val numberOfOtherTables = JBTextField()

    init {
        mainPanel = FormBuilder.createFormBuilder()
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.registerLoadingTimeout")), registersLoadingTimeout,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.showFloatingPoints")), showFloatingPointRegisters,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.showExceptionState")), showExceptionStateRegisters,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.showOtherRegisters")), showOtherRegisters,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.openOnStartup")), openOnStartup,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.registerLoadingTimeout")), registersLoadingTimeout,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.generalPurposeTables")), numberOfGeneralPurposeTables,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.floatingPointTables")), numberOfFloatingPointTables,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.exceptionStateTables")), numberOfExceptionsStateTables,
                1, false
            )
            .addLabeledComponent(
                JBLabel(RegDebugBundle.message("regdebug.settings.otherTables")), numberOfOtherTables,
                1, false
            )
            .addComponentFillVertically(JPanel(), 0)
            .panel
    }
}
