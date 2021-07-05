/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
object FLAGSParser {

    private val labels = listOf(
        FLAGEntry("CF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("PF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("AF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("ZF", FLAGCategory.S),
        FLAGEntry("SF", FLAGCategory.S),
        FLAGEntry("TF", FLAGCategory.X),
        FLAGEntry("IF", FLAGCategory.X),
        FLAGEntry("DF", FLAGCategory.C),
        FLAGEntry("OF", FLAGCategory.S),
        FLAGEntry("IOPL", FLAGCategory.X),
        FLAGEntry("IOPL", FLAGCategory.X),
        FLAGEntry("NT", FLAGCategory.X),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("RF", FLAGCategory.X),
        FLAGEntry("VM", FLAGCategory.X),
        FLAGEntry("AC", FLAGCategory.X),
        FLAGEntry("VIF", FLAGCategory.X),
        FLAGEntry("VIP", FLAGCategory.X),
        FLAGEntry("ID", FLAGCategory.X),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
    )

    init {
        assert(labels.size == 32) { "Should be 32 was: ${labels.size}" }
    }

    fun parse(content: String): List<FLAGEntry> {
        val reserved = content.reversed()
        val result = mutableListOf<FLAGEntry>()

        labels.forEachIndexed { index, s ->
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

enum class FLAGCategory {
    S,
    X,
    C,
    Reserved;
}

data class FLAGEntry(val symbol: String, val category: FLAGCategory)