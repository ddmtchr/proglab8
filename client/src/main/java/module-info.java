module ddmtchr.client {
    requires javafx.controls;
    requires javafx.fxml;
    requires common;


    opens client.controllers to javafx.fxml;
    exports client.controllers;
    exports launch;
    opens launch to javafx.fxml;
}