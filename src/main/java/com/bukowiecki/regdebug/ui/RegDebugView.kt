/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.RegistersHolder
import com.intellij.openapi.project.Project
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import org.jetbrains.annotations.NonNls
import java.awt.BorderLayout
import javax.swing.JPanel

/**
 * @author Marcin Bukowiecki
 */
abstract class RegDebugView<T : RegistersHolder<*>>(val project: Project) {

  protected val myMainPanel: JPanel = BorderLayoutPanel()
  protected lateinit var cellsPanel: JPanel

  private lateinit var registerCellContainers: List<RegisterCellContainer>
  private var cellsSetup = false

  open fun rebuildView(parseResult: ParseResult) {
    extractParseResult(parseResult)

    if (!cellsSetup) {
      initialize()
      createCellPanel()
      cellsSetup = true
    } else {
      cellsPanel.removeAll()
      updateCells(getRegistersHolder())
      addCellsToPanel()
    }

    afterRebuild()
  }

  open fun numberOfColumns() = 2

  abstract fun getActionGroupId(): String

  abstract fun getCellCreateProvider(): CellCreateProvider

  fun refreshView() {
    cellsPanel.removeAll()
    addCellsToPanel()
    cellsPanel.revalidate()
  }

  open fun addActions() {  }

  open fun getPopupPlace(): @NonNls String = getActionGroupId()

  fun getMainPanel(): JPanel {
    return myMainPanel
  }

  open fun addErrorMessages(message: String?) {}

  open fun initialize() {}

  open fun afterRebuild() {
    cellsPanel.revalidate()
  }

  abstract fun extractParseResult(parseResult: ParseResult)

  abstract fun getRegistersHolder(): T

  abstract fun getHeaderForm(): BaseFilterForm<T>?

  private fun createCellPanel() {
    this.cellsPanel = JPanel()
    val scrollPane = ScrollPaneFactory.createScrollPane(cellsPanel)
    myMainPanel.add(scrollPane, BorderLayout.WEST)

    addCellsToPanel()
    addActions()
  }

  private fun updateCells(registerHolder: RegistersHolder<*>) {
    for (registerCellContainer in registerCellContainers) {
      registerCellContainer.updateCell(
        registerHolder.findRegister(registerCellContainer.myRegisterName) ?: continue
      )
    }
  }

  private fun addCellsToPanel() {
    val registersToView = getRegistersToView()
    this.registerCellContainers = getRegistersHolder().registers.filter {
      registersToView.isEmpty() || registersToView.contains(it.registerName)
    }.map {
      val registerCellContainer = RegisterCellContainer(it.registerName)
      registerCellContainer.createCell(project, it, getCellCreateProvider())
      registerCellContainer
    }
    val table = RegDebugRegisterTable(this, RegDebugRegisterTableModel(this.registerCellContainers, numberOfColumns()))
    this.cellsPanel.add(table, BorderLayout.WEST)
  }

  private fun getRegistersToView(): Set<String> {
    val text = getHeaderForm()?.getFilterTextField()?.text ?: return emptySet()
    if (text.isEmpty()) return emptySet()
    return text.split(',', ' ').map { it.trim() }.toSet()
  }
}
