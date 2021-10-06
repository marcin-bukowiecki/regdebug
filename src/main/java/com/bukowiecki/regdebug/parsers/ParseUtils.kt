/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.parsers

import com.bukowiecki.regdebug.backend.BackendType

/**
 * @author Marcin Bukowiecki
 */
object ParseUtils {

    val registerLineSeparators = mapOf(
        BackendType.lldb to '=',
        BackendType.gdb to ' '
    )

    val flagsLabel = listOf(
        FLAGEntry("CF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("PF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("AF", FLAGCategory.S),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("ZF", FLAGCategory.S),
        FLAGEntry("SF", FLAGCategory.S),
        FLAGEntry("TF", FLAGCategory.X),
        FLAGEntry("IF", FLAGCategory.X),
        FLAGEntry("DF", FLAGCategory.C),
        FLAGEntry("OF", FLAGCategory.S),
        FLAGEntry("IOPL", FLAGCategory.X),
        FLAGEntry("IOPL", FLAGCategory.X),
        FLAGEntry("NT", FLAGCategory.X),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("RF", FLAGCategory.X),
        FLAGEntry("VM", FLAGCategory.X),
        FLAGEntry("AC", FLAGCategory.X),
        FLAGEntry("VIF", FLAGCategory.X),
        FLAGEntry("VIP", FLAGCategory.X),
        FLAGEntry("ID", FLAGCategory.X),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
        FLAGEntry("", FLAGCategory.Reserved),
    )

    init {
        assert(flagsLabel.size == 32) { "Should be 32 flags was: ${flagsLabel.size}" }
    }
}
