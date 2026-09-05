package ari.gui;

import java.io.IOException;
import java.util.Collections;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

/**
 * Displays one chat message beside an image representing its sender.
 */
public class DialogBox extends HBox {
    @FXML
    private Label dialog;
    @FXML
    private ImageView displayPicture;

    private DialogBox(String text, Image image) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(
                    MainWindow.class.getResource("/view/DialogBox.fxml")
            );
            fxmlLoader.setController(this);
            fxmlLoader.setRoot(this);
            fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        this.dialog.setText(text);
        this.displayPicture.setImage(image);
    }

    /**
     * Creates a dialog with the user's image on the right.
     *
     * @param text Message entered by the user.
     * @param image Image representing the user.
     * @return Dialog configured for a user message.
     */
    public static DialogBox getUserDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.getStyleClass().add("user-dialog");
        return dialogBox;
    }

    /**
     * Creates a dialog with Ari's image on the left.
     *
     * @param text Message returned by Ari.
     * @param image Image representing Ari.
     * @return Dialog configured for Ari's message.
     */
    public static DialogBox getAriDialog(String text, Image image) {
        DialogBox dialogBox = new DialogBox(text, image);
        dialogBox.flip();
        dialogBox.getStyleClass().add("ari-dialog");
        return dialogBox;
    }

    /**
     * Reverses the child order so the sender image appears on the left.
     */
    private void flip() {
        ObservableList<Node> children = FXCollections.observableArrayList(this.getChildren());
        Collections.reverse(children);
        this.getChildren().setAll(children);
        this.setAlignment(Pos.TOP_LEFT);
        this.dialog.getStyleClass().add("ari-reply");
    }
}
