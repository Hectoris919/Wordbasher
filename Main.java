// Main application entry point for Voice LAB.
// Builds the GUI, connects frontend panels, and wires project, playback, search, and timeline actions.

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Main extends Application {

    private ProjectFile currentProject;

    @Override
    public void start(Stage stage) {

        // Creates the starting project object.
        currentProject = new ProjectFile("Voice LAB Project");

        // Creates the main menu bar.
        MenuBar menuBar = createMenuBar();

        // Creates the top toolbar buttons.
        Button importButton = new Button("Import Audio");
        Button saveButton = new Button("Save Project");
        Button loadButton = new Button("Load Project");
        Button exportButton = new Button("Export WAV");

        // Creates the search field used to filter the clip library.
        TextField searchField = new TextField();
        searchField.setPromptText("Search clips...");
        searchField.setPrefWidth(300);

        // Places the top toolbar controls in one row.
        HBox toolbar = new HBox(
                10,
                importButton,
                saveButton,
                loadButton,
                exportButton,
                searchField
        );
        toolbar.setPadding(new Insets(10));

        // Combines the menu bar and toolbar into the top section.
        VBox topSection = new VBox(menuBar, toolbar);

        // Creates the main application panels.
        LibraryPanel libraryPanel = new LibraryPanel();
        WaveformPanel waveformPanel = new WaveformPanel();
        TimelinePanel timelinePanel = new TimelinePanel(libraryPanel);
        ClipDetailsPanel clipDetailsPanel = new ClipDetailsPanel();

        // Creates playback controllers and timeline playback helper.
        PlaybackController playbackController = new PlaybackController();
        TimelinePlaybackEngine timelinePlaybackEngine = new TimelinePlaybackEngine();

        // Connects the search field to live clip filtering.
        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            ArrayList<AudioClip> filteredClips = new ArrayList<>();
            String search = newValue.toLowerCase();

            for (AudioClip clip : libraryPanel.getAllClips()) {
                if (clip.getFileName().toLowerCase().contains(search)
                        || clip.getCategory().toLowerCase().contains(search)
                        || clip.getTags().toLowerCase().contains(search)) {
                    filteredClips.add(clip);
                }
            }

            libraryPanel.refreshLibrary(filteredClips);
        });

        // Updates clip details and waveform display when a clip is selected.
        libraryPanel.getClipLibrary()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldClip, newClip) -> {
                    System.out.println("Selected clip: " + newClip);
                    clipDetailsPanel.displayClip(newClip);
                    waveformPanel.loadClip(newClip);
                });

        // Imports a real audio file and adds it to the library.
        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Audio File");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Audio Files",
                            "*.wav",
                            "*.mp3",
                            "*.m4a",
                            "*.aiff",
                            "*.aif",
                            "*.flac",
                            "*.aac"
                    )
            );

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                AudioClip newClip = new AudioClip(
                        selectedFile.getName(),
                        selectedFile.getAbsolutePath(),
                        "Imported",
                        "imported",
                        0.0
                );

                libraryPanel.addClip(newClip);
                currentProject.addClip(newClip);

                System.out.println("Imported: " + selectedFile.getAbsolutePath());
            }
        });

        // Saves the library clips and timeline arrangement to a project file.
        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Voice LAB Project");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Voice LAB Project",
                            "*.vlb"
                    )
            );

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                currentProject = new ProjectFile("Voice LAB Project");

                for (AudioClip clip : libraryPanel.getAllClips()) {
                    currentProject.addClip(clip);
                }

                for (TimelineClip timelineClip : timelinePanel.getTimelineClips()) {
                    currentProject.addTimelineClip(timelineClip);
                }

                currentProject.saveToFile(selectedFile.getAbsolutePath());

                System.out.println("Project saved: " + selectedFile.getAbsolutePath());
            }
        });

        // Loads a saved project and restores the library and timeline layout.
        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Voice LAB Project");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter(
                            "Voice LAB Project",
                            "*.vlb"
                    )
            );

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                ProjectFile loadedProject =
                        ProjectFile.loadFromFile(selectedFile.getAbsolutePath());

                if (loadedProject != null) {
                    currentProject = loadedProject;

                    libraryPanel.setAllClips(
                            new ArrayList<>(currentProject.getClips())
                    );

                    timelinePanel.clearTimeline();

                    for (TimelineClip timelineClip : currentProject.getTimelineClips()) {
                        while (timelinePanel.getChannelCount()
                                < timelineClip.getChannelNumber()) {
                            timelinePanel.addChannel();
                        }

                        timelinePanel.addClipToTimeline(
                                timelineClip.getClip(),
                                timelineClip.getChannelNumber()
                        );
                    }

                    System.out.println("Project loaded: " + selectedFile.getAbsolutePath());
                } else {
                    System.out.println("Failed to load project.");
                }
            }
        });

        // Creates the center area containing waveform and timeline panels.
        VBox centerPanel = new VBox(20, waveformPanel, timelinePanel);
        centerPanel.setPadding(new Insets(10));

        // Creates playback and timeline control buttons.
        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button loopButton = new Button("Loop");
        Button playTimelineButton = new Button("Play Timeline");
        Button addToTimelineButton = new Button("Add To Timeline");
        Button addChannelButton = new Button("Add Channel");
        Button removeChannelButton = new Button("Remove Channel");
        Button clearTimelineButton = new Button("Clear Timeline");

        // Creates the channel selector used by the Add To Timeline button.
        ComboBox<Integer> channelSelector = new ComboBox<>();
        updateChannelSelector(channelSelector, timelinePanel);

        // Plays the selected clip from the library.
        playButton.setOnAction(e -> {
            AudioClip selectedClip = libraryPanel.getSelectedClip();
            playbackController.playClip(selectedClip);
        });

        // Plays all clips currently arranged on the timeline.
        playTimelineButton.setOnAction(e -> {
            playbackController.playTimeline(
                    timelinePlaybackEngine.getPlaybackOrder(timelinePanel)
            );
        });

        // Pauses the active playback.
        pauseButton.setOnAction(e -> playbackController.pauseClip());

        // Stops the active playback.
        stopButton.setOnAction(e -> playbackController.stopClip());

        // Toggles loop mode for playback.
        loopButton.setOnAction(e -> {
            playbackController.setLoop(!playbackController.isLoop());
        });

        // Adds the selected library clip to the selected timeline channel.
        addToTimelineButton.setOnAction(e -> {
            AudioClip selectedClip = libraryPanel.getSelectedClip();
            timelinePanel.addClipToTimeline(
                    selectedClip,
                    channelSelector.getValue()
            );
        });

        // Adds a new timeline channel and refreshes the channel selector.
        addChannelButton.setOnAction(e -> {
            timelinePanel.addChannel();
            updateChannelSelector(channelSelector, timelinePanel);
        });

        // Removes the last timeline channel and refreshes the channel selector.
        removeChannelButton.setOnAction(e -> {
            timelinePanel.removeLastChannel();
            updateChannelSelector(channelSelector, timelinePanel);
        });

        // Clears only the clips from the timeline while keeping channels.
        clearTimelineButton.setOnAction(e -> {
            timelinePanel.clearTimeline();
            updateChannelSelector(channelSelector, timelinePanel);
        });

        // Creates the bottom control bar.
        HBox playbackControls = new HBox(
                10,
                playButton,
                pauseButton,
                stopButton,
                loopButton,
                playTimelineButton,
                addToTimelineButton,
                new Label("Channel:"),
                channelSelector,
                addChannelButton,
                removeChannelButton,
                clearTimelineButton
        );
        playbackControls.setPadding(new Insets(10));

        // Builds the main layout.
        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setLeft(libraryPanel);
        root.setCenter(centerPanel);
        root.setRight(clipDetailsPanel);
        root.setBottom(playbackControls);

        // Creates and displays the application scene.
        Scene scene = new Scene(root, 1450, 850);

        stage.setTitle("Voice LAB");
        stage.setScene(scene);
        stage.show();
    }

    // Refreshes the channel selector so it matches the number of timeline channels.
    private void updateChannelSelector(
            ComboBox<Integer> channelSelector,
            TimelinePanel timelinePanel) {

        channelSelector.getItems().clear();

        for (int i = 1; i <= timelinePanel.getChannelCount(); i++) {
            channelSelector.getItems().add(i);
        }

        channelSelector.setValue(1);
    }

    // Creates the menu bar for file, project, tools, and help actions.
    private MenuBar createMenuBar() {
        MenuBar menuBar = new MenuBar();

        Menu fileMenu = new Menu("File");
        fileMenu.getItems().addAll(
                new MenuItem("Import Audio"),
                new MenuItem("Save Project"),
                new MenuItem("Load Project"),
                new SeparatorMenuItem(),
                new MenuItem("Export WAV"),
                new SeparatorMenuItem(),
                new MenuItem("Exit")
        );

        Menu projectMenu = new Menu("Project");
        projectMenu.getItems().addAll(
                new MenuItem("New Project"),
                new MenuItem("Project Settings")
        );

        Menu toolsMenu = new Menu("Tools");
        toolsMenu.getItems().addAll(
                new MenuItem("Trim Audio"),
                new MenuItem("Normalize Audio"),
                new MenuItem("Generate Phrase")
        );

        Menu helpMenu = new Menu("Help");
        helpMenu.getItems().add(
                new MenuItem("About Voice LAB")
        );

        menuBar.getMenus().addAll(
                fileMenu,
                projectMenu,
                toolsMenu,
                helpMenu
        );

        return menuBar;
    }

    // Launches the JavaFX application.
    public static void main(String[] args) {
        launch();
    }
}
