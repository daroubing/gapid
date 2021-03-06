/*
 * Copyright (C) 2017 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.google.gapid.views;

import static com.google.gapid.widgets.Widgets.createComposite;
import static com.google.gapid.widgets.Widgets.createLabel;
import static com.google.gapid.widgets.Widgets.withLayoutData;

import com.google.gapid.models.Settings;
import com.google.gapid.util.Messages;
import com.google.gapid.widgets.FileTextbox;

import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.TitleAreaDialog;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Shell;

/**
 * Dialog that allows the user to modify application settings.
 */
public class SettingsDialog extends TitleAreaDialog {
  private final Settings settings;
  private FileTextbox adbPath;

  public SettingsDialog(Shell parent, Settings settings) {
    super(parent);
    this.settings = settings;
  }

  public static void showSettingsDialog(Shell shell, Settings settings) {
    new SettingsDialog(shell, settings).open();
  }

  private void update() {
    settings.adb = adbPath.getText().trim();
  }

  @Override
  public void create() {
    super.create();
    setTitle(Messages.SETTINGS_TITLE);
  }

  @Override
  protected boolean isResizable() {
    return true;
  }

  @Override
  protected void configureShell(Shell newShell) {
    super.configureShell(newShell);
    newShell.setText(Messages.SETTINGS_TITLE);
  }

  @Override
  protected Control createDialogArea(Composite parent) {
    Composite area = (Composite)super.createDialogArea(parent);

    Composite container = createComposite(area, new GridLayout(2, false));
    container.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
    createLabel(container, "Path to adb:*");
    adbPath = withLayoutData(new FileTextbox.File(container, settings.adb) {
      @Override
      protected void configureDialog(FileDialog dialog) {
        dialog.setText("Path to adb:");
      }
    }, new GridData(SWT.FILL, SWT.FILL, true, false));

    createLabel(container, "");
    createLabel(container, "* Requires Restart");

    return area;
  }

  @Override
  protected void createButtonsForButtonBar(Composite parent) {
    createButton(parent, IDialogConstants.CANCEL_ID, IDialogConstants.CANCEL_LABEL, false);
    createButton(parent, IDialogConstants.OK_ID, IDialogConstants.OK_LABEL, true);
  }

  @Override
  protected void buttonPressed(int buttonId) {
    if (buttonId == IDialogConstants.OK_ID) {
      update();
      settings.save();
    }
    super.buttonPressed(buttonId);
  }
}
