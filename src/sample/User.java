package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Created by Joakim on 12/09/2017.
 */
public class User {
    static ObservableList<String> usersOnline;
    String userName;

    public User(String userName) {
        this.userName = userName;
    }

    public static ObservableList<String> getUsersOnline() {
        if (usersOnline == null) {
            usersOnline = FXCollections.observableArrayList();
            return usersOnline;
        } else {
            return usersOnline;
        }
    }

    public static void addUser(String user) {
        usersOnline.add(user);
    }

    public static void removeUser(String user) {
        usersOnline.remove(user);
    }

    public String getUserName() {
        return userName;
    }

}
