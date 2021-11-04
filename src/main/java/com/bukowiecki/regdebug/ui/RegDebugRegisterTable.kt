/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.intellij.ui.table.JBTable

/**
 * @author Marcin Bukowiecki
 */
class RegDebugRegisterTable(model: RegDebugRegisterTableModel) : JBTable(model) {

  init {
    setAutoResizeMode(AUTO_RESIZE_ALL_COLUMNS)
    setupSizes()
  }

  private fun setupSizes() {
    setRowMargin(5)
    setRowHeight(50)

    for (column in getColumnModel().columns) {
      column.resizable = true
      column.width = 300
    }
  }
}
