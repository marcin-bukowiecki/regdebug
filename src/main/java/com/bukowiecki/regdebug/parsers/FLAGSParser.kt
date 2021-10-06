/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object FLAGSParser {

    fun parse(content: String): List<FLAGEntry> {
        if (content.startsWith("0x")) {
            val l = java.lang.Long.decode(content)
            return parse(java.lang.Long.toBinaryString(l))
        }

        val reserved = content.reversed()
        val result = mutableListOf<FLAGEntry>()

        ParseUtils.flagsLabel.forEachIndexed { index, s ->
            if (index >= content.length - 2) { //ignore 0b prefix
                return@forEachIndexed
            }
            if (s.symbol.isNotEmpty()) {
                val c = reserved[index]
                if (c == '1') {
                    result.add(s)
                }
            }
        }

        return result
    }
}

/**
 * @author Marcin Bukowiecki
 */
enum class FLAGCategory {
    S,
    X,
    C,
    Reserved;
}

/**
 * @author Marcin Bukowiecki
 */
data class FLAGEntry(val symbol: String, val category: FLAGCategory)
