module org.uet.int3304.gateway {
    requires javafx.base;
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.graphics;
    requires javafx.web;

    opens org.uet.int3304.gateway
            to javafx.base, javafx.controls, javafx.fxml;

    opens org.uet.int3304.gateway.UI
            to javafx.base, javafx.controls, javafx.fxml,
            javafx.graphics;

    opens org.uet.int3304.gateway.UI.controllers
            to javafx.base, javafx.controls, javafx.fxml,
            javafx.graphics;
}