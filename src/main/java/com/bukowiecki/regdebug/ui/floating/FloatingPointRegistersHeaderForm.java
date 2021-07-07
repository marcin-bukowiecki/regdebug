/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.floating;

import com.bukowiecki.regdebug.bundle.RegDebugBundle;
import com.bukowiecki.regdebug.parsers.FloatingPointRegisters;
import com.bukowiecki.regdebug.settings.RegDebugSettings;
import com.bukowiecki.regdebug.ui.BaseFilterForm;
import com.bukowiecki.regdebug.ui.RegDebugView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class FloatingPointRegistersHeaderForm extends BaseFilterForm<FloatingPointRegisters> {
    private JPanel mainPanel;
    private JPanel registerFilterPanel;
    private JLabel filterLabel;
    private JTextField filterTextField;

    public FloatingPointRegistersHeaderForm(RegDebugView<FloatingPointRegisters> regDebugView) {
        super(regDebugView);
        filterLabel.setText(RegDebugBundle.INSTANCE.message("regdebug.tab.filter"));
        initListeners();
    }

    @NotNull
    @Override
    public JTextField getFilterTextField() {
        return filterTextField;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    @Override
    public void setSettings(@NotNull RegDebugSettings settings) {
        settings.setFloatingRegistersToSelect(filterTextField.getText());
    }

    @Override
    public @NotNull String getSettings(@NotNull RegDebugSettings settings) {
        return settings.getFloatingRegistersToSelect();
    }

    @NotNull
    @Override
    public Class<? extends RegDebugView<?>> getViewClass() {
        return FloatingPointView.class;
    }
}
