package XO.Controllers;

import XO.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.input.MouseEvent;
import sun.misc.Cleaner;


import java.io.IOException;

import static XO.Constants.LIST_OF_PAUSED_GAMES;
import static XO.Constants.LOGINED_USER;

public class ResumeController {

    @FXML
    Label loginedUser_lbl;

    @FXML
    ListView listOfGames_lv;

    public static final ObservableList data =
            FXCollections.observableArrayList();

    public void fillListView() throws IOException {
        Client.dos.writeUTF(LIST_OF_PAUSED_GAMES + " " + loginedUser_lbl.getText());

        String result = Client.dis.readUTF();

        String[] games = result.split(" , ");

        data.clear();


        for (int i =0;i<games.length;i++)
        {
            data.add(games[i]);
        }
        listOfGames_lv.setItems(data);
        listOfGames_lv.setCellFactory(ComboBoxListCell.forListView(data));

        listOfGames_lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

            @Override
            public void handle(MouseEvent event) {
                System.out.println("clicked on " + listOfGames_lv.getSelectionModel().getSelectedItem());
            }
        });

        /*
         lv.setOnMouseClicked(new EventHandler<MouseEvent>() {

        @Override
        public void handle(MouseEvent event) {
            System.out.println("clicked on " + lv.getSelectionModel().getSelectedItem());
        }
    });
         */

    }


    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
        }
    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);


    }
}
