package XO.Controllers;

import XO.Client;
import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class LoginController {
    @FXML
    Button signUp_btn;

    @FXML
    Button login_btn;


    @FXML
    TextField username_tf;

    @FXML
    PasswordField password_tf;


    public void handleBtnSignUp() throws IOException {

        String username = username_tf.getText();
        String password = password_tf.getText();

        DataInputStream dis = Client.dis;
        DataOutputStream dos = Client.dos;
        String sendMessage = "1 "+username + " "+password;

        dos.writeUTF(sendMessage);


    }

    public void handleBtnLogin() {

    }


}
