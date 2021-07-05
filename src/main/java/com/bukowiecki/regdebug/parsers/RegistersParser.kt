/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object RegistersParser {

    @Suppress("ControlFlowWithEmptyBody")
    fun parse(content: String): ParseResult {
        val lines = content.lines()
        val it = lines.iterator()

        var general: GeneralPurposeRegisters? = null
        var floating: FloatingPointRegisters? = null

        while (it.hasNext()) {
            val line = it.next()
            if (line.trim() == "General Purpose Registers:") {
                val registers = mutableListOf<GeneralPurposeRegister>()
                while (it.hasNext()) {
                    val next = it.next().trim()
                    if (next.isEmpty()) break
                    registers.add(GeneralPurposeParser.parseRegLine(next))
                }
                general = GeneralPurposeRegisters(registers)
            }
            if (line.trim() == "Floating Point Registers:") {
                val registers = mutableListOf<FloatingPointRegister>()
                while (it.hasNext()) {
                    val next = it.next().trim()
                    if (next.isEmpty()) break
                    registers.add(FloatingPointParser.parseRegLine(next))
                }
                floating = FloatingPointRegisters(registers)
            }
        }

        return ParseResult(general, floating)
    }
}

/**
 * @author Marcin Bukowiecki
 */
data class ParseResult(val generalPurpose: GeneralPurposeRegisters?, val floatingPoint: FloatingPointRegisters?)