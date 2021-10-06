/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.lldb

import com.bukowiecki.regdebug.parsers.ExceptionStateRegister

/**
 * @author Marcin Bukowiecki
 */
object ExceptionStateParser {

    fun parseRegLine(regLine: String): ExceptionStateRegister {
        val split = regLine.split("=")
        val register = split[0].trim()
        val data = LLDBExceptionStateRegisterContentParser.parseContent(split[1])
        val info = if (data.size == 2) {
            data[1]
        } else {
            ""
        }
        val hex = data[0]
        return ExceptionStateRegister(register, hex, info)
    }
}
