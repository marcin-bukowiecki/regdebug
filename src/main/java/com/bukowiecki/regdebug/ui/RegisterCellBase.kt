/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.Register

/**
 * @author Marcin Bukowiecki
 */
abstract class RegisterCellBase(override val register: Register) : RegisterCell