/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
interface RegistersHolder<T : Register> {

    val registers: List<T>

    fun findRegister(name: String): T?

    fun isEmpty(): Boolean {
        return registers.isEmpty()
    }
}