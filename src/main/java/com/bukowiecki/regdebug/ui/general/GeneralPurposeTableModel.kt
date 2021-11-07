/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.general

import com.bukowiecki.regdebug.ui.RegDebugRegisterTableModel
import com.bukowiecki.regdebug.ui.RegisterCellContainer

/**
 * @author Marcin Bukowiecki
 */
class GeneralPurposeTableModel(registers: List<RegisterCellContainer>,
                               numberOfColumns: Int) : RegDebugRegisterTableModel(registers, numberOfColumns) {

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
}
