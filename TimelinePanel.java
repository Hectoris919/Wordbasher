// Represents the wordbashing timeline.
// Allows audio clips to be arranged into multiple channels to build phrases, sentences, and custom audio sequences.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class TimelinePanel extends VBox {

    private VBox timelineTracks;
    private ArrayList<HBox> channels;

    public TimelinePanel() {
        setSpacing(10);

        Label title = new Label("Wordbashing Timeline");

        HBox timeRuler = new HBox(
                55,
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

        channels = new ArrayList<>();

        addChannel();
        addChannel();
        addChannel();
        addChannel();

        Button addChannelButton = new Button("Add Channel");
        Button clearTimelineButton = new Button("Clear Timeline");

        addChannelButton.setOnAction(e -> addChannel());

        clearTimelineButton.setOnAction(e -> clearTimeline());

        HBox controls = new HBox(
                10,
                addChannelButton,
                clearTimelineButton
        );

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

        track.getChildren().add(label);

        return track;
    }

    public void addChannel() {
        int channelNumber = channels.size() + 1;

        HBox newChannel = createTrack(
                "Channel " + channelNumber
        );

        channels.add(newChannel);
        timelineTracks.getChildren().add(newChannel);
    }

    public void addClipToTimeline(AudioClip clip, int channelNumber) {
        if (clip == null) {
            return;
        }

        if (channelNumber < 1 || channelNumber > channels.size()) {
            channelNumber = 1;
        }

        Button clipButton = new Button(clip.getFileName());

        channels.get(channelNumber - 1)
                .getChildren()
                .add(clipButton);
    }

    public void clearTimeline() {
        timelineTracks.getChildren().clear();
        channels.clear();

        addChannel();
        addChannel();
        addChannel();
        addChannel();
    }

    public int getChannelCount() {
        return channels.size();
    }
}
