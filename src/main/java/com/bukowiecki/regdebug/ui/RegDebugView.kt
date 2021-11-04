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
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.project.Project
import com.intellij.ui.PopupHandler
import com.intellij.ui.ScrollPaneFactory
import com.intellij.util.ui.components.BorderLayoutPanel
import org.jetbrains.annotations.NonNls
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

        if (!cellsSetup) {
            initialize()
            createCellPanel()
            cellsSetup = true
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
                        actionManager.createActionPopupMenu(getPopupPlace(), group).component.show(comp, x, y)
                    }
                }
            )
        }
    }

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
        myMainPanel.add(scrollPane, BorderLayout.CENTER)

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
        val table = RegDebugRegisterTable(RegDebugRegisterTableModel(this.registerCellContainers))
        this.cellsPanel.add(table, BorderLayout.WEST)
    }

    private fun getRegistersToView(): Set<String> {
        val text = getHeaderForm()?.getFilterTextField()?.text ?: return emptySet()
        if (text.isEmpty()) return emptySet()
        return text.split(',', ' ').map { it.trim() }.toSet()
    }
}
