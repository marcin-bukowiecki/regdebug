/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object FloatingPointParser {

    fun parseRegLine(regLine: String): FloatingPointRegister {
        val split = regLine.split("\\s+".toRegex())
        val register = split[0].trim()
        val exp = split[2].trim().drop(1) // = ignored
        val mantissa = split[3].trim().dropLast(1)
        val info = if (split.size < 5) {
            ""
        } else {
            split[4].trim()
        }
        return FloatingPointRegister(register, exp, mantissa, info)
    }
}

/**
 * @author Marcin Bukowiecki
 */
data class FloatingPointRegisters(val registers: List<FloatingPointRegister>): RegistersHolder<FloatingPointRegister> {

    override fun findRegister(name: String): FloatingPointRegister? {
        return registers.firstOrNull { it.registerName == name }
    }
}
