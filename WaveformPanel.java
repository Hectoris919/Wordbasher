// Provides the audio processing workspace.
// Displays waveform information and contains controls for trimming, editing, and modifying audio clips.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class WaveformPanel extends VBox {

    private Label selectedClip;

    public WaveformPanel() {
        setSpacing(10);

        Label title = new Label("Audio Processing / Trim Editor");

        selectedClip = new Label("Selected Clip: none");

        Pane waveformPane = new Pane();
        waveformPane.setPrefHeight(180);
        waveformPane.setStyle(
                "-fx-border-color: black;" +
                "-fx-background-color: #eeeeee;"
        );

        Label placeholder = new Label("Waveform Display Placeholder");
        placeholder.setLayoutX(20);
        placeholder.setLayoutY(75);
        waveformPane.getChildren().add(placeholder);

        TextField trimStart = new TextField();
        trimStart.setPromptText("Trim Start");

        TextField trimEnd = new TextField();
        trimEnd.setPromptText("Trim End");

        Button applyTrim = new Button("Apply Trim");
        Button fadeIn = new Button("Fade In");
        Button fadeOut = new Button("Fade Out");
        Button normalize = new Button("Normalize");
        Button reverse = new Button("Reverse");

        HBox trimControls = new HBox(
                10,
                trimStart,
                trimEnd,
                applyTrim,
                fadeIn,
                fadeOut,
                normalize,
                reverse
        );

        trimControls.setPadding(new Insets(5, 0, 0, 0));

        getChildren().addAll(
                title,
                selectedClip,
                waveformPane,
                trimControls
        );
    }

    public void loadClip(AudioClip clip) {
        if (clip == null) {
            selectedClip.setText("Selected Clip: none");
            return;
        }

        selectedClip.setText("Selected Clip: " + clip.getFileName());
    }
}
