/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
class FloatingPointRegisters(override val registers: List<FloatingPointRegister>): RegistersHolder<FloatingPointRegister> {

    override fun findRegister(name: String): FloatingPointRegister? {
        return registers.firstOrNull { it.registerName == name }
    }

    companion object {
        val empty = FloatingPointRegisters(emptyList())
    }
}
