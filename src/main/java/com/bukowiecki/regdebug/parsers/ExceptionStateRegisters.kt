/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
data class ExceptionStateRegisters(override val registers: List<ExceptionStateRegister>): RegistersHolder<ExceptionStateRegister> {

    override fun findRegister(name: String): ExceptionStateRegister? {
        return registers.firstOrNull { it.registerName == name }
    }

    companion object {
        val empty = ExceptionStateRegisters(emptyList())
    }
}
