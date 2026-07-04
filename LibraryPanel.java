// Displays audio categories and the clip library for selecting, importing, searching, and dragging clips.

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

        // Sets spacing, padding, and width for the library panel.
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(260);

        // Creates the category section label.
        Label categoryLabel = new Label("Categories");

        // Creates the category list used for visual organization.
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

        // Creates the clip library section label.
        Label libraryLabel = new Label("Clip Library");

        // Stores every clip so search filtering does not permanently remove clips.
        allClips = new ArrayList<>();

        // Creates the visible clip library list.
        clipLibrary = new ListView<>();

        // Adds sample clips for testing the interface before real imports are used.
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

        // Copies the starting clips into the master list.
        allClips.addAll(clipLibrary.getItems());

        // Enables dragging a selected AudioClip from the library to the timeline.
        clipLibrary.setOnDragDetected(event -> {
            AudioClip selectedClip = getSelectedClip();

            if (selectedClip != null) {
                Dragboard dragboard = clipLibrary.startDragAndDrop(TransferMode.COPY);

                ClipboardContent content = new ClipboardContent();

                // Stores the file path so the timeline can find the full AudioClip later.
                content.putString(selectedClip.getFilePath());

                dragboard.setContent(content);
                event.consume();
            }
        });

        // Adds all visual elements to the library panel.
        getChildren().addAll(
                categoryLabel,
                categories,
                libraryLabel,
                clipLibrary
        );
    }

    // Returns the currently selected audio clip from the library.
    public AudioClip getSelectedClip() {
        return clipLibrary.getSelectionModel().getSelectedItem();
    }

    // Returns the visible clip library list so other classes can observe or update it.
    public ListView<AudioClip> getClipLibrary() {
        return clipLibrary;
    }

    // Returns the category list used by the library panel.
    public ListView<String> getCategories() {
        return categories;
    }

    // Adds a new clip to both the visible library and the master clip list.
    public void addClip(AudioClip clip) {
        if (clip == null) {
            return;
        }

        clipLibrary.getItems().add(clip);
        allClips.add(clip);
    }

    // Returns all clips stored in the library, including clips hidden by search filtering.
    public ArrayList<AudioClip> getAllClips() {
        return allClips;
    }

    // Updates the visible library without changing the master clip list.
    public void refreshLibrary(ArrayList<AudioClip> clips) {
        clipLibrary.getItems().setAll(clips);
    }

    // Replaces the full library after loading a saved project.
    public void setAllClips(ArrayList<AudioClip> clips) {
        allClips.clear();
        clipLibrary.getItems().clear();

        if (clips != null) {
            allClips.addAll(clips);
            clipLibrary.getItems().addAll(clips);
        }
    }

    // Finds a clip by file path so drag-and-drop can restore the complete AudioClip object.
    public AudioClip findClipByPath(String filePath) {
        if (filePath == null) {
            return null;
        }

        for (AudioClip clip : allClips) {
            if (filePath.equals(clip.getFilePath())) {
                return clip;
            }
        }

        return null;
    }
}
