// Main application entry point.
// Creates the primary Voice LAB window and assembles all GUI components,
// including menus, panels, timeline, playback controls, and project tools.

// Main application entry point.
// Creates the primary Voice LAB window and assembles all GUI components.

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

public class Main extends Application {

    private ProjectFile currentProject;

    private void updateChannelSelector(ComboBox<Integer> channelSelector, TimelinePanel timelinePanel) {

    channelSelector.getItems().clear();

    for (int i = 1; i <= timelinePanel.getChannelCount(); i++) {
        channelSelector.getItems().add(i);
    }

    channelSelector.setValue(1);
}
    @Override
    public void start(Stage stage) {

        currentProject = new ProjectFile("Voice LAB Project");

        MenuBar menuBar = createMenuBar();

        Button importButton = new Button("Import Audio");
        Button saveButton = new Button("Save Project");
        Button loadButton = new Button("Load Project");
        Button exportButton = new Button("Export WAV");

        TextField searchField = new TextField();
        searchField.setPromptText("Search clips...");
        searchField.setPrefWidth(300);

        HBox toolbar = new HBox(10, importButton, saveButton, loadButton, exportButton, searchField);
        toolbar.setPadding(new Insets(10));

        VBox topSection = new VBox(menuBar, toolbar);

        LibraryPanel libraryPanel = new LibraryPanel();
        WaveformPanel waveformPanel = new WaveformPanel();
        TimelinePanel timelinePanel = new TimelinePanel();
        ClipDetailsPanel clipDetailsPanel = new ClipDetailsPanel();
        PlaybackController playbackController = new PlaybackController();

        libraryPanel.getClipLibrary()
                .getSelectionModel()
                .selectedItemProperty()
                .addListener((observable, oldClip, newClip) -> {
                    System.out.println("Selected clip: " + newClip);
                    clipDetailsPanel.displayClip(newClip);
                    waveformPanel.loadClip(newClip);
                });

        importButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Import Audio File");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Audio Files", "*.wav", "*.mp3", "*.aiff")
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

        saveButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Save Voice LAB Project");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Voice LAB Project", "*.vlb")
            );

            File selectedFile = fileChooser.showSaveDialog(stage);

            if (selectedFile != null) {
                currentProject.saveToFile(selectedFile.getAbsolutePath());
                System.out.println("Project saved: " + selectedFile.getAbsolutePath());
            }
        });

        loadButton.setOnAction(e -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Load Voice LAB Project");

            fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Voice LAB Project", "*.vlb")
            );

            File selectedFile = fileChooser.showOpenDialog(stage);

            if (selectedFile != null) {
                ProjectFile loadedProject = ProjectFile.loadFromFile(selectedFile.getAbsolutePath());

                if (loadedProject != null) {
                    currentProject = loadedProject;

                    libraryPanel.getClipLibrary().getItems().clear();
                    libraryPanel.getClipLibrary().getItems().addAll(currentProject.getClips());

                    System.out.println("Project loaded: " + selectedFile.getAbsolutePath());
                } else {
                    System.out.println("Failed to load project.");
                }
            }
        });

        VBox centerPanel = new VBox(20, waveformPanel, timelinePanel);
        centerPanel.setPadding(new Insets(10));

        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button loopButton = new Button("Loop");
        Button addToTimelineButton = new Button("Add To Timeline");
        Button addChannelButton = new Button("Add Channel");

        ComboBox<Integer> channelSelector = new ComboBox<>();
        updateChannelSelector(channelSelector, timelinePanel);

        playButton.setOnAction(e -> {
            AudioClip selectedClip = libraryPanel.getSelectedClip();
            playbackController.playClip(selectedClip);
        });

        pauseButton.setOnAction(e -> playbackController.pauseClip());

        stopButton.setOnAction(e -> playbackController.stopClip());

        loopButton.setOnAction(e -> playbackController.setLoop(!playbackController.isLoop()));

        addToTimelineButton.setOnAction(e -> {
            AudioClip selectedClip = libraryPanel.getSelectedClip();
            timelinePanel.addClipToTimeline(selectedClip, channelSelector.getValue());
        });

        addChannelButton.setOnAction(e -> {
            timelinePanel.addChannel();
            updateChannelSelector(channelSelector, timelinePanel);
        });

        HBox playbackControls = new HBox(10, playButton, pauseButton, stopButton, loopButton, addToTimelineButton, channelSelector, new Label("Channel:"), addChannelButton);
        playbackControls.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setTop(topSection);
        root.setLeft(libraryPanel);
        root.setCenter(centerPanel);
        root.setRight(clipDetailsPanel);
        root.setBottom(playbackControls);

        Scene scene = new Scene(root, 1450, 850);

        stage.setTitle("Voice LAB");
        stage.setScene(scene);
        stage.show();
    }

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
        helpMenu.getItems().add(new MenuItem("About Voice LAB"));

        menuBar.getMenus().addAll(fileMenu, projectMenu, toolsMenu, helpMenu);
        return menuBar;
    }

    public static void main(String[] args) {
        launch();
    }
}
