package view;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.scene.input.KeyEvent;
import model.Messages;
import model.User;
import controller.ChatController;
import java.net.InetAddress;
import static javafx.scene.input.KeyCode.ENTER;

/**
 * Created by Joakim on 12/09/2017.
 */
public class View {

    public View() {

    }

    public void startView(Stage primaryStage) throws Exception {
        BorderPane borderPane = new BorderPane();

        Label ip = new Label("IP Address");
        TextField ipInput = new TextField("127.0.0.1");

        Label username = new Label("Username");
        TextField usernameInput = new TextField();

        HBox ipBox = new HBox(ip, ipInput);
        HBox usernameBox = new HBox(username, usernameInput);

        Button join = new Button("Join");
        Label errorLabel = new Label();

        HBox bottomBox = new HBox(join, errorLabel);




        borderPane.setTop(ipBox);
        borderPane.setCenter(usernameBox);
        borderPane.setBottom(bottomBox);



        Scene s1 = new Scene(borderPane);
        primaryStage.setTitle("JIM - Joa's Instant Messenger 0.2");
        primaryStage.setScene(s1);
        primaryStage.show();

        borderPane.setOnKeyPressed(KeyEvent -> {
            if (KeyEvent.getCode() == ENTER) {
                join.fire();
            }
        });


        join.setOnAction(event -> {
            try {
            User user = new User(usernameInput.getText());
            ChatController controller = new ChatController(InetAddress.getByName(ipInput.getText()), user);
            boolean alreadyExists = controller.joinServer(user);
            if (!alreadyExists) {
                chatView(user, controller, primaryStage);
            } else if (alreadyExists) {
                errorLabel.setText("Error");
               // System.out.println("Error");
            }

            } catch (Exception e) {
                System.out.println(e);
            }
        });


    }


    void chatView(User user, ChatController controller, Stage primaryStage) {

        BorderPane borderPane = new BorderPane();
        borderPane.setPrefSize(450,300);

            ListView<String> userList = new ListView<>(User.getUsersOnline());
            userList.setPrefWidth(120);
            userList.setEditable(false);

            VBox userListBox = new VBox(userList);
            borderPane.setRight(userListBox);

            ListView<String> textArea = new ListView<>(Messages.getMessages());
            textArea.setEditable(false);

            borderPane.setCenter(textArea);

            TextField chatInput = new TextField();
            chatInput.setPrefWidth(400);
            Button send = new Button("Send");
            HBox bottomBox = new HBox(chatInput, send);
            borderPane.setBottom(bottomBox);


            borderPane.setOnKeyPressed(KeyEvent -> {
                if (KeyEvent.getCode() == ENTER) {
                    send.fire();

                }

            });

            send.setOnAction(event ->  {
                try {
                    controller.sendMessage(chatInput.getText());
                    chatInput.setText("");
                } catch (Exception e) {
                    System.out.println(e);

                }

            });


        Scene s1 = new Scene(borderPane);
        primaryStage.setTitle("JIM - Joa's Instant Messenger");
        primaryStage.setScene(s1);
        primaryStage.show();

    }


}
