// Represents the wordbashing timeline.
// Allows audio clips to be arranged into multiple channels to build phrases, sentences, and custom audio sequences.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;

public class TimelinePanel extends VBox {

    private VBox timelineTracks;

    public TimelinePanel() {
        setSpacing(10);

        Label title = new Label("Wordbashing Timeline");

        HBox timeRuler = new HBox(55,
                new Label("0:00"),
                new Label("0:01"),
                new Label("0:02"),
                new Label("0:03"),
                new Label("0:04"),
                new Label("0:05")
        );

        timelineTracks = new VBox(8);
        timelineTracks.setPadding(new Insets(10));
        timelineTracks.setStyle(
                "-fx-border-color: black;" +
                "-fx-background-color: #f8f8f8;"
        );

        timelineTracks.getChildren().addAll(
                createTrack("Channel 1"),
                createTrack("Channel 2"),
                createTrack("Channel 3"),
                createTrack("Channel 4")
        );

        Button addChannel = new Button("Add Channel");
        Button removeClip = new Button("Remove Clip");
        Button clearTimeline = new Button("Clear Timeline");
        Button previewPhrase = new Button("Preview Phrase");

        addChannel.setOnAction(e -> {
            int nextChannel = timelineTracks.getChildren().size() + 1;
            timelineTracks.getChildren().add(createTrack("Channel " + nextChannel));
        });

        clearTimeline.setOnAction(e -> {
            timelineTracks.getChildren().clear();
            timelineTracks.getChildren().addAll(
                    createTrack("Channel 1"),
                    createTrack("Channel 2"),
                    createTrack("Channel 3"),
                    createTrack("Channel 4")
            );
        });

        HBox controls = new HBox(10, addChannel, removeClip, clearTimeline, previewPhrase);

        getChildren().addAll(
                title,
                timeRuler,
                timelineTracks,
                controls
        );
    }

    private HBox createTrack(String trackName) {
        HBox track = new HBox(10);
        track.setPadding(new Insets(8));
        track.setMinHeight(60);
        track.setStyle(
                "-fx-border-color: gray;" +
                "-fx-background-color: white;"
        );

        Label label = new Label(trackName);
        label.setMinWidth(100);

        Label sampleClip = new Label("[ empty track ]");
        sampleClip.setStyle("-fx-text-fill: gray;");

        track.getChildren().addAll(label, sampleClip);

        return track;
    }
}