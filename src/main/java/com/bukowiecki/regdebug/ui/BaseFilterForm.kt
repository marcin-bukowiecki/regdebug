/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.RegistersHolder
import com.bukowiecki.regdebug.settings.RegDebugSettings
import com.bukowiecki.regdebug.settings.RegDebugSettings.Companion.getInstance
import com.bukowiecki.regdebug.ui.RegDebugSessionTab.Companion.topic
import javax.swing.JTextField
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener

/**
 * @author Marcin Bukowiecki
 */
abstract class BaseFilterForm<T : RegistersHolder<*>>(private val regDebugView: RegDebugView<T>) {

    val listener = regDebugView.project.messageBus.syncPublisher(topic)

    fun initListeners() {
        val settings = getInstance(regDebugView.project)
        getFilterTextField().text = getSettings(settings)

        getFilterTextField().document.addDocumentListener(object : DocumentListener {
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
                setSettings(settings)
                listener.rebuildView(getViewClass())
            }
        })
    }

    abstract fun getSettings(settings: RegDebugSettings): String

    abstract fun setSettings(settings: RegDebugSettings)

    abstract fun getFilterTextField(): JTextField

    abstract fun getViewClass(): Class<out RegDebugView<*>>
}