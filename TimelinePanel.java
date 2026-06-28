// Represents the wordbashing timeline.
// Allows audio clips to be arranged into multiple channels
// to build phrases, sentences, and custom audio sequences.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
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

        HBox controls = new HBox(10);

        ScrollPane timelineScrollPane = new ScrollPane(timelineTracks);
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.setPrefHeight(260);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        timelineScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        getChildren().addAll(
                title,
                timeRuler,
                timelineScrollPane,
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

        track.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        track.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                Button clipButton = new Button(dragboard.getString());

                clipButton.setOnAction(e -> {
                    HBox parentTrack = (HBox) clipButton.getParent();
                    parentTrack.getChildren().remove(clipButton);
                });

                track.getChildren().add(clipButton);
                success = true;
            }

            event.setDropCompleted(success);
            event.consume();
        });

        return track;
    }

    public void addChannel() {
        int channelNumber = channels.size() + 1;

        HBox newChannel = createTrack("Channel " + channelNumber);

        channels.add(newChannel);
        timelineTracks.getChildren().add(newChannel);
    }

    public void removeLastChannel() {
        if (channels.size() <= 1) {
            return;
        }
            HBox lastChannel = channels.remove(channels.size() - 1);
            timelineTracks.getChildren().remove(lastChannel);
        }

    public void addClipToTimeline(AudioClip clip, int channelNumber) {
        if (clip == null) {
            return;
        }

        if (channelNumber < 1 || channelNumber > channels.size()) {
            channelNumber = 1;
        }

        Button clipButton = new Button(clip.getFileName());

        //Store the entire AudioClip object in the button's user data for later retrieval
        clipButton.setUserData(clip);

        clipButton.setOnAction(e -> {
            HBox parentTrack = (HBox) clipButton.getParent();
            parentTrack.getChildren().remove(clipButton);
        });

        channels.get(channelNumber - 1)
                .getChildren()
                .add(clipButton);
    }

    public void clearTimeline() {
        for (HBox channel : channels) {
            if (channel.getChildren().size() > 1) {
                channel.getChildren().remove(1, channel.getChildren().size());
            }
        }
    }

    public int getChannelCount() {
        return channels.size();
    }

    public ArrayList<HBox> getChannels() {
        return channels;
    }

    public ArrayList<TimelineClip> getTimelineClips() {
    ArrayList<TimelineClip> timelineClipList = new ArrayList<>();

    for (int i = 0; i < channels.size(); i++) {
        HBox channel = channels.get(i);

        for (int j = 1; j < channel.getChildren().size(); j++) {
            Button clipButton = (Button) channel.getChildren().get(j);

            AudioClip audioClip = (AudioClip) clipButton.getUserData();
            timelineClipList.add(new TimelineClip(i + 1, audioClip));
        }
    }
    return timelineClipList;
}
}
