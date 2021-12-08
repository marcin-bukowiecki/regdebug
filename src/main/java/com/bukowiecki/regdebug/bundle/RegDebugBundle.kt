/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.bundle

import com.intellij.DynamicBundle
import org.jetbrains.annotations.PropertyKey

const val BUNDLE = "messages.regDebug"

/**
 * @author Marcin Bukowiecki
 */
object RegDebugBundle : DynamicBundle(BUNDLE) {

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String): String {
        return getMessage(key)
    }

    fun message(@PropertyKey(resourceBundle = BUNDLE) key: String, arg1: Any): String {
        return getMessage(key, arg1)
    }
}
