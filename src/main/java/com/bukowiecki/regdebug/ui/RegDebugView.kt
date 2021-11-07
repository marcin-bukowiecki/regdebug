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
import javax.swing.JScrollPane

/**
 * @author Marcin Bukowiecki
 */
abstract class RegDebugView<T : RegistersHolder<*>>(val project: Project) {

  protected val myMainPanel: JPanel = BorderLayoutPanel()

  private val myContentPanel: JPanel = JPanel()
  private val myScrollPane: JScrollPane = ScrollPaneFactory.createScrollPane(myContentPanel)
  private var initialized = false

  private var registerCellContainers = listOf<RegisterCellContainer>()

  init {
    myMainPanel.add(myScrollPane, BorderLayout.WEST)
  }

  open fun rebuildView(parseResult: ParseResult) {
    extractParseResult(parseResult)

    if (!initialized) {
      initialize()
      initialized = true
    }

    myContentPanel.removeAll()
    addCellsToPanel()
    addActions()
    updateCells(getRegistersHolder())

    afterRebuild()
  }

  open fun numberOfColumns() = 2

  abstract fun getActionGroupId(): String

  abstract fun getCellCreateProvider(): CellCreateProvider

  fun refreshView() {
    myContentPanel.removeAll()
    addCellsToPanel()
    myContentPanel.revalidate()
    myContentPanel.repaint()
    myMainPanel.revalidate()
    myMainPanel.repaint()

  }

  open fun addActions() {  }

  open fun getPopupPlace(): @NonNls String = getActionGroupId()

  fun getMainPanel(): JPanel {
    return myMainPanel
  }

  open fun numberOfTables() = 2

  open fun addErrorMessages(message: String?) {}

  open fun initialize() {
    this.registerCellContainers = getRegistersHolder().registers.map {
      val registerCellContainer = RegisterCellContainer(it.registerName)
      registerCellContainer.createCell(project, it, getCellCreateProvider())
      registerCellContainer
    }
  }

  open fun afterRebuild() {
    myContentPanel.revalidate()
  }

  abstract fun extractParseResult(parseResult: ParseResult)

  abstract fun getRegistersHolder(): T

  abstract fun getHeaderForm(): BaseFilterForm<T>?

  open fun tableModelProvider(registerCellContainers: List<RegisterCellContainer>): () -> RegDebugRegisterTableModel {
    return {
      RegDebugRegisterTableModel(registerCellContainers, numberOfColumns())
    }
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
    val numberOfColumns = numberOfTables()

    val filteredRegisters = this.registerCellContainers.filter {
      registersToView.isEmpty() || registersToView.contains(it.myRegisterName)
    }

    val subLists = mutableListOf<MutableList<RegisterCellContainer>>()
    var i = 0
    while (i < numberOfColumns) {
      subLists.add(mutableListOf())
      i++
    }

    filteredRegisters.forEachIndexed { index, registerCellContainer ->
      val slot = index % numberOfColumns
      subLists[slot].add(registerCellContainer)
    }

    subLists.forEach { l ->
      val table = RegDebugRegisterTable(this, tableModelProvider(l)())
      myContentPanel.add(table)
    }
  }

  private fun getRegistersToView(): Set<String> {
    val text = getHeaderForm()?.getFilterTextField()?.text ?: return emptySet()
    if (text.isEmpty()) return emptySet()
    return text.split(',', ' ').map { it.trim() }.toSet()
  }
}
