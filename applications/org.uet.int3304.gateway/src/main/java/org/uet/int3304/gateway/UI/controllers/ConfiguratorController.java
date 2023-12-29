package org.uet.int3304.gateway.UI.controllers;

import org.uet.int3304.gateway.UI.NodeState;

import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConfiguratorController {
	private final Button saveDataIntervalButton;
	private final TextField dataIntervalInput;
	private final Text savingStatus;
	private final Text nodeList;

	private final NodeState state;

	public ConfiguratorController(Text nodeList, Button saveDataIntervalButton, TextField dataIntervalInput,
			Text savingStatus) {
		this.saveDataIntervalButton = saveDataIntervalButton;
		this.dataIntervalInput = dataIntervalInput;
		this.savingStatus = savingStatus;
		this.nodeList = nodeList;
		this.nodeList.setWrappingWidth(300);

		state = NodeState.getInstance();

		dataIntervalInput.setText(Long.toString(state.getDataInterval()));
	}

	public void saveConfig() {
		if (dataIntervalInput.getText().equals("")) {
			savingStatus.setText("Please enter data interval!");
			return;
		}
		System.out.println(dataIntervalInput.getText());

		if (dataIntervalInput.getText().equals("0")) {
			savingStatus.setText("Data interval must be greater than 0!");
			return;
		}

		savingStatus.setText("Data interval save successfully!");
		dataIntervalInput.setText("");
	}

}
