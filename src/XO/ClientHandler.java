package XO;

import XO.Model.Account;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;
import static XO.Constants.*;

public class ClientHandler implements Runnable {

    public DataOutputStream dos;
    public DataInputStream dis;
    public Server server;
    public long UID;
    public Account account = null;

    public ClientHandler(DataOutputStream dos, DataInputStream dis, Server server, Socket socket, long UID) {
        this.dos = dos;
        this.dis = dis;
        this.server = server;
    }

    @Override
    public void run() {
        while (true) {
            try {
                String s = dis.readUTF();
                handleInput(s);
            } catch (IOException e) {
            }

        }

    }

    public void handleInput(String s) throws IOException {
        String[] strings = s.split("\\s");
        if (strings.length >= 1) {
            if (strings[0].equals(SIGNUP)) { //Signup
                String accountName = strings[1];
                String password = strings[2];

                Account account = new Account(accountName, password);
                Server.allOfAccount.add(account);
            } else if (strings[0].equals(LOGIN)) { //Login
                String accountName = strings[1];
                String password = strings[2];
                if (Account.isAccountPasswordRight(accountName, password, Server.allOfAccount)) {
                    Account account = Account.findAccount(accountName, Server.allOfAccount);
                    this.account = account;
                    Server.loginedAccount.add(account);
                }
            } else if (strings[0].equals(GOTO_ALL_USERS)) //ShowAllAccounts
            {
                String send = "";
                for (int i = 0; i < Server.allOfAccount.size(); i++) {
                    send = send + Server.allOfAccount.get(i).getUsername() + ",";
                }
                dos.writeUTF(send);
            } else if (strings[0].equals(LOGINED_USER)) { //LogindUser
                if (this.account == null) {
                    dos.writeUTF("NO USER LOGINED");
                } else {
                    String send = this.account.getUsername();
                    dos.writeUTF(send);
                }
            }
        }
    }

}
