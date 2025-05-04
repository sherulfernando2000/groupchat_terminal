package lk.ijse;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

/**
 * ------------------------------------------------
 * Author: Sherul Fdo
 * GitHub: https://github.com/sherulfernando2000
 * Created: 5/2/2025 4:22 AM
 * Project: multiple_client_chat
 * ------------------------------------------------
 */
public class Client {
    private Socket socket;
    private BufferedReader bufferedReader; //read client data
    private BufferedWriter bufferedWriter; //write client data
    private String clientUsername; //client name

    public Client(Socket socket, String clientUsername) {
        try {
            this.socket = socket;
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.clientUsername = clientUsername;
        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void sendMessage(){
        try {
            bufferedWriter.write(clientUsername);
            bufferedWriter.newLine();
            bufferedWriter.flush();

            Scanner scanner = new Scanner(System.in);
            while (socket.isConnected()) {
                String messageToSend = scanner.nextLine();
                bufferedWriter.write(clientUsername + ": " + messageToSend);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }



        } catch (IOException e) {
            closeEverything(socket, bufferedReader, bufferedWriter);
        }
    }

    public void listenForMessage(){
        new Thread(() -> {                           //explanation eka palleha
            String messageFromGroupChat;
            while (socket.isConnected()) {
                try {
                    messageFromGroupChat = bufferedReader.readLine();
                    System.out.println(messageFromGroupChat);
                } catch (IOException e) {
                    closeEverything(socket, bufferedReader, bufferedWriter);
                }
            }
        }).start();
    }

    //sendtext
    //sendImage




    public void closeEverything(Socket socket, BufferedReader bufferedReader, BufferedWriter bufferedWriter) {
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();
            }
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter your name: ");
        String clientUsername = scanner.nextLine();
        Socket socket1 = new Socket("localhost", 1234);
        Client client = new Client(socket1, clientUsername);
        client.listenForMessage(); //listen for messages from the server
        client.sendMessage(); //send messages to the server
    }
}







/*
new Thread(new Runnable() {
    @Override
    public void run() {
        String messageFromGroupChat;
        while (socket.isConnected()) {
            try {
                messageFromGroupChat = bufferedReader.readLine();
                System.out.println(messageFromGroupChat);
            } catch (IOException e) {
                closeEverything(socket, bufferedReader, bufferedWriter);
            }
        }
    }
}).start();
*/

/*new Runnable() {
    @Override
    public void run() {
        // Code to execute in the thread
    }
}
This does two things at once:

Implements the Runnable interface anonymously (without naming a new class).

Instantiates an object from that implementation.

🧠 Behind the Scenes:
Java internally does this:


class MyRunnable implements Runnable {
    @Override
    public void run() {
        // your code
    }
}

Runnable obj = new MyRunnable(); // create object*/