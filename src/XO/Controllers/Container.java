package XO.Controllers;

import XO.Exceptions.*;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
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
        if (msg.equals(USER_ALREADY_LOGINED_PROMPT)) {
            alertShower(new UserAlreadyLoginedException(), "User Already Logined");
        }
        if (msg.equals(INVALID_ROW_COL_NUMBER_PROMPT)) {
            alertShower(new InvalidRowColNumberException(), "Invalid Row or Column Number");
        }
        if (msg.equals(INVALID_UNDO_PROMPT)) {
            alertShower(new InvalidUndoException(), "Invalid Undo");
        }
        if (msg.equals(NOT_YOUR_TURN_PROMPT)) {
            alertShower(new NotYourTurnException(), "Not Your Turn");
        }
        if (msg.equals(OTHER_PLAYER_IS_PLAYING_PROMPT)) {
            alertShower(new OtherPlayerIsPlayingException(), "Other Player Playing");
        }
        if (msg.equals(YOU_CANT_PLAY_WITH_YOURSELF_PROMPT))
        {
            alertShower(new YouCantPlayWithYourselftException() , "You Can't Play XO With Yourself");
        }
    }

    public static void alertShower(Exception e, String title) {
        Alert alert = new Alert(Alert.AlertType.ERROR, e.getMessage());
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.show();
    }

    public static void notificationShower(String context, String title) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, context);
        alert.setTitle(title);
        alert.setHeaderText(title);
        alert.show();
    }

    public void enterGame(int row, int column) throws IOException {
        Pane root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/XO/ViewFXML/GameController.fxml"));
            root = fxmlLoader.load();
            int i = 0;
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Container.scenes.addLast(scene);
        Container.stage.setScene(Container.scenes.getLast());
        GameController controller = (GameController) fxmlLoader.getController();

        controller.row = row;
        controller.column = column;
        controller.blockMaker();
        controller.updateLoginedUser();
        controller.updateGameBoard();
        controller.timeLineGen();

        root.getChildren().addAll(controller.blocks);
        //   root.getChildren().addAll(controller.blockTexts);
        //   root.getChildren().addAll(controller.blockTexts);

        Container.stage.show();
    }


}
