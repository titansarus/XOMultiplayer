package XO;

import XO.Controllers.Container;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import static XO.Constants.*;

public class Client extends Application {

    public static DataOutputStream dos;
    public static DataInputStream dis;


    {
        Pane root = null;
        FXMLLoader fxmlLoader = null;
        try {
            fxmlLoader = new FXMLLoader(getClass().getResource("/XO/ViewFXML/LoginFXML.fxml"));
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }


        Scene scene = new Scene(root);
        Container.scenes.add(scene);
        // ((LoginController) fxmlLoader.getController()).updateLoginedUser();
    }

    public static void main(String[] args) throws Exception {
        Socket socket = new Socket("localhost", port);
        dis = new DataInputStream(socket.getInputStream());
        dos = new DataOutputStream(socket.getOutputStream());

        launch(args);

    }


    @Override
    public void start(Stage primaryStage) throws Exception {
        primaryStage = Container.stage;


        primaryStage.setScene(Container.scenes.getLast());
        primaryStage.show();
    }
}
