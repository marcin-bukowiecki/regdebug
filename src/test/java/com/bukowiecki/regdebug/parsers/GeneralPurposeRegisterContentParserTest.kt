/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class GeneralPurposeRegisterContentParserTest {

    @Test
    fun testParse_1() {
        val result = GeneralPurposeRegisterContentParser.parseContent("0x0000000000000000")
        assertEquals(listOf("0x0000000000000000"), result)
    }

    @Test
    fun testParse_2() {
        val result = GeneralPurposeRegisterContentParser.parseContent("0x0000000000000000  foo bar ")
        assertEquals(listOf("0x0000000000000000", "foo bar"), result)
    }

    @Test
    fun testParse_3() {
        val result = GeneralPurposeRegisterContentParser.parseContent(" 0x00007ff9efd14080  std::basic_ostream<char,std::char_traits<char> > std::cout ")
        assertEquals(listOf("0x00007ff9efd14080", "std::basic_ostream<char,std::char_traits<char> > std::cout"), result)
    }
}