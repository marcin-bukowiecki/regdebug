/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.bundle.RegDebugBundle
import com.bukowiecki.regdebug.lldb.LLDBDebugHandler
import com.intellij.execution.ui.layout.LayoutAttractionPolicy
import com.intellij.execution.ui.layout.LayoutViewOptions
import com.intellij.execution.ui.layout.PlaceInGrid
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorImpl
import com.intellij.openapi.util.Disposer
import com.intellij.ui.content.Content
import com.intellij.util.SlowOperations
import com.jetbrains.cidr.execution.debugger.CidrDebugProcess
import com.jetbrains.cidr.execution.debugger.backend.lldb.LLDBDriver
import java.util.concurrent.atomic.AtomicLong

const val tabId = 2000
const val GeneralPurposeRegisterContentId = "GeneralPurposeRegisterContentId"

/**
 * @author Marcin Bukowiecki
 */
class RegDebugSessionTab(private val debugProcess: CidrDebugProcess) : Disposable {

    private val log = Logger.getInstance(RegDebugSessionTab::class.java)

    private lateinit var generalPurposeView: GeneralPurposeView
    private lateinit var floatingPointView: FloatingPointView

    val views = mutableListOf<RegDebugView>()
    val memoryEditorViews = mutableListOf<PsiAwareTextEditorImpl>()
    private val session = debugProcess.session

    val executionId = AtomicLong()

    init {
        val ui = session.ui
        ui.defaults.initTabDefaults(tabId, RegDebugBundle.message("regdebug.tab.name"), null)

        ui.defaults
            .initContentAttraction(GeneralPurposeRegisterContentId, LayoutViewOptions.STARTUP, LayoutAttractionPolicy.FocusOnce(false))
    }

    fun rebuildViews() {
        ApplicationManager.getApplication().invokeLater {
            when (val driverInTests = debugProcess.driverInTests) {
                is LLDBDriver -> {
                    LLDBDebugHandler(this, driverInTests).handle()
                }
                else -> {
                    if (driverInTests == null || driverInTests.javaClass.canonicalName == null) {
                        log.info("Debug process driver is null")
                    } else {
                        log.info("Debug process driver: ${driverInTests.javaClass.canonicalName} is not supported")
                    }
                }
            }
        }
    }

    fun registerGeneralPurposeView(view: GeneralPurposeView) {
        val ui = session.ui
        generalPurposeView = view
        val content: Content = ui.createContent(
            GeneralPurposeRegisterContentId,
            view.getMainPanel(),
            RegDebugBundle.message("regdebug.general.purpose.registers"), null, null
        )
        content.isCloseable = false
        ui.addContent(content, tabId, PlaceInGrid.left, false)
        Disposer.register(session.runContentDescriptor, view)
        views.add(view)
    }

    fun registerFloatingPointView(view: FloatingPointView) {
        val ui = session.ui
        floatingPointView = view
        val content: Content = ui.createContent(
            GeneralPurposeRegisterContentId,
            view.getMainPanel(),
            RegDebugBundle.message("regdebug.floating.point.registers"), null, null
        )
        content.isCloseable = false
        ui.addContent(content, tabId, PlaceInGrid.left, false)
        Disposer.register(session.runContentDescriptor, view)
        views.add(view)
    }

    fun beforeSessionResumed() {
        val editorManager = FileEditorManagerEx.getInstance(debugProcess.project) as FileEditorManagerEx
        val windows = editorManager.windows
        for (window in windows) {
            val selectedEditor = window.selectedEditor ?: continue
            for (editor in selectedEditor.editors) {
                if (editor in memoryEditorViews) {
                    val selectedFile = window.selectedFile

                    SlowOperations.allowSlowOperations<RuntimeException> {
                        editorManager.closeFile(selectedFile, window)
                    }
                }
            }
        }

        synchronized(this) {
            memoryEditorViews.forEach {
                it.dispose()
            }
            memoryEditorViews.clear()
        }
    }

    override fun dispose() {
        generalPurposeView.dispose()
        floatingPointView.dispose()
        Disposer.dispose(this)
    }
}