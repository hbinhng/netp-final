#!/bin/sh

java \
  --class-path applications/org.uet.int3304.gateway/target/org.uet.int3304.gateway-1.0.0-SNAPSHOT.jar \
  --module-path applications/org.uet.int3304.gateway/target/jfx-lib/ \
  --add-modules javafx.base,javafx.controls,javafx.fxml,javafx.media,javafx.graphics,javafx.swing,javafx.web \
  org.uet.int3304.gateway.Program