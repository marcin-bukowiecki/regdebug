/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import javax.swing.table.AbstractTableModel

/**
 * @author Marcin Bukowiecki
 */
class RegDebugRegisterTableModel(private val registers: List<RegisterCellContainer>,
                                 private val columns: Int) : AbstractTableModel() {

  override fun getRowCount(): Int {
    return registers.size
  }

  override fun getColumnName(column: Int): String = ""

  override fun getColumnCount(): Int = columns

  override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean {
    return false
  }

  override fun getValueAt(row: Int, column: Int): Any {
    return when (column) {
      0 -> {
        registers[row]
      }
      1 -> {
        registers[row]
      }
      2 -> {
        registers[row].myCell.getInfoText()
      }
      else -> {
        ""
      }
    }
  }

  override fun getColumnClass(columnIndex: Int): Class<*> {
    if (columnIndex == 0 || columnIndex == 1) {
      return RegisterCellContainer::class.java
    }
    return String::class.java
  }
}
