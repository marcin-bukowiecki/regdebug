/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.lldb

import com.bukowiecki.regdebug.parsers.GeneralPurposeRegister

/**
 * @author Marcin Bukowiecki
 */
object LLDBGeneralPurposeParser {

    fun parseRegLine(regLine: String): GeneralPurposeRegister {
        val split = regLine.split("=")
        val register = split[0].trim()
        val data = LLDBGeneralPurposeRegisterContentParser.parseContent(split[1])
        val info = if (data.size == 2) {
            data[1]
        } else {
            ""
        }
        val hex = data[0]
        return GeneralPurposeRegister(register, hex, info)
    }
}

