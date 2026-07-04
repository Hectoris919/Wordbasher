// Provides the audio processing workspace and displays waveform information for selected clips.

import javafx.geometry.Insets;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;

import javax.sound.sampled.*;
import java.io.File;
import java.util.Random;

public class WaveformPanel extends VBox {

    private Label selectedClip;
    private Label waveformStatus;
    private Canvas waveformCanvas;

    public WaveformPanel() {

        // Sets spacing for the waveform panel layout.
        setSpacing(10);

        // Creates the title for the audio editor section.
        Label title = new Label("Audio Processing / Trim Editor");

        // Displays the currently selected clip name.
        selectedClip = new Label("Selected Clip: none");

        // Displays whether a real or fallback waveform is being shown.
        waveformStatus = new Label("Waveform: none");

        // Creates the canvas used to draw the waveform.
        waveformCanvas = new Canvas(900, 180);

        // Wraps the canvas in a styled pane.
        Pane waveformPane = new Pane(waveformCanvas);
        waveformPane.setPrefHeight(180);
        waveformPane.setStyle(
                "-fx-border-color: black;" +
                "-fx-background-color: #eeeeee;"
        );

        // Draws the initial empty waveform message.
        drawEmptyWaveform();

        // Creates trim start input.
        TextField trimStart = new TextField();
        trimStart.setPromptText("Trim Start");

        // Creates trim end input.
        TextField trimEnd = new TextField();
        trimEnd.setPromptText("Trim End");

        // Creates audio editing placeholder buttons.
        Button applyTrim = new Button("Apply Trim");
        Button fadeIn = new Button("Fade In");
        Button fadeOut = new Button("Fade Out");
        Button normalize = new Button("Normalize");
        Button reverse = new Button("Reverse");

        // Groups the editing controls in one row.
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

        // Adds all waveform panel components to the layout.
        getChildren().addAll(
                title,
                selectedClip,
                waveformStatus,
                waveformPane,
                trimControls
        );
    }

    // Loads a selected clip and draws its waveform.
    public void loadClip(AudioClip clip) {
        if (clip == null) {
            selectedClip.setText("Selected Clip: none");
            waveformStatus.setText("Waveform: none");
            drawEmptyWaveform();
            return;
        }

        selectedClip.setText("Selected Clip: " + clip.getFileName());
        drawWaveform(clip);
    }

    // Draws the empty waveform message.
    private void drawEmptyWaveform() {
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();

        gc.clearRect(0, 0, waveformCanvas.getWidth(), waveformCanvas.getHeight());
        gc.setFill(Color.GRAY);
        gc.fillText("No clip selected", 20, 90);
    }

    // Attempts to draw a real waveform and falls back if the format is unsupported.
    private void drawWaveform(AudioClip clip) {
        try {
            double[] samples = readAudioSamples(clip.getFilePath());
            waveformStatus.setText("Waveform: real audio data");
            drawSamples(samples);
        } catch (Exception e) {
            waveformStatus.setText("Waveform: preview fallback for unsupported format");
            drawFallbackWaveform(clip);
        }
    }

    // Reads PCM audio samples from supported audio files.
    private double[] readAudioSamples(String filePath) throws Exception {
        File file = new File(filePath);

        AudioInputStream originalStream = AudioSystem.getAudioInputStream(file);
        AudioFormat originalFormat = originalStream.getFormat();

        // Converts supported audio into 16-bit PCM so samples can be read consistently.
        AudioFormat pcmFormat = new AudioFormat(
                AudioFormat.Encoding.PCM_SIGNED,
                originalFormat.getSampleRate(),
                16,
                originalFormat.getChannels(),
                originalFormat.getChannels() * 2,
                originalFormat.getSampleRate(),
                false
        );

        AudioInputStream pcmStream = AudioSystem.getAudioInputStream(pcmFormat, originalStream);

        byte[] audioBytes = pcmStream.readAllBytes();

        int frameSize = pcmFormat.getFrameSize();
        int channels = pcmFormat.getChannels();
        int totalFrames = audioBytes.length / frameSize;

        double[] samples = new double[totalFrames];

        // Converts raw PCM bytes into normalized sample amplitudes.
        for (int frame = 0; frame < totalFrames; frame++) {
            double sum = 0;

            for (int channel = 0; channel < channels; channel++) {
                int byteIndex = frame * frameSize + channel * 2;

                int low = audioBytes[byteIndex] & 0xff;
                int high = audioBytes[byteIndex + 1];

                short sample = (short) ((high << 8) | low);
                sum += sample / 32768.0;
            }

            samples[frame] = sum / channels;
        }

        pcmStream.close();
        originalStream.close();

        return samples;
    }

    // Draws real audio samples onto the waveform canvas.
    private void drawSamples(double[] samples) {
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();

        double width = waveformCanvas.getWidth();
        double height = waveformCanvas.getHeight();
        double centerY = height / 2;

        gc.clearRect(0, 0, width, height);

        // Draws the waveform center line.
        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, centerY, width, centerY);

        gc.setStroke(Color.BLACK);

        int samplesPerPixel = Math.max(1, samples.length / (int) width);

        // Draws the loudest sample range for each horizontal pixel.
        for (int x = 0; x < width; x++) {
            int start = x * samplesPerPixel;
            int end = Math.min(start + samplesPerPixel, samples.length);

            double min = 0;
            double max = 0;

            for (int i = start; i < end; i++) {
                min = Math.min(min, samples[i]);
                max = Math.max(max, samples[i]);
            }

            double y1 = centerY - max * centerY;
            double y2 = centerY - min * centerY;

            gc.strokeLine(x, y1, x, y2);
        }
    }

    // Draws a visual fallback when Java Sound cannot read the file format.
    private void drawFallbackWaveform(AudioClip clip) {
        GraphicsContext gc = waveformCanvas.getGraphicsContext2D();

        double width = waveformCanvas.getWidth();
        double height = waveformCanvas.getHeight();
        double centerY = height / 2;

        gc.clearRect(0, 0, width, height);

        gc.setStroke(Color.GRAY);
        gc.strokeLine(0, centerY, width, centerY);

        gc.setStroke(Color.BLACK);

        Random random = new Random(clip.getFileName().hashCode());

        // Draws a consistent preview pattern for unsupported formats.
        for (int x = 0; x < width; x += 6) {
            double barHeight = 10 + random.nextDouble() * 120;
            gc.strokeLine(x, centerY - barHeight / 2, x, centerY + barHeight / 2);
        }
    }
}
