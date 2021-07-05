/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui;

import com.bukowiecki.regdebug.bundle.RegDebugBundle;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class FlagsCell {
    private JPanel mainPanel;
    private JTextField flagsTextField;
    private JLabel flagsLabel;
    private JLabel headerLabel;

    public FlagsCell() {
        flagsLabel.setText(RegDebugBundle.INSTANCE.message("regdebug.flags"));
        flagsTextField.setEnabled(false);
        headerLabel.setText("");
    }

    public JLabel getHeaderLabel() {
        return headerLabel;
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }

    public JTextField getFlagsTextField() {
        return flagsTextField;
    }
}
