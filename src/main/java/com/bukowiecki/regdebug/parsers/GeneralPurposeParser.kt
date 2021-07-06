/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object GeneralPurposeParser {

    fun parseRegLine(regLine: String): GeneralPurposeRegister {
        val split = regLine.split("=")
        val register = split[0].trim()
        var hex = split[1].trim()
        val space = hex.indexOfFirst { it.isWhitespace() }
        var info = ""

        if (space >= 0) {
            info = hex.substring(space).trim()
            hex = hex.substring(0, space)
        }

        return GeneralPurposeRegister(register, hex, info)
    }
}

/**
 * @author Marcin Bukowiecki
 */
data class GeneralPurposeRegisters(val registers: List<GeneralPurposeRegister>): RegistersHolder<GeneralPurposeRegister> {

    override fun findRegister(name: String): GeneralPurposeRegister? {
        return registers.firstOrNull { it.registerName == name }
    }

    fun findFlagsRegister(): GeneralPurposeRegister? {
        var flags = findRegister("flags")
        if (flags != null) return flags

        flags = findRegister("eflags")
        if (flags != null) return flags

        flags = findRegister("rflags")
        if (flags != null) return flags

        return null
    }
}
