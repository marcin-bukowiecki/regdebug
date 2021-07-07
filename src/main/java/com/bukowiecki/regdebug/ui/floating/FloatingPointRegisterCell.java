/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.floating;

import com.bukowiecki.regdebug.parsers.Register;
import com.bukowiecki.regdebug.ui.RegisterCellBase;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class FloatingPointRegisterCell extends RegisterCellBase {

    private JPanel mainPanel;
    private JLabel registerLabel;
    private JTextField hexTextField;

    public FloatingPointRegisterCell(Register register) {
        super(register);
        this.registerLabel.setText(register.getRegisterName());
        this.hexTextField.setEditable(false);
        this.hexTextField.setText(register.getHex());
    }

    @Override
    public @NotNull JTextField getHexTextField() {
        return hexTextField;
    }

    @Override
    public @NotNull JPanel getMainPanel() {
        return mainPanel;
    }

    @NotNull
    @Override
    public JLabel getRegisterLabel() {
        return registerLabel;
    }
}
