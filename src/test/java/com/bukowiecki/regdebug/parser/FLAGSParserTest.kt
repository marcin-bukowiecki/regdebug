/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parser

import com.bukowiecki.regdebug.parsers.FLAGSParser
import org.junit.Assert
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class FLAGSParserTest {

    @Test
    fun testParse_1() {
        val result = FLAGSParser.parse("0b0000000000000000000000000000000000000000000000000000001000000110")
        Assert.assertEquals(listOf("PF","IF"), result.map { it.symbol })
    }

    @Test
    fun testParse_2() {
        val result = FLAGSParser.parse("0b000000000000000000000000000000000000001000000110")
        Assert.assertEquals(listOf("PF","IF"), result.map { it.symbol })
    }

    @Test
    fun testParse_3() {
        val result = FLAGSParser.parse("0b0000001000000110")
        Assert.assertEquals(listOf("PF","IF"), result.map { it.symbol })
    }

    @Test
    fun testParse_4() {
        val result = FLAGSParser.parse("0x00000000000130a8")
        Assert.assertEquals(listOf("SF", "IOPL", "IOPL"), result.map { it.symbol })
    }

    @Test
    fun testParse_5() {
        val result = FLAGSParser.parse("0x0000000000000206")
        Assert.assertEquals(listOf("PF"), result.map { it.symbol })
    }
}