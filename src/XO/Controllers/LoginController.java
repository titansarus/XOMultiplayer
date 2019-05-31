package XO.Controllers;

import XO.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import static XO.Constants.*;

public class LoginController {
    @FXML
    Button signUp_btn;

    @FXML
    Button login_btn;


    @FXML
    TextField username_tf;

    @FXML
    PasswordField password_tf;

    @FXML
    Button listOfUsers_btn;

    @FXML
    Label loginedUser_lbl;


    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            try {
                updateLoginedUser();
                checkIsSummoned();
            } catch (IOException e) {

            }
        }), new KeyFrame(Duration.seconds(1)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();


    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);
    }

    public void checkIsSummoned() throws IOException {
        Client.dos.writeUTF(SUMMONED);

        String summoned = Client.dis.readUTF();

        System.out.println(summoned);
    }


    public void handleListOfUsersButton() {
        Pane root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/XO/ViewFXML/ListOfUsersFXML.fxml"));
            root = fxmlLoader.load();
            int i = 0;
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Container.scenes.addLast(scene);
        Container.stage.setScene(Container.scenes.getLast());
        try {
            ((ListOfUsesrFXMLController) fxmlLoader.getController()).updateUsersList();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Container.stage.show();
    }


    public void handleBtnSignUp() throws IOException {

        String username = username_tf.getText();
        String password = password_tf.getText();

        DataInputStream dis = Client.dis;
        DataOutputStream dos = Client.dos;
        String sendMessage = SIGNUP + " " + username + " " + password;

        dos.writeUTF(sendMessage);

        String result = dis.readUTF();

        Container.ExceptionGenerator(result);


    }

    public void handleBtnMainMenu() {
        if (loginedUser_lbl.getText().equals(NO_USER_LOGINED)) {
            Container.ExceptionGenerator(NO_USER_LOGINED);
            return;
        }
        Pane root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/XO/ViewFXML/MainMenu.fxml"));
            root = fxmlLoader.load();
            int i = 0;
            System.out.println(i);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Scene scene = new Scene(root);
        Container.scenes.addLast(scene);
        Container.stage.setScene(Container.scenes.getLast());
        Container.stage.show();

    }

    public void handleBtnLogin() throws IOException {
        String username = username_tf.getText();
        String password = password_tf.getText();

        String sendMessage = LOGIN + " " + username + " " + password;
        Client.dos.writeUTF(sendMessage);

        String result = Client.dis.readUTF();

        Container.ExceptionGenerator(result);

    }


}
