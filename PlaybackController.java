// Handles audio playback operations.
// Provides methods for playing, pausing, and stopping
// audio clips and serves as the connection point
// between the GUI and backend audio engine.

public class PlaybackController {

    private boolean isPlaying;
    private boolean isPaused;
    private boolean loop;

    public PlaybackController() {
        this.isPlaying = false;
        this.isPaused = false;
        this.loop = false;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public boolean isPaused() {
        return isPaused;
    }

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

        isPlaying = true;
        isPaused = false;

        System.out.println("Playing: " + clip.getFileName());
    }

    public void stopClip() {

        if (!isPlaying && !isPaused) {
            System.out.println("Nothing is playing.");
            return;
        }

        isPlaying = false;
        isPaused = false;

        System.out.println("Stopping playback.");
    }

    public void pauseClip() {

        if (!isPlaying) {
            System.out.println("Nothing to pause.");
            return;
        }

        isPlaying = false;
        isPaused = true;

        System.out.println("Pausing playback.");
    }

    public void resumeClip() {

        if (!isPaused) {
            System.out.println("Nothing to resume.");
            return;
        }

        isPaused = false;
        isPlaying = true;

        System.out.println("Resuming playback.");
    }

    public void setLoop(boolean loop) {
        this.loop = loop;

        System.out.println(
                "Loop mode: " +
                (loop ? "enabled" : "disabled")
        );
    }
}
