package XO;

import XO.Model.Account;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

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
                signUp(strings);
            } else if (strings[0].equals(LOGIN)) { //Login
                login(strings);
            } else if (strings[0].equals(GOTO_ALL_USERS)) //ShowAllAccounts
            {
                goToAllUsers();
            } else if (strings[0].equals(LOGINED_USER)) { //LogindUser
                loginedUser();
            }
        }
    }

    private void loginedUser() throws IOException {
        if (this.account == null) {
            dos.writeUTF("NO USER LOGINED");
        } else {
            String send = this.account.getUsername();
            dos.writeUTF(send);
        }
    }

    private void goToAllUsers() throws IOException {
        String send = "";
        for (int i = 0; i < Server.allOfAccount.size(); i++) {
            send = send + Server.allOfAccount.get(i).getUsername() + ",";
        }
        dos.writeUTF(send);
    }

    private void login(String[] strings) throws IOException {
        String accountName = strings[1];
        String password = strings[2];
        String sendMessage = "";
        if (!Account.accountExist(accountName, Server.allOfAccount)) {
            sendMessage = ACCOUNT_NOT_EXIST_EXCEPTION_PROMPT;
        } else if (Account.isAccountPasswordRight(accountName, password, Server.allOfAccount)) {
            Account account = Account.findAccount(accountName, Server.allOfAccount);
            this.account = account;
            Server.loginedAccount.add(account);
            sendMessage = DONE;
        } else {
            sendMessage = INVALID_PASSWORD_EXCEPTION_PROMPT;
        }
        dos.writeUTF(sendMessage);
    }

    private void signUp(String[] strings) throws IOException {
        String accountName = strings[1];
        String password = strings[2];

        String sendMessage = "";
        if (!Account.accountExist(accountName, Server.allOfAccount)) {

            Account account = new Account(accountName, password);
            Server.allOfAccount.add(account);
            sendMessage = DONE;
        } else {
            sendMessage = ACCOUNT_EXIST_EXCEPTION_PROMPT;
        }
        dos.writeUTF(sendMessage);

    }

}
