package lk.ijse;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

/**
 * ------------------------------------------------
 * Author: Sherul Fdo
 * GitHub: https://github.com/sherulfernando2000
 * Created: 5/2/2025 3:53 AM
 * Project: multiple_client_chat
 * ------------------------------------------------
 */
public class ClientHandler implements Runnable{
    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>(); //responsible for broadcast message
    private Socket socket;
    private BufferedReader bufferedReader; //buffer is a temporary storage area.read client data
    private BufferedWriter bufferedWriter; //write client data
    private String clientUsername; //client name

    public ClientHandler(Socket socket) {

        try {
            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())); //BufferedWriter - character stream , OutputStreamWriter - character stream,  getOutputStream - byte stream
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream())); //BufferedReader - character stream , InputStreamReader - character stream,  getInputStream - byte stream
            this.clientUsername = bufferedReader.readLine(); //user namak input karala enter karapu gaman initialize wenna
            clientHandlers.add(this); //add the client to the list
            broadcastMessage("SERVER: " + clientUsername + " has joined the chat!"); //broadcast message to all clients

        } catch (Exception e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
            e.printStackTrace();
            throw new RuntimeException(e);

        }
    }

    @Override
    public void run() {
        String messageFromClient;

        while (socket.isConnected()) { //while the socket is connected
            try {
                messageFromClient = bufferedReader.readLine(); //reads text until it finds a newline. So without this line, the receiving side might wait forever for the end of the message.
                broadcastMessage(messageFromClient); //broadcast the message to all clients

            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
                break;
            }
        }
    }


    public void broadcastMessage(String messageClient) {
        for (ClientHandler clientHandler : clientHandlers) { //for each client in the list
            try {
                if (!clientHandler.clientUsername.equals(clientUsername)) { //if the client is not the same as the sender
                    clientHandler.bufferedWriter.write(messageClient); //write the message to the client
                    clientHandler.bufferedWriter.newLine(); //add a new line Hello, world!\n  bufferedReader.readLine(); It will wait (block) until it sees a newline character.
                    clientHandler.bufferedWriter.flush(); //flush the buffer
                }
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }

    }

    public void removeClientHandler(){
        clientHandlers.remove(this); //remove the client from the list
        broadcastMessage("SERVER: " + clientUsername + " has left the chat!"); //broadcast message to all clients
    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        removeClientHandler(); //remove the client from the list
        try {
            if (bufferedReader != null) {
                bufferedReader.close(); //close the reader
            }
            if (bufferedWriter != null) {
                bufferedWriter.close(); //close the writer
            }
            if (socket != null) {
                socket.close(); //close the socket
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
