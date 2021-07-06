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
        val split = regLine.split("=")
        val register = split[0].trim()
        val hex = split[1].trim().let { // = ignored
            if (it.startsWith("{")) {
                it.trim().drop(1).dropLast(1)
            } else {
                it
            }
        }

        return FloatingPointRegister(register, hex)
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
