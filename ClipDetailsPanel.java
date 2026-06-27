// Displays metadata for the currently selected audio clip.
// Includes clip name, category, duration, tags, notes,
// and editing controls for managing clip information.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ClipDetailsPanel extends VBox {

    private TextField clipName;
    private TextField category;
    private TextField duration;
    private TextArea tags;
    private TextArea notes;

    public ClipDetailsPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(260);

        Label title = new Label("Clip Details");

        clipName = new TextField();
        clipName.setPromptText("Clip Name");

        category = new TextField();
        category.setPromptText("Category");

        duration = new TextField();
        duration.setPromptText("Duration");

        tags = new TextArea();
        tags.setPromptText("Tags / Metadata");
        tags.setPrefHeight(120);

        notes = new TextArea();
        notes.setPromptText("Notes");
        notes.setPrefHeight(120);

        Button editTags = new Button("Edit Tags");
        Button updateClip = new Button("Update Clip");

        getChildren().addAll(
                title,
                clipName,
                category,
                duration,
                tags,
                notes,
                editTags,
                updateClip
        );
    }

    public void displayClip(AudioClip clip) {
        if (clip == null) {
            clearFields();
            return;
        }

        clipName.setText(clip.getFileName());
        category.setText(clip.getCategory());
        duration.setText(String.valueOf(clip.getDuration()));
        tags.setText(clip.getTags());
    }

    public void updateClip(AudioClip clip) {
        if (clip == null) {
            return;
        }

        clip.setFileName(clipName.getText());
        clip.setCategory(category.getText());
        clip.setTags(tags.getText());

        try {
            clip.setDuration(Double.parseDouble(duration.getText()));
        } catch (NumberFormatException e) {
            System.out.println("Invalid duration entered.");
        }
    }

    public void clearFields() {
        clipName.clear();
        category.clear();
        duration.clear();
        tags.clear();
        notes.clear();
    }
}
