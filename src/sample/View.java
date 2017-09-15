package sample;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Created by Joakim on 12/09/2017.
 */
public class View {

    public void startView(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Label ip = new Label("IP Address");
        TextField ipInput = new TextField();

        Label username = new Label("Username");
        TextField usernameInput = new TextField();

        HBox ipBox = new HBox(ip, ipInput);
        HBox usernameBox = new HBox(username, usernameInput);

        Button join = new Button("Join");

        borderPane.setTop(ipBox);
        borderPane.setCenter(usernameBox);
        borderPane.setBottom(join);

        Scene s1 = new Scene(borderPane);
        primaryStage.setTitle("JIM - Joa's Instant Messenger 0.2");
        primaryStage.setScene(s1);
        primaryStage.show();


        join.setOnAction(event -> {
            User user = new User(usernameInput.getText());
            ChatController controller = new ChatController(ipInput.getText(), user);
            chatView(user, controller, primaryStage);
            try {
                controller.joinServer(user);
            } catch (Exception e) {
                System.out.println(e);
            }
        });


    }


    void chatView(User user, ChatController controller, Stage primaryStage) {

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(450,300);

            ObservableList<String> users = User.getUsersOnline();
            ListView<String> userList = new ListView<>(users);
            userList.setPrefWidth(120);
            userList.setEditable(false);

            VBox userListBox = new VBox(userList);
            borderPane.setRight(userListBox);

            ObservableList<String> messages = Messages.getMessages();
            ListView<String> textArea = new ListView<>(messages);
            textArea.setEditable(false);

            borderPane.setCenter(textArea);

            TextField chatInput = new TextField();
            chatInput.setPrefWidth(400);
            Button send = new Button("Send");
            HBox bottomBox = new HBox(chatInput, send);
            borderPane.setBottom(bottomBox);


            send.setOnAction(event ->  {
                try {
                    controller.sendMessage(chatInput.getText());
                    chatInput.setText("");
                } catch (Exception e) {
                    System.out.println(e);

                }

            });


        Scene s1 = new Scene(borderPane);
        primaryStage.setTitle("JIM - Joa's Instant Messaging");
        primaryStage.setScene(s1);
        primaryStage.show();

    }


}
