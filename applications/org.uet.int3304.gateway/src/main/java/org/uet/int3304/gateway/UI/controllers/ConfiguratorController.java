package org.uet.int3304.gateway.UI.controllers;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConfiguratorController {
	private Button saveDataIntervalButton;
	private TextField dataIntervalInput;
	private Text savingStatus;
	private Text nodeList;

	public ConfiguratorController(Text nodeList, Button saveDataIntervalButton, TextField dataIntervalInput, Text savingStatus) {
		this.saveDataIntervalButton = saveDataIntervalButton;
		this.dataIntervalInput = dataIntervalInput;
		this.savingStatus = savingStatus;
		this.nodeList = nodeList;
		this.nodeList.setWrappingWidth(300);
	}

	public void saveConfig() {
		if (dataIntervalInput.getText().equals("")) {
			savingStatus.setText("Please enter data interval!");
			return;
		}
		System.out.println(dataIntervalInput.getText());

		if(dataIntervalInput.getText().equals("0")) {
			savingStatus.setText("Data interval must be greater than 0!");
			return;
		}

		savingStatus.setText("Data interval save successfully!");
		dataIntervalInput.setText("");
	}

}
