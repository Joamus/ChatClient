package controller;

import javafx.application.Platform;
import model.ListenHandler;
import model.Messages;
import model.User;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatController  {
    User user;
    InetAddress serverIP;
    int serverPort = 4700;
    static DatagramSocket socket;

   public ChatController(InetAddress ip, User user) {
        try {
            this.serverIP = ip;
        } catch(Exception e) {

        }
        this.user = user;
    }

    public ChatController(User user) {
        this.user = user;
    }


   public void sendMessage(String message) throws IOException {
        byte[] sendData;
        String newMessage = "DATA " + user.getUserName() + ": " + message;
        sendData = newMessage.getBytes();

        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);

        socket.send(packet);

    }

    public boolean joinServer(User user) throws IOException {
        // Når vi joiner serveren, sender vi information om hvilken port vi modtager pakker med. Denne socket nedeunder er gemt i controlleren,
        // Så listen() kan få fat i den.
        socket = new DatagramSocket();

        byte[] sendData;
        String newUser = "JOIN " + user.getUserName();
        sendData = newUser.getBytes();

        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
        socket.send(packet);

        boolean alreadyExists = false;

        try {
            alreadyExists = authenticate();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (!alreadyExists) {

            Thread thread = new Thread(new ListenHandler(serverIP, user));
            thread.start();
        }
            return alreadyExists;

    }

    public void listen() throws IOException {
        byte[] receiveData = new byte[1024];
        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
        do {
            socket.receive(packet);
            String stringPacket = new String(packet.getData(), 0, packet.getLength());
            String transactionType = stringPacket.substring(0, 4);

            switch (transactionType) {
                case "DATA":
                    Platform.runLater(() -> {
                        Messages.addMessage(stringPacket.substring(5, stringPacket.length()));
                    });
                    break;
                case "LIST":
                    Platform.runLater(() ->  {
                        updateOnlineUsers(stringPacket);
                    });
                    break;

                case "ALVE":
                    try {
                        heartBeat();
                    } catch(Exception e) {
                        e.printStackTrace();
                    }
                    break;

            }

        } while (socket.isBound());
        socket.close();

    }

   public void updateOnlineUsers(String newUsers) {
        ArrayList<String> userArray;

        String usersWithoutTransaction = newUsers.substring(5, newUsers.length());

        String[] splitArray = usersWithoutTransaction.split(",");
        userArray = new ArrayList<>(Arrays.asList(splitArray));

        for (String user : userArray) {
            // Hvis det eksisterende online-bruger array ikke har de nye brugere, som er hentet fra serveren, skal de tilføjes
            if (!User.getUsersOnline().contains(user)) {
                Messages.addMessage(user + " has joined the server.");
                User.addUser(user);

            }
        }

        // Hvis der er en bruger i klientens array over online brugere, som ikke eksisterer i det nye bruger array, så skal de slettes
        for (int i = 0; i < User.getUsersOnline().size(); i++) {
            if (!userArray.contains(User.getUsersOnline().get(i))) {
                Messages.addMessage(User.getUsersOnline().get(i) + " has left the server.");
                user.removeUser(User.getUsersOnline().get(i));
            }
        }

    }


    public void heartBeat() throws Exception {
       byte[] sendData;
       String newMessage = "ALVE " + user.getUserName();
       sendData = newMessage.getBytes();

       DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);

       socket.send(packet);


    }

    public boolean authenticate() throws Exception {
       boolean alreadyExists = false;
        byte[] receiveData = new byte[1024];
        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);

        socket.receive(packet);


        String stringPacket = new String(packet.getData(), 0, packet.getLength());
        if (stringPacket.equals("J_OK")) {
            alreadyExists = false;
        } else if (stringPacket.equals("JERR")) {
            alreadyExists = true;
        }
        return alreadyExists;

    }

}
