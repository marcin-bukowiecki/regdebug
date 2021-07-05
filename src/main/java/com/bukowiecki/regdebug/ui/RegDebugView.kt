/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.actions.RegisterCopyProvider
import com.bukowiecki.regdebug.parsers.ParseResult
import com.bukowiecki.regdebug.parsers.Register
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
abstract class RegDebugView(private val project: Project) {

    protected val myMainPanel: JPanel = BorderLayoutPanel()

    private lateinit var cellsPanel: JPanel
    private lateinit var registerCellContainers: List<RegisterCellContainer>

    protected var cellsSetup = false

    abstract fun rebuildView(parseResult: ParseResult)

    fun setupCells(registers: List<Register>) {
        if (cellsSetup) return

        val registerCellContainers = mutableListOf<RegisterCellContainer>()
        for (register in registers) {
            registerCellContainers.add(RegisterCellContainer())
        }
        this.registerCellContainers = registerCellContainers
        this.cellsSetup = true
    }

    fun createCells(registers: List<Register>) {
        for ((i, cellContainer) in registerCellContainers.withIndex()) {
            cellContainer.createCell(project, registers[i], getCellCreateProvider())
        }
        this.cellsPanel = JPanel(GridLayoutManager(registerCellContainers.size, 2))
    }

    fun buildCellsView() {
        var rowIdx = 0
        for ((i, rc) in registerCellContainers.withIndex()) {
            val gridConstraints = GridConstraints()
            gridConstraints.row = rowIdx
            gridConstraints.column = i % 2
            if (gridConstraints.column == 1) {
                rowIdx++
            }
            gridConstraints.anchor = GridConstraints.ANCHOR_WEST
            cellsPanel.add(rc.getMainPanel(), gridConstraints, -1)
        }

        val scrollPane = ScrollPaneFactory.createScrollPane(cellsPanel)
        myMainPanel.add(scrollPane, BorderLayout.CENTER)
   }

    open fun addActions() {
        val actionManager = ActionManager.getInstance()
        for (registerCellContainer in registerCellContainers) {
            DataManager.registerDataProvider(registerCellContainer.getHexTextField()) { dataId ->
                when {
                    PlatformDataKeys.COPY_PROVIDER.name == dataId -> {
                        RegisterCopyProvider(registerCellContainer.cell)
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

    abstract fun getActionGroupId(): String

    abstract fun getCellCreateProvider(): CellCreateProvider

    open fun addErrorMessages(message: String?) {

    }
}