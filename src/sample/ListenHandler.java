package sample;

/**
 * Created by Joakim on 14/09/2017.
 */
public class ListenHandler implements Runnable {
    User user;

    public ListenHandler(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        ChatController chatController = new ChatController(user);
        try {
            System.out.println("Server listening...");
            chatController.listen();
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
