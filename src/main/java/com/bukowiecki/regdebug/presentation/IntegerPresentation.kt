/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.presentation

import com.bukowiecki.regdebug.parsers.Register

/**
 * @author Marcin Bukowiecki
 */
class IntegerPresentation : RegisterPresentation {

    override fun getText(register: Register): String {
        return getText(register.hex)
    }

    override fun getText(hex: String): String {
        val asLong = java.lang.Long.decode(hex)
        return asLong.toString()
    }

    companion object {

        val instance = IntegerPresentation()
    }
}
