/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
enum class RegisterType {
    GeneralPurpose,
    FloatingPoint
}

/**
 * @author Marcin Bukowiecki
 */
interface Register {
    val registerName: String
    val hex: String
    val info: String
    val registerType: RegisterType
}

/**
 * @author Marcin Bukowiecki
 */
data class GeneralPurposeRegister(override val registerName: String,
                                  override val hex: String,
                                  override val info: String = "") : Register {

    override val registerType: RegisterType
        get() = RegisterType.GeneralPurpose
}

/**
 * @author Marcin Bukowiecki
 */
data class FloatingPointRegister(override val registerName: String,
                                 override val hex: String,
                                 override val info: String = "") : Register {

    override val registerType: RegisterType
        get() = RegisterType.FloatingPoint
}

/**
 * @author Marcin Bukowiecki
 */
data class ExceptionStateRegister(override val registerName: String,
                                  override val hex: String,
                                  override val info: String = "") : Register {

    override val registerType: RegisterType
        get() = RegisterType.FloatingPoint
}
