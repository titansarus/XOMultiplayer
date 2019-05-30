package XO.Controllers;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Deque;
import java.util.LinkedList;

public class Container {

    public static Stage stage = new Stage();
    public static Deque<Scene> scenes = new LinkedList<>();

    static void alertShower(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.show();
    }

}
