// Provides the audio processing workspace.
// Displays waveform information and contains controls for trimming, editing, and modifying audio clips.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Rectangle;
import java.util.Random;

public class WaveformPanel extends VBox {

    private Label selectedClip;
    private Pane waveformPane;

    public WaveformPanel() {
        setSpacing(10);

        Label title = new Label("Audio Processing / Trim Editor");

        selectedClip = new Label("Selected Clip: none");

        waveformPane = new Pane();
        waveformPane.setPrefHeight(180);
        waveformPane.setStyle(
                "-fx-border-color: black;" +
                "-fx-background-color: #eeeeee;"
        );

        drawEmptyWaveform();

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
            drawEmptyWaveform();
            return;
        }

        selectedClip.setText("Selected Clip: " + clip.getFileName());
        drawWaveform(clip);
    }

    private void drawEmptyWaveform() {
        waveformPane.getChildren().clear();

        Label placeholder = new Label("No clip selected");
        placeholder.setLayoutX(20);
        placeholder.setLayoutY(75);

        waveformPane.getChildren().add(placeholder);
    }

    private void drawWaveform(AudioClip clip) {
        waveformPane.getChildren().clear();

        Random random = new Random(clip.getFileName().hashCode());

        double paneHeight = 180;
        double centerY = paneHeight / 2;
        double barWidth = 4;
        double spacing = 3;

        for (int i = 0; i < 120; i++) {
            double height = 20 + random.nextDouble() * 120;

            Rectangle bar = new Rectangle(
                    20 + i * (barWidth + spacing),
                    centerY - height / 2,
                    barWidth,
                    height
            );

            waveformPane.getChildren().add(bar);
        }
    }
}
