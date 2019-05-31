package XO.Controllers;

import XO.Client;
import XO.Model.AccountInfo;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.util.ArrayList;

import static XO.Constants.*;

public class LeaderboardFXMLController {

    @FXML
    TableView leaderboard_tbv;

    @FXML
    Label loginedUser_lbl;


    public void leaderBoardMaker() throws IOException {
        String sendMessage = GIVE_ALL_ACCOUNT_INFO;
        Client.dos.writeUTF(sendMessage);
        String recievedMessage;
        recievedMessage = Client.dis.readUTF();
        String[] accountsStrings = recievedMessage.split(" , ");
        ArrayList<AccountInfo> accountInfos = new ArrayList<>();
        for (int i = 0; i < accountsStrings.length; i++) {
            String[] info = accountsStrings[i].split(" ");
            String username = info[0];
            int wins = Integer.parseInt(info[1]);
            int loses = Integer.parseInt(info[2]);
            int draws = Integer.parseInt(info[3]);

            AccountInfo accountInfo = new AccountInfo(username, wins, loses, draws);
            accountInfos.add(accountInfo);
        }

        /* FOR TEST UNCOMMENT THIS
        accountInfos.add(new AccountInfo("abbas" , 3 , 5 , 2));
        accountInfos.add(new AccountInfo("ali" , 4 , 3 , 3));
        accountInfos.add(new AccountInfo("mohammadhossein" , 2 , 6 , 2));
        accountInfos.add(new AccountInfo("mohammadali" , 2 , 5 , 2));
        accountInfos.add(new AccountInfo("ahmad" , 3 , 4 , 3));
        */

        ArrayList<AccountInfo> sortedAccount = AccountInfo.accountInfoSorter(accountInfos);
        for (int i = 0; i < sortedAccount.size(); i++) {
            sortedAccount.get(i).setRank(i + 1);
        }


        TableColumn<AccountInfo, Integer> column1 = new TableColumn<>("Rank");
        column1.setCellValueFactory(new PropertyValueFactory<>("rank"));


        TableColumn<AccountInfo, String> column2 = new TableColumn<>("Username");
        column2.setCellValueFactory(new PropertyValueFactory<>("username"));

        TableColumn<AccountInfo, Integer> column3 = new TableColumn<>("Wins");
        column3.setCellValueFactory(new PropertyValueFactory<>("wins"));

        TableColumn<AccountInfo, Integer> column4 = new TableColumn<>("Loses");
        column4.setCellValueFactory(new PropertyValueFactory<>("loses"));

        TableColumn<AccountInfo, Integer> column5 = new TableColumn<>("Draws");
        column5.setCellValueFactory(new PropertyValueFactory<>("draws"));

        TableView tableView = leaderboard_tbv;

        tableView.getColumns().add(column1);
        tableView.getColumns().add(column2);
        tableView.getColumns().add(column3);
        tableView.getColumns().add(column4);
        tableView.getColumns().add(column5);

        for (int i = 0; i < sortedAccount.size(); i++) {
            AccountInfo account = sortedAccount.get(i);
            tableView.getItems().add(account);
        }

    }

    public void updateLoginedUser() throws IOException {
        Client.dos.writeUTF(LOGINED_USER);

        String user = Client.dis.readUTF();

        loginedUser_lbl.setText(user);
    }

    public void handleBack() {
        if (Container.scenes.size() > 0) {
            Container.scenes.removeLast();
            Container.stage.setScene(Container.scenes.getLast());
            Container.stage.show();
        }
    }


}
