/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */
package com.bukowiecki.regdebug.parsers

/**
 * @author Marcin Bukowiecki
 */
data class ParseResult(val generalPurpose: GeneralPurposeRegisters,
                       val floatingPoint: FloatingPointRegisters,
                       val exceptionState: ExceptionStateRegisters,
                       val other: OtherRegisters = OtherRegisters.empty)

