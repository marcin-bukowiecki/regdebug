/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

import org.junit.Assert
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class GeneralPurposeParserTest {

    @Test
    fun testParseLine1() {
        val result = GeneralPurposeParser.parseRegLine("gs = 0x0000000000000000")
        Assert.assertEquals(GeneralPurposeRegister("gs", "0x0000000000000000"), result)
    }

    @Test
    fun testParseLine2() {
        val result = GeneralPurposeParser.parseRegLine("gs = 0x0000000000000000 foo bar")
        Assert.assertEquals(GeneralPurposeRegister("gs", "0x0000000000000000", "foo bar"), result)
    }
}