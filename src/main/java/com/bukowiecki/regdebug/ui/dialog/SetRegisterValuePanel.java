/*
 * Copyright 2021 Marcin Bukowiecki.
 * Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
 */

package com.bukowiecki.regdebug.ui.dialog;

import com.bukowiecki.regdebug.backend.BackendType;
import com.bukowiecki.regdebug.parsers.ParseUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 * @author Marcin Bukowiecki
 */
public class SetRegisterValuePanel {
  private JPanel mainPanel;
  private JTextField registerValueTextField;
  private JComboBox<String> operatorComboBox;
  private JPanel toolboxPanel;
  private JPanel headerPanel;
  private JLabel messageLabel;

  public SetRegisterValuePanel(BackendType backendType) {
    for (String s : ParseUtils.INSTANCE.getRegisterOperators().get(backendType)) {
      operatorComboBox.addItem(s);
    }
    messageLabel.setText("");
  }

  public @NotNull JLabel getMessageLabel() {
    return messageLabel;
  }

  public @NotNull JComboBox<String> getOperatorComboBox() {
    return operatorComboBox;
  }

  public @NotNull JTextField getRegisterValueTextField() {
    return registerValueTextField;
  }

  public @NotNull JPanel getMainPanel() {
    return mainPanel;
  }
}
