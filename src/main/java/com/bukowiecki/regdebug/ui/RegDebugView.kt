/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.actions.RegisterCopyProvider
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.RegistersHolder
import com.bukowiecki.regdebug.utils.DataKeys
import com.intellij.ide.DataManager
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.ui.PopupHandler
import com.intellij.ui.ScrollPaneFactory
import com.intellij.uiDesigner.core.GridConstraints
import com.intellij.uiDesigner.core.GridLayoutManager
import com.intellij.util.ui.components.BorderLayoutPanel
import java.awt.BorderLayout
import java.awt.Component
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

        if (getRegistersHolder().isEmpty()) return

        if (!cellsSetup) {
            initialize()
            setupCellContainers(getRegistersHolder())
            createCellPanel()
        } else {
            cellsPanel.removeAll()
            addCellsToPanel()
            updateCells(getRegistersHolder())
        }

        afterRebuild()
    }

    abstract fun getActionGroupId(): String

    abstract fun getCellCreateProvider(): CellCreateProvider

    fun refreshView() {
        cellsPanel.removeAll()
        addCellsToPanel()
        cellsPanel.revalidate()
    }

    open fun addActions() {
        val actionManager = ActionManager.getInstance()
        for (registerCellContainer in registerCellContainers) {
            DataManager.registerDataProvider(registerCellContainer.getHexTextField()) { dataId ->
                when {
                    PlatformDataKeys.COPY_PROVIDER.name == dataId -> {
                        RegisterCopyProvider(registerCellContainer.myCell)
                    }
                    DataKeys.registerCellContainer.name == dataId -> {
                        registerCellContainer
                    }
                    else -> {
                        null
                    }
                }
            }

            registerCellContainer.getHexTextField().addMouseListener(
                object : PopupHandler() {
                    override fun invokePopup(comp: Component, x: Int, y: Int) {
                        val group = actionManager.getAction(getActionGroupId()) as? ActionGroup ?: return
                        actionManager.createActionPopupMenu(ActionPlaces.UNKNOWN, group).component.show(comp, x, y)
                    }
                }
            )
        }
    }

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

    private fun updateCells(registerHolder: RegistersHolder<*>) {
        for (registerCellContainer in registerCellContainers) {
            registerCellContainer.updateCell(
                registerHolder.findRegister(registerCellContainer.myRegisterName) ?: continue
            )
        }
    }

    private fun setupCellContainers(registersHolder: RegistersHolder<*>) {
        if (cellsSetup) return

        val registers = registersHolder.registers
        val registerCellContainers = mutableListOf<RegisterCellContainer>()
        for (register in registers) {
            registerCellContainers.add(RegisterCellContainer(register.registerName))
        }
        this.registerCellContainers = registerCellContainers

        for ((i, cellContainer) in registerCellContainers.withIndex()) {
            cellContainer.createCell(project, registers[i], getCellCreateProvider())
        }

        this.cellsSetup = true
    }

    private fun createCellPanel() {
        this.cellsPanel = JPanel(GridLayoutManager(registerCellContainers.size, 2))
        val scrollPane = ScrollPaneFactory.createScrollPane(cellsPanel)
        myMainPanel.add(scrollPane, BorderLayout.CENTER)

        addCellsToPanel()
        addActions()
    }

    private fun addCellsToPanel() {
        val registersToView = getRegistersToView()
        var rowIdx = 0

        var i = 0
        for (rc in registerCellContainers) {
            if (registersToView.isEmpty() || registersToView.contains(rc.myRegisterName)) {
                val gridConstraints = GridConstraints()
                gridConstraints.row = rowIdx
                gridConstraints.column = i % 2
                if (gridConstraints.column == 1) {
                    rowIdx++
                }
                gridConstraints.anchor = GridConstraints.ANCHOR_WEST
                cellsPanel.add(rc.getMainPanel(), gridConstraints, -1)
                i++
            }
        }
    }

    private fun getRegistersToView(): Set<String> {
        val text = getHeaderForm()?.getFilterTextField()?.text ?: return emptySet()
        if (text.isEmpty()) return emptySet()

        return text.split(',', ' ').map { it.trim() }.toSet()
    }
}