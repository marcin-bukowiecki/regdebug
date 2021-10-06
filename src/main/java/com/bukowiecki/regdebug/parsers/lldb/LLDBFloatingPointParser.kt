/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.lldb

import com.bukowiecki.regdebug.backend.BackendType
import com.bukowiecki.regdebug.parsers.FloatingPointRegister
import com.bukowiecki.regdebug.parsers.ParseUtils

/**
 * @author Marcin Bukowiecki
 */
object LLDBFloatingPointParser {

    fun parseRegLine(regLine: String): FloatingPointRegister {
        val split = regLine.split(ParseUtils.registerLineSeparators[BackendType.LLDB]!!)
        val register = split[0].trim()
        val hex = LLDBFloatingPointRegisterContentParser.parseContent(split[1])
        return FloatingPointRegister(register, hex)
    }
}
