package XO.Controllers;

import XO.Client;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.io.IOException;


import static XO.Constants.*;

public class ListOfUsesrFXMLController {
    @FXML
    ListView<String> users_lv;

    public static final ObservableList data =
            FXCollections.observableArrayList();

    public void updateUsersList() throws IOException {
        String s = GOTO_ALL_USERS;
        Client.dos.writeUTF(s);
        String ss = Client.dis.readUTF();
        String[] users = ss.split(",");
        ResumeController.setListViewToString(users, data, users_lv);

    }

    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
        }


    }
}
