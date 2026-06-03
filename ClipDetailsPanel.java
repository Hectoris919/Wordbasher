// Displays metadata for the currently selected audio clip.
// Includes clip name, category, duration, tags, notes, and editing controls for managing clip information.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ClipDetailsPanel extends VBox {

    public ClipDetailsPanel() {
        setSpacing(10);
        setPadding(new Insets(10));
        setPrefWidth(260);

        Label title = new Label("Clip Details");

        TextField clipName = new TextField();
        clipName.setPromptText("Clip Name");

        TextField category = new TextField();
        category.setPromptText("Category");

        TextField duration = new TextField();
        duration.setPromptText("Duration");

        TextArea tags = new TextArea();
        tags.setPromptText("Tags / Metadata");
        tags.setPrefHeight(120);

        TextArea notes = new TextArea();
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
}