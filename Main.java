// Main application entry point.
// Creates the primary Voice LAB window and assembles all GUI components,
// including menus, panels, timeline, playback controls, and project tools.

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.geometry.Insets;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {

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

        VBox centerPanel = new VBox(20, waveformPanel, timelinePanel);
        centerPanel.setPadding(new Insets(10));

        Button playButton = new Button("Play");
        Button pauseButton = new Button("Pause");
        Button stopButton = new Button("Stop");
        Button loopButton = new Button("Loop");

        HBox playbackControls = new HBox(10, playButton, pauseButton, stopButton, loopButton);
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