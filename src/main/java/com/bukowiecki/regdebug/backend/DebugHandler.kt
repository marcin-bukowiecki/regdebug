/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.backend

/**
 * @author Marcin Bukowiecki
 */
interface DebugHandler {

  val backendType: BackendType

  fun handle()

  fun handleSetCommand(register: String, operator: String, value: String): String?
}
