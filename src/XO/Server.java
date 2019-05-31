package XO;

import XO.Model.Account;
import XO.Model.Game;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
    private static long lastUID = 1;

    static long gameUID = 1;

    private static Server server = new Server();
   static ArrayList<Account> allOfAccount = new ArrayList<>();

   static ArrayList<Account> loginedAccount = new ArrayList<>();

   public static ArrayList<Account> playingAccount = new ArrayList<>();

   static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();

   static ArrayList<Game> runningGames = new ArrayList<>();

   static ArrayList<Game> pausedGames = new ArrayList<>();


    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(55555);

        while (true)
        {
            Socket socket = serverSocket.accept();
            DataInputStream dis = new DataInputStream(socket.getInputStream());
            DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
            ClientHandler clientHandler = new ClientHandler(dos,dis,server , socket , lastUID++);
            clientHandlers.add(clientHandler);
            Thread thread = new Thread(clientHandler);
            thread.start();


        }


    }



}
