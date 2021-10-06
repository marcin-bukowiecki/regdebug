/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
data class OtherRegisters(override val registers: List<OtherRegister>): RegistersHolder<OtherRegister> {

    override fun findRegister(name: String): OtherRegister? {
        return registers.firstOrNull { it.registerName == name }
    }

    companion object {
        val empty = OtherRegisters(emptyList())
    }
}
