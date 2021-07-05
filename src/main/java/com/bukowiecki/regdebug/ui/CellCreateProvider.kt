/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui

import com.bukowiecki.regdebug.parsers.Register
import com.bukowiecki.regdebug.presentation.RegisterPresentation
import com.intellij.openapi.project.Project

/**
 * @author Marcin Bukowiecki
 */
abstract class CellCreateProvider {

    abstract fun create(project: Project, register: Register, presentation: RegisterPresentation): RegisterCell
}