/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.Register
import com.bukowiecki.regdebug.parsers.RegisterType
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

/**
 * @author Marcin Bukowiecki
 */
interface RegisterCell {

    val registerLabel: JLabel
    val mainPanel: JPanel
    val hexTextField: JTextField
    val register: Register

    fun getText(): String {
        return hexTextField.text
    }

    fun getRegisterType(): RegisterType {
        return register.registerType
    }
}