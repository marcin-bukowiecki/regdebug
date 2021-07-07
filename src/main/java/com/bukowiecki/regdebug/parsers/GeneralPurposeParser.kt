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
        val data = GeneralPurposeRegisterContentParser.parseContent(split[1])
        val info = if (data.size == 2) {
            data[1]
        } else {
            ""
        }
        val hex = data[0]
        return GeneralPurposeRegister(register, hex, info)
    }
}

/**
 * @author Marcin Bukowiecki
 */
data class GeneralPurposeRegisters(override val registers: List<GeneralPurposeRegister>): RegistersHolder<GeneralPurposeRegister> {

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

    companion object {

        val empty = GeneralPurposeRegisters(emptyList())
    }
}
