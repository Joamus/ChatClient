package sample;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

/**
 * Created by Joakim on 14/09/2017.
 */
public class ViewController {

    void addTextToChat(ObservableList<String> textArea, String message) {
        textArea.add(message);

    }
}
