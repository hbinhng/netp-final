package org.uet.int3304.gateway.UI;

import org.uet.int3304.gateway.UI.controllers.RootController;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
//import javafx.stage.Screen;
import javafx.stage.Stage;

public class GatewayUI extends Application {
  private static GatewayUI gatewayUI;
  //private static final double INITIAL_SCALE = 0.8;
  private static final int INITIAL_WIDTH = 1080;
  private static final int INITIAL_HEIGHT = 600;
  private static final String TITLE = "Gateway UI";
  private RootController ctrl;

  @Override
  public void start(Stage stage) throws Exception {
    var fxmlLoader = new FXMLLoader(GatewayUI.class.getResource("root-view.fxml"));
    //var screenSize = Screen.getPrimary().getBounds();

    var scene = new Scene(
        fxmlLoader.load(),
        //screenSize.getWidth() * INITIAL_SCALE,
        //screenSize.getHeight() * INITIAL_SCALE);
        INITIAL_WIDTH,
        INITIAL_HEIGHT);

    stage.setTitle(TITLE);
    stage.setScene(scene);

    stage.show();
    ctrl = (RootController)(fxmlLoader.getController());
  }

  public void pushTData(double value) {
    ctrl.pushTData(value);
  }

  public void pushHBData(double value) {
    ctrl.pushHBData(value);
  }

  public void pushBPData(double systolicValue, double diastolicValue) {
    ctrl.pushBPData(systolicValue, diastolicValue);
  }

  public static void launch(String[] args) {
    Application.launch(args);
  }

  public static GatewayUI getInstance() {
    if (gatewayUI == null) {
      gatewayUI = new GatewayUI();
    }
    return gatewayUI;
  }
}
