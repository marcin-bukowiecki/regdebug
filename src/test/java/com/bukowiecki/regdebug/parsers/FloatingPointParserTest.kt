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
class FloatingPointParserTest {

    @Test
    fun testParseLine1() {
        val result = FloatingPointParser.parseRegLine("xmm15 = {0xaf 0xca 0x6b 0x0c 0x3f 0x1d 0x25 0x2e 0xec 0x43 0xd4 0xd5 0xec 0x61 0x5c 0xc0}")
        Assert.assertEquals(FloatingPointRegister("xmm15", "0xaf 0xca 0x6b 0x0c 0x3f 0x1d 0x25 0x2e 0xec 0x43 0xd4 0xd5 0xec 0x61 0x5c 0xc0"), result)
    }
}