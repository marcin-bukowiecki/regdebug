/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.backend

import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.ui.RegDebugSessionTab
import com.jetbrains.cidr.execution.debugger.backend.lldb.auto_generated.Protocol

/**
 * @author Marcin Bukowiecki
 */
abstract class BaseDebugHandler(protected val sessionTab: RegDebugSessionTab) : DebugHandler {

    private var killed = false

    protected val timeout = RegDebugSettings.getInstance(sessionTab.project).registersLoadingTimeout
    protected val executionId: Long = sessionTab.executionId.incrementAndGet()
    protected var request: Protocol.CompositeRequest? = null

    protected fun handleError(e: Exception) {
        if (sessionTab.executionId.get() == executionId) {
            synchronized(sessionTab) {
                if (sessionTab.executionId.get() == executionId) {
                    kill()
                    sessionTab.views.forEach { it.addErrorMessages(e.message) }
                }
            }
        }
    }

    private fun kill() {
        killed = true
        request?.kill
    }
}
