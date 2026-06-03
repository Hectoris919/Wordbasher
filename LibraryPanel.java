// Displays audio clip categories and the clip library.
// Allows users to browse, search, and select audio snippets that can be added to projects or the wordbashing timeline.

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

public class LibraryPanel extends VBox {

    public LibraryPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(260);

        Label categoryLabel = new Label("Categories");

        ListView<String> categories = new ListView<>();
        categories.getItems().addAll(
                "All Clips",
                "Greetings",
                "Names",
                "Laughs",
                "Actions",
                "Misc"
        );
        categories.setPrefHeight(160);

        Label libraryLabel = new Label("Clip Library");

        ListView<String> clipLibrary = new ListView<>();
        clipLibrary.getItems().addAll(
                "hello.wav",
                "yes.wav",
                "no.wav",
                "laugh.wav",
                "greeting.wav",
                "character_i.wav",
                "character_love.wav",
                "character_eggs.wav"
        );

        getChildren().addAll(
                categoryLabel,
                categories,
                libraryLabel,
                clipLibrary
        );
    }
}