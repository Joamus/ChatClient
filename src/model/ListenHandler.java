package model;

import controller.ChatController;
import model.User;

import java.net.InetAddress;

/**
 * Created by Joakim on 14/09/2017.
 */
public class ListenHandler implements Runnable {
    User user;
    InetAddress ip;

    public ListenHandler(InetAddress ip, User user) {
        this.user = user;
        this.ip = ip;
    }

    @Override
    public void run() {
        ChatController chatController = new ChatController(ip, user);
        try {
            System.out.println("Server listening...");
            chatController.listen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
