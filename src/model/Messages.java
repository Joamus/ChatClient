package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.List;

/**
 * Created by Joakim on 14/09/2017.
 */
public class Messages {
   static ObservableList<String> messages;

    public static ObservableList<String> getMessages() {
        if (messages == null) {
            messages = FXCollections.observableArrayList();
        }
        return messages;
    }
    public static void addMessage(String message) {
        messages.add(message);
    }

}
