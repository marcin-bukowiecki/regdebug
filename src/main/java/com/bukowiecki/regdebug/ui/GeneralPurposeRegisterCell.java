/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui;

import com.bukowiecki.regdebug.parsers.Register;
import com.bukowiecki.regdebug.presentation.RegisterPresentation;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class GeneralPurposeRegisterCell extends RegisterCellBase {

    private JPanel mainPanel;
    private JTextField hexTextField;
    private JLabel registerLabel;
    private JLabel infoLabel;

    public GeneralPurposeRegisterCell(Register register, RegisterPresentation registerPresentation) {
        super(register);
        this.registerLabel.setText(register.getRegisterName());
        this.hexTextField.setEditable(false);
        this.hexTextField.setText(registerPresentation.getText(register));
        this.infoLabel.setText(register.getInfo());
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
