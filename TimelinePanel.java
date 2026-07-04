// Represents the wordbashing timeline where clips can be arranged into multiple audio channels.

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.input.Dragboard;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.*;
import java.util.ArrayList;

public class TimelinePanel extends VBox {

    private VBox timelineTracks;
    private ArrayList<HBox> channels;
    private LibraryPanel libraryPanel;

    public TimelinePanel(LibraryPanel libraryPanel) {

        // Stores the library reference so dropped clips can be matched to full AudioClip objects.
        this.libraryPanel = libraryPanel;

        // Sets spacing for the timeline panel layout.
        setSpacing(10);

        // Creates the timeline title.
        Label title = new Label("Wordbashing Timeline");

        // Creates a simple timeline ruler for visual timing reference.
        HBox timeRuler = new HBox(
                55,
                new Label("0:00"),
                new Label("0:01"),
                new Label("0:02"),
                new Label("0:03"),
                new Label("0:04"),
                new Label("0:05")
        );

        // Creates the vertical container that holds all timeline channels.
        timelineTracks = new VBox(8);
        timelineTracks.setPadding(new Insets(10));
        timelineTracks.setStyle(
                "-fx-border-color: black;" +
                "-fx-background-color: #f8f8f8;"
        );

        // Stores all timeline channels so channels can be added, removed, and searched dynamically.
        channels = new ArrayList<>();

        // Creates the default starting channels.
        addChannel();
        addChannel();
        addChannel();
        addChannel();

        // Provides a placeholder controls row for future timeline-specific buttons.
        HBox controls = new HBox(10);

        // Adds scrolling so channels can grow without crowding the screen.
        ScrollPane timelineScrollPane = new ScrollPane(timelineTracks);
        timelineScrollPane.setFitToWidth(true);
        timelineScrollPane.setPrefHeight(260);
        timelineScrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        timelineScrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);

        // Adds the title, ruler, scrollable track area, and controls row to the panel.
        getChildren().addAll(
                title,
                timeRuler,
                timelineScrollPane,
                controls
        );
    }

    // Creates one timeline channel and prepares it to accept dropped audio clips.
    private HBox createTrack(String trackName) {
        HBox track = new HBox(10);
        track.setPadding(new Insets(8));
        track.setMinHeight(60);
        track.setStyle(
                "-fx-border-color: gray;" +
                "-fx-background-color: white;"
        );

        // Adds the channel label at the start of each track.
        Label label = new Label(trackName);
        label.setMinWidth(100);
        track.getChildren().add(label);

        // Allows dragged clips from the library to hover over this channel.
        track.setOnDragOver(event -> {
            if (event.getDragboard().hasString()) {
                event.acceptTransferModes(TransferMode.COPY);
            }
            event.consume();
        });

        // Adds the dropped clip to this channel using the full AudioClip object.
        track.setOnDragDropped(event -> {
            Dragboard dragboard = event.getDragboard();
            boolean success = false;

            if (dragboard.hasString()) {
                AudioClip clip = libraryPanel.findClipByPath(dragboard.getString());

                if (clip != null) {
                    Button clipButton = createClipButton(clip);
                    track.getChildren().add(clipButton);
                    success = true;
                }
            }

            event.setDropCompleted(success);
            event.consume();
        });

        return track;
    }

    // Creates a timeline button that stores the full AudioClip object.
    private Button createClipButton(AudioClip clip) {
        Button clipButton = new Button(clip.getFileName());

        // Stores the full clip object for saving, playback, and future editing.
        clipButton.setUserData(clip);

        // Removes the clip from the timeline when the button is clicked.
        clipButton.setOnAction(e -> {
            HBox parentTrack = (HBox) clipButton.getParent();
            parentTrack.getChildren().remove(clipButton);
        });

        return clipButton;
    }

    // Adds a new channel to the timeline.
    public void addChannel() {
        int channelNumber = channels.size() + 1;

        HBox newChannel = createTrack("Channel " + channelNumber);

        channels.add(newChannel);
        timelineTracks.getChildren().add(newChannel);
    }

    // Removes the last channel while keeping at least one channel available.
    public void removeLastChannel() {
        if (channels.size() <= 1) {
            return;
        }

        HBox lastChannel = channels.remove(channels.size() - 1);
        timelineTracks.getChildren().remove(lastChannel);
    }

    // Adds a clip to a specific channel using the selected channel number.
    public void addClipToTimeline(AudioClip clip, int channelNumber) {
        if (clip == null) {
            return;
        }

        if (channelNumber < 1 || channelNumber > channels.size()) {
            channelNumber = 1;
        }

        Button clipButton = createClipButton(clip);

        channels.get(channelNumber - 1)
                .getChildren()
                .add(clipButton);
    }

    // Clears only the clips from the timeline while keeping all channels in place.
    public void clearTimeline() {
        for (HBox channel : channels) {
            if (channel.getChildren().size() > 1) {
                channel.getChildren().remove(1, channel.getChildren().size());
            }
        }
    }

    // Returns the number of active timeline channels.
    public int getChannelCount() {
        return channels.size();
    }

    // Returns all timeline channels for inspection or future editing features.
    public ArrayList<HBox> getChannels() {
        return channels;
    }

    // Converts the visual timeline buttons into TimelineClip objects for saving and playback.
    public ArrayList<TimelineClip> getTimelineClips() {
        ArrayList<TimelineClip> timelineClipList = new ArrayList<>();

        for (int i = 0; i < channels.size(); i++) {
            HBox channel = channels.get(i);

            for (int j = 1; j < channel.getChildren().size(); j++) {
                Button clipButton = (Button) channel.getChildren().get(j);
                AudioClip audioClip = (AudioClip) clipButton.getUserData();

                if (audioClip != null) {
                    timelineClipList.add(new TimelineClip(i + 1, audioClip));
                }
            }
        }

        return timelineClipList;
    }
}
