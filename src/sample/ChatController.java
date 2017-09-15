package sample;

import javafx.application.Platform;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;

public class ChatController {
    User user;
    InetAddress serverIP;
    int serverPort = 4700;
    static DatagramSocket listenSocket;

    ChatController(String ip, User user) {
        try {
            this.serverIP = InetAddress.getByName(ip);
        } catch(Exception e) {

        }
        this.user = user;
    }

    ChatController(User user) {
        this.user = user;
    }


    void sendMessage(String message) throws Exception {
        byte[] sendData = new byte[1024];
        String newMessage = "DATA " + user.getUserName() + ": " + message;
        sendData = newMessage.getBytes();

        DatagramSocket socket = new DatagramSocket();
        DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);

        socket.send(packet);

        socket.close();

    }

    void joinServer(User user) throws Exception {
        // Når vi joiner serveren, sender vi information om hvilken port vi modtager pakker med. Denne socket nedeunder er gemt i controlleren,
        // Så listen() kan få fat i den.
        listenSocket = new DatagramSocket();

        byte[] sendData;
        String newUser = "JOIN " + listenSocket.getLocalPort() + ";" + user.getUserName();
        sendData = newUser.getBytes();

            DatagramSocket socket = new DatagramSocket();
            DatagramPacket packet = new DatagramPacket(sendData, sendData.length, serverIP, serverPort);
            socket.send(packet);
            socket.close();
            Thread thread = new Thread(new ListenHandler(user));
            thread.start();

    }

    void listen() throws Exception {
        System.out.println(listenSocket.getLocalPort());
        byte[] receiveData = new byte[1024];
        DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);
        do {
            listenSocket.receive(packet);
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
            }

        } while (listenSocket.isBound());
        listenSocket.close();

    }

    void updateOnlineUsers(String newUsers) {
        ArrayList<String> userArray;

        String usersWithoutTransaction = newUsers.substring(5, newUsers.length());

        String[] splitArray = usersWithoutTransaction.split(",");
        userArray = new ArrayList<>(Arrays.asList(splitArray));
        System.out.println(userArray);

        for (String user : userArray) {
            if (!User.getUsersOnline().contains(user)) {
                User.addUser(user);
            }
        }


    }

}
