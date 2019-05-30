package XO;

import XO.Model.Account;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.StringTokenizer;

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

    public void handleInput(String s) {
        String[] strings = s.split("\\s");
        if (strings.length>=1) {
            if (strings[0].equals("1")) {
                String accountName = strings[1];
                String password = strings[2];

                Account account = new Account(accountName, password);
                Server.allOfAccount.add(account);
            } else if (strings[0].equals("2")) {
                String accountName = strings[1];
                String password = strings[2];
                if (Account.isAccountPasswordRight(accountName, password, Server.allOfAccount)) {
                    Account account = Account.findAccount(accountName, Server.allOfAccount);
                    this.account = account;
                    Server.loginedAccount.add(account);
                }
            }
        }
    }

}
