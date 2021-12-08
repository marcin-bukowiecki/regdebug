/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.RegistersHolder
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.intellij.openapi.project.Project
import com.intellij.ui.ScrollPaneFactory
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * @author Marcin Bukowiecki
 */
abstract class RegDebugView<T : RegistersHolder<*>>(val project: Project) {

  protected val listener = project.messageBus.syncPublisher(RegDebugSessionTab.topic)
  protected val myMainPanel: JPanel = JPanel(GridLayoutManager(2, 1))
  protected val filterTextField = JTextField()
  protected val registerGroupsTextField = JTextField()
  protected val statusLabel = JLabel()
  protected val toolboxPanel = BorderLayoutPanel()

  private val myHeaderPanel = JPanel()
  private val myContentPanel: JPanel = JPanel()
  private val myScrollPane: JScrollPane = ScrollPaneFactory.createScrollPane(myContentPanel)
  private var initialized = false

  private var registerCellContainers = listOf<RegisterCellContainer>()

  init {
    val filterPanel = BorderLayoutPanel()
    filterPanel.add(JLabel(RegDebugBundle.message("regdebug.tab.filter")), BorderLayout.WEST)
    filterTextField.columns = 30
    filterPanel.add(filterTextField, BorderLayout.CENTER)
    filterPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 15)
    toolboxPanel.add(filterPanel, BorderLayout.WEST)

    val groupsPanel = BorderLayoutPanel()
    groupsPanel.add(JLabel(RegDebugBundle.message("regdebug.tab.groups")), BorderLayout.WEST)
    registerGroupsTextField.size = Dimension(300, -1)
    groupsPanel.add(registerGroupsTextField, BorderLayout.CENTER)
    groupsPanel.border = BorderFactory.createEmptyBorder(0, 0, 0, 15)
    toolboxPanel.add(groupsPanel, BorderLayout.CENTER)

    myHeaderPanel.add(toolboxPanel)

    val messagePanel = BorderLayoutPanel()
    messagePanel.add(statusLabel, BorderLayout.WEST)
    myHeaderPanel.add(messagePanel)

    myMainPanel.border = BorderFactory.createEmptyBorder(15, 15, 15, 15)
    myHeaderPanel.border = BorderFactory.createEmptyBorder(0, 0, 5, 0)

    var gridConstraints = GridConstraints()
    gridConstraints.row = 0
    gridConstraints.column = 0
    gridConstraints.anchor = GridConstraints.ANCHOR_NORTHWEST
    myMainPanel.add(myHeaderPanel, gridConstraints, -1)

    gridConstraints = GridConstraints()
    gridConstraints.row = 1
    gridConstraints.column = 0
    gridConstraints.anchor = GridConstraints.ANCHOR_NORTHWEST
    myMainPanel.add(myScrollPane, gridConstraints, -1)
  }

  fun refreshView() {
    myContentPanel.removeAll()
    addCellsToPanel()
    myScrollPane.revalidate()
    myScrollPane.repaint()
    myContentPanel.revalidate()
    myContentPanel.repaint()
    myMainPanel.revalidate()
    myMainPanel.repaint()

  }

  fun getMainPanel(): JPanel {
    return myMainPanel
  }

  abstract fun getActionGroupId(): String

  abstract fun getCellCreateProvider(): CellCreateProvider

  abstract fun extractParseResult(parseResult: ParseResult)

  abstract fun getRegistersHolder(): T

  abstract fun loadSettings(settings: RegDebugSettings)

  abstract fun updateSettings(settings: RegDebugSettings)

  abstract fun getViewClass(): Class<*>

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

  open fun addActions() {  }

  open fun getPopupPlace(): String = getActionGroupId()

  open fun numberOfTables() = 2

  open fun addErrorMessages(message: String?) {}

  open fun afterRebuild() {
    myContentPanel.revalidate()
  }

  open fun initListeners() {
    val settings = RegDebugSettings.getInstance(project)
    loadSettings(settings)

    val listener = object : DocumentListener {
      override fun insertUpdate(e: DocumentEvent) {
        rebuild()
      }

      override fun removeUpdate(e: DocumentEvent) {
        rebuild()
      }

      override fun changedUpdate(e: DocumentEvent) {
        rebuild()
      }

      private fun rebuild() {
        updateSettings(settings)
        listener.rebuildView(getViewClass())
      }
    }

    filterTextField.document.addDocumentListener(listener)
    registerGroupsTextField.document?.addDocumentListener(listener)
  }

  open fun initialize() {
    initListeners()
    this.registerCellContainers = getRegistersHolder().registers.map {
      val registerCellContainer = RegisterCellContainer(it.registerName)
      registerCellContainer.createCell(project, it, getCellCreateProvider())
      registerCellContainer
    }
  }

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
    val text = filterTextField.text ?: return emptySet()
    if (text.isEmpty()) return emptySet()
    return text.split(',', ' ').map { it.trim() }.toSet()
  }
}
