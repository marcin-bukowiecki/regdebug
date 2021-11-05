/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.actions.RegisterCopyProvider
import com.bukowiecki.regdebug.parsers.RegistersHolder
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.util.Ref
import com.intellij.ui.table.JBTable
import java.awt.Component
import java.awt.event.MouseAdapter
import java.awt.event.MouseEvent
import javax.swing.JTable
import javax.swing.SwingUtilities
import javax.swing.table.TableCellRenderer

/**
 * @author Marcin Bukowiecki
 */
class RegDebugRegisterTable<T : RegistersHolder<*>>(
  private val view: RegDebugView<T>,
  model: RegDebugRegisterTableModel
) : JBTable(model) {

  private val myLock = Object()
  private val currentCellRef: Ref<RegisterCellContainer> = Ref()

  init {
    setupSizes()

    addMouseListener(object : MouseAdapter() {

      override fun mouseClicked(e: MouseEvent) {
        if (SwingUtilities.isRightMouseButton(e)) {
          val source = e.source as? JTable ?: return
          val row = source.rowAtPoint(e.point)
          val column = source.columnAtPoint(e.point)
          val value = source.model.getValueAt(row, column)
          if (value is RegisterCellContainer) {
            synchronized(myLock) {
              this@RegDebugRegisterTable.currentCellRef.set(value)
              val actionManager = ActionManager.getInstance()
              val group = actionManager.getAction(view.getActionGroupId()) as? ActionGroup ?: return
              actionManager.createActionPopupMenu(view.getPopupPlace(), group).component.show(
                source,
                e.x,
                e.y
              )
            }
          }
        }
      }
    })

    DataManager.registerDataProvider(this) { dataId ->
      val c = currentCellRef.get() ?: return@registerDataProvider null
      val myCell = c.myCell

      when {
        PlatformDataKeys.COPY_PROVIDER.name == dataId -> {
          RegisterCopyProvider(myCell)
        }
        DataKeys.registerCellContainer.name == dataId -> {
          c
        }
        else -> {
          null
        }
      }
    }
  }

  private fun setupSizes() {
    setRowMargin(5)
    setRowHeight(40)

    val columnModel = getColumnModel()
    val columnCount = columnModel.columnCount

    if (columnCount > 0) {
      val column = getColumnModel().getColumn(0)
      column.cellRenderer = object : TableCellRenderer {

        override fun getTableCellRendererComponent(
          table: JTable?,
          value: Any?,
          isSelected: Boolean,
          hasFocus: Boolean,
          row: Int,
          column: Int
        ): Component {
          return (value as RegisterCellContainer).myCell.registerLabel
        }
      }
    }

    if (columnCount > 1) {
      val column = getColumnModel().getColumn(1)
      column.minWidth = 300
      column.cellRenderer = object : TableCellRenderer {

        override fun getTableCellRendererComponent(
          table: JTable?,
          value: Any?,
          isSelected: Boolean,
          hasFocus: Boolean,
          row: Int,
          column: Int
        ): Component {
          return (value as RegisterCellContainer).getHexTextField()
        }
      }
    }
  }
}
