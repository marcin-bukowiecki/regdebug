/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import javax.swing.table.AbstractTableModel

/**
 * @author Marcin Bukowiecki
 */
class RegDebugRegisterTableModel(private val registers: List<RegisterCellContainer>) : AbstractTableModel() {

  override fun getRowCount(): Int {
    return registers.size / 2
  }

  override fun getColumnName(column: Int): String = ""

  override fun getColumnCount(): Int = 4

  override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
    return false
  }

  override fun getValueAt(row: Int, column: Int): Any {
    when (column) {
      0 -> {
        return registers[row].myRegisterName
      }
      1 -> {
        return registers[row].getHexTextField()
      }
      2 -> {
        return registers[2 * row].myRegisterName
      }
      3 -> {
        return registers[2 * row].getHexTextField()
      }
      else -> {
        return ""
      }
    }
  }

  override fun getColumnClass(columnIndex: Int): Class<*> {
    return String::class.java
  }
}
