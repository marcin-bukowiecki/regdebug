/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object ExceptionStateParser {

    fun parseRegLine(regLine: String): ExceptionStateRegister {
        val split = regLine.split("=")
        val register = split[0].trim()
        val data = ExceptionStateRegisterContentParser.parseContent(split[1])
        val info = if (data.size == 2) {
            data[1]
        } else {
            ""
        }
        val hex = data[0]
        return ExceptionStateRegister(register, hex, info)
    }
}

/**
 * @author Marcin Bukowiecki
 */
data class ExceptionStateRegisters(override val registers: List<ExceptionStateRegister>): RegistersHolder<ExceptionStateRegister> {

    override fun findRegister(name: String): ExceptionStateRegister? {
        return registers.firstOrNull { it.registerName == name }
    }

    companion object {

        val empty = ExceptionStateRegisters(emptyList())
    }
}
