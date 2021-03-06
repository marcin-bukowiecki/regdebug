/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.lldb

/**
 * @author Marcin Bukowiecki
 */
object LLDBGeneralPurposeRegisterContentParser {

    fun parseContent(content: String): List<String> {
        val contentTrimmed = content.trim()
        val spaceIndex = contentTrimmed.indexOfFirst { it.isWhitespace() }

        return if (spaceIndex == -1) {
            listOf(contentTrimmed)
        } else {
            val info = contentTrimmed.substring(spaceIndex).trim()
            val hex = contentTrimmed.substring(0, spaceIndex).trim()
            listOf(hex, info)
        }
    }
}

/**
 * @author Marcin Bukowiecki
 */
object LLDBFloatingPointRegisterContentParser {

    fun parseContent(content: String): String {
        return content.trim().let {
            if (it.startsWith("{")) {
                it.trim().drop(1).dropLast(1)
            } else {
                it
            }
        }
    }
}

/**
 * @author Marcin Bukowiecki
 */
object LLDBExceptionStateRegisterContentParser {

    fun parseContent(content: String): List<String> = LLDBGeneralPurposeRegisterContentParser.parseContent(content)
}
