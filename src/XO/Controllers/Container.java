package XO.Controllers;

import XO.Exceptions.*;
import javafx.animation.Timeline;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

import java.util.Deque;
import java.util.LinkedList;

import static XO.Constants.*;

public class Container {

    public static Stage stage = new Stage();
    public static Deque<Scene> scenes = new LinkedList<>();

    public static void ExceptionGenerator(String msg) {
        if (msg.equals(ACCOUNT_EXIST_EXCEPTION_PROMPT)) {
            alertShower(new AccountExistException(), "Account Exists");
        }
        if (msg.equals(INVALID_PASSWORD_EXCEPTION_PROMPT)) {
            alertShower(new InvalidPasswordException(), "Invalid Password");
        }
        if (msg.equals(ACCOUNT_NOT_EXIST_EXCEPTION_PROMPT)) {
            alertShower(new NoAccountExistException(), "Account Not Exists");
        }
        if (msg.equals(NO_USER_LOGINED)) {
            alertShower(new NoUserLoginedException(), "No User Logined");
        }
        if (msg.equals(USER_ALREADY_LOGINED_PROMPT))
        {
            alertShower(new UserAlreadyLoginedException() , "User Already Logined");
        }
    }

    public static void alertShower(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.show();
    }


}
