package Util;

import javafx.scene.control.Alert;
import javafx.stage.Window;

/**
 * Created by caiomcg on 18/05/17.
 */
public class CreateDialog {
    public static Alert createAlert(Window window, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Courier Error");
        alert.initOwner(window);
        alert.setContentText(message);
        return alert;
    }

    public static Alert createInformation(Window window, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Courier Info");
        alert.initOwner(window);
        alert.setContentText(message);
        return alert;
    }
}
