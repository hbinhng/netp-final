package org.uet.int3304.gateway.UI.controllers;

import java.net.URL;
import javafx.util.Duration;

import java.util.LinkedList;
import java.util.Map;
import java.util.ResourceBundle;

import org.uet.int3304.gateway.AppConfig.Config;
import org.uet.int3304.gateway.Group.GroupManager;
import org.uet.int3304.gateway.UI.BucketId;
import org.uet.int3304.gateway.UI.NodeState;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class ConfiguratorController implements Initializable {
	private static final Map<String, String> BUCKETID_TO_TYPE = Map.ofEntries(
			Map.entry(BucketId.SYSTOLIC_BUCKET, "Blood pressure sensor"),
			Map.entry(BucketId.DIASTOLIC_BUCKET, "Blood pressure sensor"),
			Map.entry(BucketId.HB_BUCKET, "Heart beat sensor"),
			Map.entry(BucketId.TEMP_BUCKET, "Temperature sensor"));

	@FXML
	private Button saveButton;

	@FXML
	private TextField dataIntervalInput;

	@FXML
	private Label motd;

	@FXML
	private ComboBox<String> groupSelector;

	@FXML
	private ListView<String> activeNodeList;

	private Text savingStatus;

	private final NodeState state;
	private final GroupManager groupManager;

	public ConfiguratorController() {
		state = NodeState.getInstance();
		groupManager = GroupManager.getInstance();
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

	public void openGroupSelector() {
		var groups = groupManager.getGroups();

		groupSelector.setItems(FXCollections.observableArrayList(groups));
	}

	public void chooseGroup() {
		state.setGroup(groupSelector.getValue());

		System.out.printf("Observing group changed to [%s]\n", state.getGroup());
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		motd.setText(String.format(motd.getText(), Config.getInstance().getPort()));
		dataIntervalInput.setText(Long.toString(state.getDataInterval()));
		groupSelector.getItems().clear();

		var refreshActiveNodeListTimeline = new Timeline(
				new KeyFrame(Duration.millis(500), (event) -> {
					var connected = groupManager.getConnectedInGroup(state.getGroup());

					var activeNodes = new LinkedList<String>();

					for (var node : connected) {
						activeNodes.add(
								String.format(
										"[%d]: %s",
										node.getKey(),
										BUCKETID_TO_TYPE.get(node.getValue())));
					}

					activeNodeList.setItems(FXCollections.observableArrayList(activeNodes));
				}));

		refreshActiveNodeListTimeline.setCycleCount(Timeline.INDEFINITE);
		refreshActiveNodeListTimeline.play();
	}
}
