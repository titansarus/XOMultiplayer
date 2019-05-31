package XO;

import XO.Model.Account;
import XO.Model.Game;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

import static XO.Constants.*;

public class ClientHandler implements Runnable {

    private DataOutputStream dos;
    private DataInputStream dis;
    public long UID;
    private Account account = null;
    private boolean isQuit = false;
    private boolean isSummonedToGame = false;
    private long summonedGameUID = 0;
    private boolean isRunnigGamePaused = false;
    private boolean isStopped = false;

    public ClientHandler(DataOutputStream dos, DataInputStream dis, Server server, Socket socket, long UID) {
        this.dos = dos;
        this.dis = dis;
    }


    @Override
    public void run() {
        while (!isQuit) {
            try {
                String s = dis.readUTF();
                handleInput(s);
            } catch (IOException ignore) {
            }

        }

    }

    private void handleInput(String s) throws IOException {
        String[] strings = s.split("\\s");
        if (strings.length >= 1) {
            switch (strings[0]) {
                case SIGNUP:
                    signUp(strings);
                    break;
                case LOGIN:
                    login(strings);
                    break;
                case GOTO_ALL_USERS:

                    goToAllUsers();
                    break;
                case LOGINED_USER:
                    loginedUser();
                    break;
                case QUIT:
                    quit(strings[1]);
                    break;
                case ENTER_GAME:
                    enterGame(strings);
                    break;
                case SUMMONED:
                    summonCheck();
                    break;
                case GIVE_INITIAL_MY_GAMEINFO:
                    giveInitialGameInfo();
                    break;
                case GIVE_COMPLETE_GAME_INFO:
                    completeGameInfo();
                    break;
                case INSERT:
                    insert(strings);
                    break;
                case UNDO:
                    undo();
                    break;
                case WIN_CHECK:
                    winCheck();
                    break;
                case GIVE_ALL_ACCOUNT_INFO:
                    giveAllAccountsInfo();
                    break;
                case PAUSE:
                    pause();
                    break;
                case CHECK_PAUSED:
                    checkPaused();
                    break;
                case LIST_OF_PAUSED_GAMES:
                    listOfPausedGames(strings[1]);
                    break;
                case RESUME:
                    resume(strings);

                    break;
                case STOP_GAME:
                    long uid = summonedGameUID;
                    Game game = Game.findGameByUID(uid, Server.runningGames);
                    ClientHandler c1 = findClientHandler(game.getPlayer1().getUsername());
                    ClientHandler c2 = findClientHandler(game.getPlayer2().getUsername());

                    c1.isStopped = true;
                    c2.isStopped = true;


                    Server.runningGames.remove(game);
                    break;
                case IS_STOPPED:
                    String out = NOT_STOP;
                    if (isStopped) {
                        out = YES_STOP;
                        isStopped = false;
                    }
                    dos.writeUTF(out);
                    break;
            }


        }
    }

    private void resume(String[] strings) {
        Long uid = Long.parseLong(strings[1]);
        Game game = Game.findGameByUID(uid, Server.pausedGames);
        String user1 = strings[2];
        String user2 = strings[3];
        ClientHandler c1 = findClientHandler(user1);
        ClientHandler c2 = findClientHandler(user2);
        c1.isSummonedToGame = true;
        c1.summonedGameUID = uid;
        c2.isSummonedToGame = true;
        c2.summonedGameUID = uid;
        Server.pausedGames.remove(game);
    }

    private void enterGame(String[] strings) throws IOException {
        String username = strings[1];
        int row = Integer.parseInt(strings[2]);
        int column = Integer.parseInt(strings[3]);
        String sendMessage = "";
        ClientHandler c = ClientHandler.findClientHandler(username);

        if (!Account.accountExist(username, Server.allOfAccount)) {

            sendMessage = ACCOUNT_NOT_EXIST_EXCEPTION_PROMPT;
        } else if (row < 3 || row > 10 || column < 3 || column > 10) {
            sendMessage = INVALID_ROW_COL_NUMBER_PROMPT;
        } else if (c != null && c.summonedGameUID > 0) {
            sendMessage = OTHER_PLAYER_IS_PLAYING_PROMPT;
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

    private void listOfPausedGames(String string) throws IOException {
        String username = string;
        ArrayList<Game> games = Game.findListOfPausedGames(username, Server.pausedGames);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < games.size(); i++) {
            Game game = games.get(i);
            if (game != null) {
                sb.append(game.getUID()).append(" ")
                        .append(game.getPlayer1().getUsername()).append(" ").append(game.getPlayer2().getUsername())
                        .append(" ").append(game.getRow()).append(" ").append(game.getColumn()).append(" , ");
            }
        }
        dos.writeUTF(sb.toString());
    }

    private void checkPaused() throws IOException {
        String out = NOT_PAUSED;
        if (isRunnigGamePaused) {
            out = YES_PAUSED;
            isRunnigGamePaused = false;
            summonedGameUID = 0;
        }
        dos.writeUTF(out);
    }

    private void pause() {
        long uid = summonedGameUID;
        Game game = Game.findGameByUID(uid, Server.runningGames);

        ClientHandler c1 = findClientHandler(game.getPlayer1().getUsername());
        ClientHandler c2 = findClientHandler(game.getPlayer2().getUsername());
        if (c1 != null) {
            c1.isRunnigGamePaused = true;
        }
        if (c2 != null) {
            c2.isRunnigGamePaused = true;
        }
        Server.pausedGames.add(game);
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
                    //  account.incrementWins();
                    summonedGameUID = 0;
                } else if (account.equals(Game.drawAccount)) {
                    // account.incrementDraws();
                    summonedGameUID = 0;
                    out = DRAW;
                } else {
                    out = YOU_LOSE;
                    //  account.incrementLoses();
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

    private static ClientHandler findClientHandler(String username) {
        for (int i = 0; i < Server.clientHandlers.size(); i++) {
            ClientHandler clientHandler = Server.clientHandlers.get(i);
            if (clientHandler != null && clientHandler.account != null && clientHandler.account.getUsername().equals(username)) {
                return clientHandler;
            }
        }
        return null;
    }
}
