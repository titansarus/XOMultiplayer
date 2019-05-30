package XO.Controllers;

import XO.Client;
import XO.Constants;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

import static XO.Constants.*;

public class MainMenuController {

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
    public void checkIsSummoned() throws IOException{
        Client.dos.writeUTF(SUMMONED);

        String summoned = Client.dis.readUTF();

        if (summoned.equals(SUMMONED_TO_GAME))
        {
            String sendMessage2 = GIVE_MY_GAMEINFO;
            Client.dos.writeUTF(sendMessage2);
            String[] strings = Client.dis.readUTF().split(" ");
            new Container().enterGame(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
        }
    }


    public void handeBtnQuit() throws IOException {
        String sendMesssage = QUIT + " " + loginedUser_lbl.getText();

        Client.dos.writeUTF(sendMesssage);

        try {
            Thread.sleep(1500);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.exit(0);
    }

    public void handleBtnPlayGame() throws IOException {

        int row = gettingNumberOfRowsFromUser();
        int column = gettingNumberOfColumnsFromUser();
        String anotherUsername = gettingAnotherPlayerAccountUsername();
        System.out.println(row);
        System.out.println(column);
        String sendMessage = ENTER_GAME + " " + anotherUsername + " " + row + " " + column;
        Client.dos.writeUTF(sendMessage);
        String recievedMessage = Client.dis.readUTF();
        if (!recievedMessage.equals(DONE)) {
            Container.ExceptionGenerator(recievedMessage);
        }
//        else
//        {
//            String sendMessage2 = GIVE_MY_GAMEINFO;
//            Client.dos.writeUTF(sendMessage2);
//            String[] strings = Client.dis.readUTF().split(" ");
//            new Container().enterGame(Integer.parseInt(strings[0]),Integer.parseInt(strings[1]));
//        }

    }


    private String gettingAnotherPlayerAccountUsername() {
        String s = "NO USER NAME";
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Another Player UserName");
        textInputDialog.setHeaderText("Please Enter UserName of another Player");
        textInputDialog.setContentText("Username");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {
            s = result.get();

        }

        return s;
    }

    private int gettingNumberOfRowsFromUser() {
        int n = 3;
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Size of Board");
        textInputDialog.setHeaderText("Please Enter a number between 3 to 10. Default is 3");
        textInputDialog.setContentText("Board Size Rows (n):");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {

            //TODO CHECK IF USER INPUTS VALID NUMBER;
            //TODO DUPLICATE
            n = Integer.parseInt(result.get());

        }

        return n;
    }

    private int gettingNumberOfColumnsFromUser() {
        int n = 3;
        TextInputDialog textInputDialog = new TextInputDialog();
        textInputDialog.setTitle("Size of Board");
        textInputDialog.setHeaderText("Please Enter a number between 3 to 10. Default is 3");
        textInputDialog.setContentText("Board Size Columns (n):");
        Optional<String> result = textInputDialog.showAndWait();
        if (result.isPresent()) {

            //TODO CHECK IF USER INPUTS VALID NUMBER;
            //TODO DUPLICATE
            n = Integer.parseInt(result.get());

        }

        return n;
    }


    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
        }


    }
}
