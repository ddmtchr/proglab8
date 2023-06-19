package client.controllers;

import javafx.scene.control.TextFormatter;

import java.util.regex.Pattern;

public class Util {
    public static TextFormatter<String> getWholeNumbersFormatter() {
        return new TextFormatter<>(change -> {
            if (Pattern.compile("^-?(\\d*)$").matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        });
    }

    public static TextFormatter<String> getFracNumbersFormatter() {
        return new TextFormatter<>(change -> {
            if (Pattern.compile("^-?(\\d*)([.,]\\d*)?$").matcher(change.getControlNewText()).matches()) {
                return change;
            } else {
                return null;
            }
        });
    }
}
