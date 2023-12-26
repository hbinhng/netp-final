package org.uet.int3304.gateway.UI;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class GatewayUI extends Application {
  private static final double INITIAL_SCALE = 0.6;
  private static final String TITLE = "Gateway UI";

  @Override
  public void start(Stage stage) throws Exception {
    var fxmlLoader = new FXMLLoader(GatewayUI.class.getResource("root-view.fxml"));
    var screenSize = Screen.getPrimary().getBounds();

    var scene = new Scene(
        fxmlLoader.load(),
        screenSize.getWidth() * INITIAL_SCALE,
        screenSize.getHeight() * INITIAL_SCALE);

    stage.setTitle(TITLE);
    stage.setScene(scene);

    stage.show();
  }

  public static void launch(String[] args) {
    Application.launch(args);
  }
}
