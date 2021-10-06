/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers.gdb

import com.bukowiecki.regdebug.parsers.*
import org.apache.commons.lang.StringUtils

/**
 * @author Marcin Bukowiecki
 */
object GDBRegistersParser {

    fun parseGroups(content: String?): List<String> {
        if (content.isNullOrEmpty()) return emptyList()
        return content.lines().mapNotNull { l ->
            val s = StringUtils.split(l)
            if (s.isNullOrEmpty()) {
                null
            } else {
                s[0].trim()
            }
        }.drop(1)
    }

    fun parseGeneralPurposeRegisters(content: String?): GeneralPurposeRegisters {
        if (content.isNullOrEmpty()) return GeneralPurposeRegisters.empty
        return GeneralPurposeRegisters(content.lines().mapNotNull { l ->
            val s = StringUtils.split(l)
            if (s.isNullOrEmpty() || s.size < 2) {
                null
            } else {
                GeneralPurposeRegister(s[0].trim(), s[1].trim())
            }
        })
    }

    fun parseFloatingPointRegisters(content: String?): FloatingPointRegisters {
        if (content.isNullOrEmpty()) return FloatingPointRegisters.empty
        return FloatingPointRegisters(content.lines().mapNotNull { l ->
            val s = StringUtils.split(l)
            if (s.isNullOrEmpty() || s.size < 2) {
                null
            } else {
                FloatingPointRegister(s[0].trim(), s[1].trim())
            }
        })
    }

    fun parseOther(content: String?): OtherRegisters {
        if (content.isNullOrEmpty()) return OtherRegisters.empty
        return OtherRegisters(content.lines().mapNotNull { l ->
            if (l.startsWith("zmm") || l.startsWith("xmm")) {
                val indexOf = l.indexOf('{')
                val lastIndexOf = l.lastIndexOf('}')
                OtherRegister(StringUtils.split(l)[0], l.substring(indexOf + 1, lastIndexOf))
            } else {
                val s = StringUtils.split(l)
                if (s.size >= 2) {
                    OtherRegister(s[0].trim(), s[1].trim())
                } else {
                    null
                }
            }
        })
    }
}
