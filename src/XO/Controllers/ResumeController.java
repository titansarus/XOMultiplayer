package XO.Controllers;

import XO.Client;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;
import javafx.util.Duration;


import java.io.IOException;

import static XO.Constants.*;

public class ResumeController {

    @FXML
    Label loginedUser_lbl;

    @FXML
    ListView listOfGames_lv;


    private Timeline timeline;


    public void timeLineGen() {
        timeline = new Timeline(new KeyFrame(Duration.ZERO, event -> {
            try {
                fillListView();
            } catch (IOException e) {

            }
        }), new KeyFrame(Duration.millis(2000)));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public void timeLineStopper() {
        timeline.stop();
    }

    public static final ObservableList data =
            FXCollections.observableArrayList();

    public void fillListView() throws IOException {
        Client.dos.writeUTF(LIST_OF_PAUSED_GAMES + " " + loginedUser_lbl.getText());

        String result = Client.dis.readUTF();

        String[] games = result.split(" , ");

        setListViewToString(games, data, listOfGames_lv);

        listOfGames_lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                try {
                    Client.dos.writeUTF(RESUME + " " + listOfGames_lv.getSelectionModel().getSelectedItem());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


    }

    static void setListViewToString(String[] games, ObservableList data, ListView listOfGames_lv) {
        data.clear();


        for (int i = 0; i < games.length; i++) {
            data.add(games[i]);
        }
        listOfGames_lv.setItems(data);
        listOfGames_lv.setCellFactory(ComboBoxListCell.forListView(data));
    }


    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
            timeLineStopper();
        }
    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);


    }
}
