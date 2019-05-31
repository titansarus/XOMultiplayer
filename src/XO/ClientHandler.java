package XO;

import XO.Model.Account;
import XO.Model.Game;

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
    public boolean isQuit = false;
    public boolean isSummonedToGame = false;
    public long summonedGameUID = 0;

    public ClientHandler(DataOutputStream dos, DataInputStream dis, Server server, Socket socket, long UID) {
        this.dos = dos;
        this.dis = dis;
        this.server = server;
    }


    @Override
    public void run() {
        while (!isQuit) {
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
            } else if (strings[0].equals(QUIT)) {
                quit(strings[1]);
            } else if (strings[0].equals(ENTER_GAME)) {
                enterGame(strings);
            } else if (strings[0].equals(SUMMONED)) {
                summonCheck();
            } else if (strings[0].equals(GIVE_INITIAL_MY_GAMEINFO)) {
                giveInitialGameInfo();
            } else if (strings[0].equals(GIVE_COMPLETE_GAME_INFO)) {
                completeGameInfo();
            } else if (strings[0].equals(INSERT)) {
                insert(strings);
            } else if (strings[0].equals(UNDO)) {
                undo();
            } else if (strings[0].equals(WIN_CHECK)) {
                winCheck();
            } else if (strings[0].equals(GIVE_ALL_ACCOUNT_INFO)) {
                giveAllAccountsInfo();
            }
        }
    }

    private void giveAllAccountsInfo() throws IOException {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < Server.allOfAccount.size(); i++) {
            Account account = Server.allOfAccount.get(i);
            if (account != null) {
                sb.append(account.getUsername()).append(" ").append(account.getWins()).append(" ").append(account.getLoses()).append(" ").append(account.getDraws()).append(" , ");
            }
        }
        dos.writeUTF(sb.toString());
    }

    private void winCheck() throws IOException {
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);
        String out = NO_WINNER;
        if (game != null) {
            Account account = game.findWinner();
            if (account != null) {
                if (account.getUsername().equals(this.account.getUsername())) {
                    out = YOU_WIN;
                    account.incrementWins();
                    summonedGameUID = 0;
                } else {
                    out = YOU_LOSE;
                    account.incrementLoses();
                    summonedGameUID = 0;
                }
            }
        }
        dos.writeUTF(out);
    }

    private void undo() throws IOException {
        String out = "";
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);
        if (game != null) {
            if (game.isUndoable()) {
                game.undo();
                out = DONE;
            } else {
                out = INVALID_UNDO_PROMPT;
            }
        } else {
            out = NO_GAME;
        }
        dos.writeUTF(out);
    }

    private void insert(String[] strings) throws IOException {
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);
        String out = "";
        if (game != null) {
            int i = Integer.parseInt(strings[1]);
            int j = Integer.parseInt(strings[2]);

            if (game.getGrid()[i][j] != 0) {
                out = BLOCK_IS_NOT_EMPTY_PROMPT;
            } else {
                String accountName = strings[3];
                game.insert(i, j, accountName);
                out = DONE;
            }

        } else {
            out = NO_GAME;
        }
        dos.writeUTF(out);
    }

    private void completeGameInfo() throws IOException {
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);
        String out = "";
        if (game != null) {
            out = game.getRow() + " " + game.getColumn();
            out = out + " " + game.getPlayer1().getUsername() + " " + game.getPlayer2().getUsername();
            out = out + " " + game.getTurnAccount().getUsername() + " , ";
            for (int i = 0; i < game.getRow(); i++) {
                for (int j = 0; j < game.getColumn(); j++) {
                    out = out + game.getGrid()[i][j] + " ";
                }
            }
        } else {
            out = NO_GAME;
        }
        dos.writeUTF(out);
    }

    private void giveInitialGameInfo() throws IOException {
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);
        String out = NO_GAME;
        if (game != null) {
            out = game.getRow() + " " + game.getColumn();
            this.isSummonedToGame = false;
        }
        dos.writeUTF(out);
    }

    private void summonCheck() throws IOException {
        if (isSummonedToGame) {
            dos.writeUTF(SUMMONED_TO_GAME);
        } else {
            dos.writeUTF(NOT_SUMMONED_TO_GAME);
        }
    }

    private void enterGame(String[] strings) throws IOException {
        String username = strings[1];
        int row = Integer.parseInt(strings[2]);
        int column = Integer.parseInt(strings[3]);
        String sendMessage = "";

        if (!Account.accountExist(username, Server.allOfAccount)) {
            //TODO CHECK IF PLAYER IS PLAYING WITH ANOTHER ONE
            //TODO CHECK IF PLAYER IS PLAYING WITH ANOTHER ONE
            //TODO WHEN PLAYER IS PLAYING, HE MUST BE ADDED TO THE PLAYINGACCOUNTS
            sendMessage = ACCOUNT_NOT_EXIST_EXCEPTION_PROMPT;
        } else if (row < 3 || row > 10 || column < 3 || column > 10) {
            sendMessage = INVALID_ROW_COL_NUMBER_PROMPT;
        } else {
            sendMessage = DONE;
        }

        dos.writeUTF(sendMessage);

        if (sendMessage.equals(DONE)) {
            ClientHandler clientHandler = findClientHandler(username);
            if (clientHandler != null) {
                this.isSummonedToGame = true;
                this.summonedGameUID = Server.gameUID;
                clientHandler.isSummonedToGame = true;
                clientHandler.summonedGameUID = Server.gameUID;
                Game game = new Game(this.account, Account.findAccount(username, Server.loginedAccount), row, column, Server.gameUID++);
                Server.runningGames.add(game);
            }

        }
    }

    private void quit(String string) {
        Account.deleteAccount(string, Server.loginedAccount);
        Server.clientHandlers.remove(this);
        isQuit = true;
    }

    private void loginedUser() throws IOException {
        if (this.account == null) {
            dos.writeUTF(NO_USER_LOGINED);
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
            if (Account.accountExist(accountName, Server.loginedAccount)) {
                sendMessage = USER_ALREADY_LOGINED_PROMPT;
            } else {
                Account account = Account.findAccount(accountName, Server.allOfAccount);
                this.account = account;
                Server.loginedAccount.add(account);
                sendMessage = DONE;
            }
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

    public static ClientHandler findClientHandler(String username) {
        for (int i = 0; i < Server.clientHandlers.size(); i++) {
            ClientHandler clientHandler = Server.clientHandlers.get(i);
            if (clientHandler != null && clientHandler.account != null && clientHandler.account.getUsername().equals(username)) {
                return clientHandler;
            }
        }
        return null;
    }
}
