// Handles audio playback operations.
// Provides methods for playing, pausing, and stopping
// audio clips and serves as the connection point
// between the GUI and backend audio engine.

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;

public class PlaybackController {

    private MediaPlayer mediaPlayer;
    private boolean loop;

    public boolean isLoop() {
        return loop;
    }

    public void playClip(AudioClip clip) {
        if (clip == null) {
            System.out.println("No clip selected.");
            return;
        }

        if (!clip.fileExists()) {
            System.out.println("File not found: " + clip.getFilePath());
            return;
        }

        stopClip();

        File file = new File(clip.getFilePath());
        Media media = new Media(file.toURI().toString());
        mediaPlayer = new MediaPlayer(media);

        if (loop) {
            mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
        }

        mediaPlayer.play();

        System.out.println("Playing: " + clip.getFileName());
    }

    public void pauseClip() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            System.out.println("Pausing playback.");
        }
    }

    public void stopClip() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
            System.out.println("Stopping playback.");
        }
    }

    public void resumeClip() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            System.out.println("Resuming playback.");
        }
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        System.out.println("Loop mode: " + (loop ? "enabled" : "disabled"));
    }
}
