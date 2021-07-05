/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parser

import com.bukowiecki.regdebug.parsers.GeneralPurposeRegister
import com.bukowiecki.regdebug.parsers.RegistersParser
import org.junit.Assert
import org.junit.Test

/**
 * @author Marcin Bukowiecki
 */
class RegistersParserTest {

    @Test
    fun testParseSuccess1() {
        val content = """
            General Purpose Registers:
                   rax = 0x00007ff9efd14080  std::basic_ostream<char,std::char_traits<char> > std::cout
                   rbx = 0x0000000000000000
                   rcx = 0x0000006a9d2ffb04
                   rdx = 0x00007ff9ea2e3c78  
                   rdi = 0x0000006a9d2ffb20
                   rsi = 0x0000000000000000
                   rbp = 0x0000000000000000
                   rsp = 0x0000006a9d2ffad0
                    r8 = 0x0000000000000003
                    r9 = 0x0000006a9d2ff8d8
                   r10 = 0x0000000000000014
                   r11 = 0x0000000000000246
                   r12 = 0x0000000000000000
                   r13 = 0x0000000000000000
                   r14 = 0x0000000000000000
                   r15 = 0x0000000000000000
                   rip = 0x00007ff7f2101526  untitled1.exe`test(int *) + 6 at main.cpp:4
                eflags = 0b0000000000000000000000000000000000000000000000000000001000000110

            Floating Point Registers:
                  xmm0 = {0x0000000000000000 0x0000000000000000}
                  xmm1 = {0x0000000000000000 0x0000000000000000}
                  xmm2 = {0x0000000000000000 0x0000000000000000}
                  xmm3 = {0x0000000000000000 0x0000000000000000}
                  xmm4 = {0x0000000000000000 0x0000000000000000}
                  xmm5 = {0x0000000000000000 0x0000000000000000}
                  xmm6 = {0x0000000000000000 0x0000000000000000}
                  xmm7 = {0x0000000000000000 0x0000000000000000}
                  xmm8 = {0x0000000000000000 0x0000000000000000}
                  xmm9 = {0x0000000000000000 0x0000000000000000}
                 xmm10 = {0x0000000000000000 0x0000000000000000}
                 xmm11 = {0x0000000000000000 0x0000000000000000}
                 xmm12 = {0x0000000000000000 0x0000000000000000}
                 xmm13 = {0x0000000000000000 0x0000000000000000}
                 xmm14 = {0x0000000000000000 0x0000000000000000}
                 xmm15 = {0x0000000000000000 0x0000000000000000}


        """.trimIndent()

        val result = RegistersParser.parse(content)
        Assert.assertNotNull(result)
        Assert.assertEquals(GeneralPurposeRegister(
            "eflags",
            "0b0000000000000000000000000000000000000000000000000000001000000110",
            ""), result.generalPurpose?.registers?.last())
    }
}