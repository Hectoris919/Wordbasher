// Displays audio clip categories and the clip library.
// Allows users to browse, search, and select audio snippets
// that can be added to projects or the wordbashing timeline.

import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.VBox;
import java.util.ArrayList;


public class LibraryPanel extends VBox {

    private ListView<String> categories;
    private ListView<AudioClip> clipLibrary;
    private ArrayList<AudioClip> allClips;

    public LibraryPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(260);

        Label categoryLabel = new Label("Categories");

        categories = new ListView<>();
        categories.getItems().addAll(
                "All Clips",
                "Greetings",
                "Names",
                "Laughs",
                "Actions",
                "Responses",
                "Misc"
        );
        categories.setPrefHeight(160);

        Label libraryLabel = new Label("Clip Library");

        allClips = new ArrayList<>();
        clipLibrary = new ListView<>();
        clipLibrary.getItems().addAll(
                new AudioClip("hello.wav", "/audio/hello.wav", "Greetings", "hello", 1.2),
                new AudioClip("yes.wav", "/audio/yes.wav", "Responses", "yes", 0.8),
                new AudioClip("no.wav", "/audio/no.wav", "Responses", "no", 0.7),
                new AudioClip("laugh.wav", "/audio/laugh.wav", "Laughs", "laugh", 1.5),
                new AudioClip("greeting.wav", "/audio/greeting.wav", "Greetings", "greeting", 2.0),
                new AudioClip("character_i.wav", "/audio/character_i.wav", "Names", "i", 0.5),
                new AudioClip("character_love.wav", "/audio/character_love.wav", "Actions", "love", 0.9),
                new AudioClip("character_eggs.wav", "/audio/character_eggs.wav", "Misc", "eggs", 1.0)
        );
        allClips.addAll(clipLibrary.getItems());

        clipLibrary.setOnDragDetected(event -> {
            AudioClip selectedClip = getSelectedClip();

            if (selectedClip != null) {
                Dragboard dragboard = clipLibrary.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();
                content.putString(selectedClip.getFileName());

                dragboard.setContent(content);
                event.consume();
            }
        });

        getChildren().addAll(
                categoryLabel,
                categories,
                libraryLabel,
                clipLibrary
        );
    }

    public AudioClip getSelectedClip() {
        return clipLibrary.getSelectionModel().getSelectedItem();
    }

    public ListView<AudioClip> getClipLibrary() {
        return clipLibrary;
    }

    public ListView<String> getCategories() {
        return categories;
    }

    public void addClip(AudioClip clip) {
        if (clip == null) {
            return;
        }
        clipLibrary.getItems().add(clip);
        allClips.add(clip);
    }

    public ArrayList<AudioClip> getAllClips() {
        return allClips;
    }

    public void refreshLibrary(ArrayList<AudioClip> clips) {
        clipLibrary.getItems().setAll(clips);
    }
}
