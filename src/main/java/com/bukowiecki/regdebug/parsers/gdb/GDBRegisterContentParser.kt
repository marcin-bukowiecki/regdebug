/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.gdb

/**
 * @author Marcin Bukowiecki
 */
object GDBGeneralPurposeRegisterContentParser {

    fun parseContent(content: String): List<String> = listOf(content.trim(), "")
}

/**
 * @author Marcin Bukowiecki
 */
object GDBFloatingPointRegisterContentParser {

    fun parseContent(content: String): String = content.trim()
}

/**
 * @author Marcin Bukowiecki
 */
object GDBExceptionStateRegisterContentParser {

    fun parseContent(content: String): List<String> = GDBGeneralPurposeRegisterContentParser.parseContent(content)
}
