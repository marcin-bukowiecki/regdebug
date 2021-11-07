/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.utils

import com.bukowiecki.regdebug.backend.DebugHandler
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.bukowiecki.regdebug.ui.RegisterCellContainer
import com.intellij.openapi.actionSystem.DataKey
import com.intellij.openapi.util.Key

/**
 * @author Marcin Bukowiecki
 */
object DataKeys {

    val registerCellContainer = DataKey.create<RegisterCellContainer>("RegDebug.registerCellContainer")
    val sessionTab = Key.create<RegDebugSessionTab>("RegDebug.sessionTab")
    val debugHandler = Key.create<DebugHandler>("RegDebug.debugHandler")
}
