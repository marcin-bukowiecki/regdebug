/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.general;

import com.bukowiecki.regdebug.bundle.RegDebugBundle;
import com.bukowiecki.regdebug.parsers.GeneralPurposeRegisters;
import com.bukowiecki.regdebug.settings.RegDebugSettings;
import com.bukowiecki.regdebug.ui.BaseFilterForm;
import com.bukowiecki.regdebug.ui.RegDebugView;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class GeneralPurposeRegistersHeaderForm extends BaseFilterForm<GeneralPurposeRegisters> {
    private JPanel mainPanel;
    private JTextField flagsTextField;
    private JLabel flagsLabel;
    private JLabel statusLabel;
    private JTextField filterTextField;
    private JPanel registerFilterPanel;
    private JLabel filterLabel;
    private JPanel flagsPanel;

    public GeneralPurposeRegistersHeaderForm(RegDebugView<GeneralPurposeRegisters> regDebugView) {
        super(regDebugView);

        flagsLabel.setText(RegDebugBundle.INSTANCE.message("regdebug.flags"));
        filterLabel.setText(RegDebugBundle.INSTANCE.message("regdebug.tab.filter"));

        flagsTextField.setEnabled(false);
        statusLabel.setText("");

        initListeners();
    }

    public JLabel getStatusLabel() {
        return statusLabel;
    }

    @Override
    public @NotNull JTextField getFilterTextField() {
        return filterTextField;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getFlagsTextField() {
        return flagsTextField;
    }

    @Override
    public void setSettings(@NotNull RegDebugSettings settings) {
        settings.setGeneralRegistersToSelect(filterTextField.getText());
    }

    @Override
    public @NotNull String getSettings(@NotNull RegDebugSettings settings) {
        return settings.getGeneralRegistersToSelect();
    }

    @NotNull
    @Override
    public Class<? extends RegDebugView<?>> getViewClass() {
        return GeneralPurposeView.class;
    }
}
