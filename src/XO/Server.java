package XO;

import XO.Model.Account;
import XO.Model.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import static XO.Constants.*;

public class Server {

    static long gameUID = 1;

    private static Server server = new Server();
    static ArrayList<Account> allOfAccount = new ArrayList<>();

    static ArrayList<Account> loginedAccount = new ArrayList<>();


    static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

    static ArrayList<Game> runningGames = new ArrayList<>();

    static ArrayList<Game> pausedGames = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(port);

        while (true) {
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ClientHandler clientHandler = new ClientHandler(dos, dis);
            clientHandlers.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();


        }


    }


}
