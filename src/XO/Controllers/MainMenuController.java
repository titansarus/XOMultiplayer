package XO.Controllers;

import XO.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.util.Duration;

import java.io.IOException;
import java.util.Optional;

import static XO.Constants.LOGINED_USER;
import static XO.Constants.QUIT;

public class MainMenuController {

    @FXML
    Label loginedUser_lbl;


    @FXML
    public void initialize() {
        Timeline timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            try {
                updateLoginedUser();
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

    public void handleBtnPlayGame() {

        int row = gettingNumberOfRowsFromUser();
        int column = gettingNumberOfColumnsFromUser();

        System.out.println(row);
        System.out.println(column);

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
