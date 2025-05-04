package lk.ijse;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * ------------------------------------------------
 * Author: Sherul Fdo
 * GitHub: https://github.com/sherulfernando2000
 * Created: 5/2/2025 3:40 AM
 * Project: multiple_client_chat
 * ------------------------------------------------
 */
public class Server {
    private ServerSocket serverSocket;

    public Server(ServerSocket serverSocket) {
        this.serverSocket = serverSocket;
    }

    public void startServer(){
        try{
            while (!serverSocket.isClosed()){
                Socket socket = serverSocket.accept();  //blocking method program stop and waiting for client to connect
                System.out.println("A new client has connected");
                ClientHandler clientHandler = new ClientHandler(socket);

                //thread is a sequence of instruction within a program that can be executed indpendently of another code
                new Thread(clientHandler).start(); //start the thread

            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void closeServer(){
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket1 = new ServerSocket(1234);
        Server server = new Server(serverSocket1);
        server.startServer();
    }
}
