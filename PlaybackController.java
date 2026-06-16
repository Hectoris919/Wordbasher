/**
 * Handles playback operations for audio clips in Voice LAB.
 * Tracks play, pause, and loop state and acts as the bridge
 * between the GUI controls and the audio processing backend.
 */
public class PlaybackController {

    private boolean isPlaying;
    private boolean isPaused;
    private boolean loop;

    /**
     * Creates a new PlaybackController with all states set to false by default.
     */
    public PlaybackController() {
        this.isPlaying = false;
        this.isPaused = false;
        this.loop = false;
    }

    /**
     * Returns true if a clip is currently playing.
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Returns true if playback is currently paused.
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Returns true if loop mode is enabled.
     */
    public boolean isLoop() {
        return loop;
    }

    /**
     * Plays the given clip if the file exists on disk.
     * Does nothing and prints a message if the file is missing.
     *
     * @param clip the AudioClip to play
     */
    public void playClip(AudioClip clip) {
        if (!clip.fileExists()) {
            System.out.println("File not found: " + clip.getFilePath());
            return;
        }
        isPlaying = true;
        isPaused = false;
        System.out.println("Playing: " + clip.getFileName());
    }

    /**
     * Stops playback and resets both playing and paused states.
     * Does nothing if nothing is currently playing or paused.
     */
    public void stopClip() {
        if (!isPlaying && !isPaused) {
            System.out.println("Nothing is playing.");
            return;
        }
        isPlaying = false;
        isPaused = false;
        System.out.println("Stopping playback.");
    }

    /**
     * Pauses playback if a clip is currently playing.
     * Does nothing if nothing is playing.
     */
    public void pauseClip() {
        if (!isPlaying) {
            System.out.println("Nothing to pause.");
            return;
        }
        isPlaying = false;
        isPaused = true;
        System.out.println("Pausing playback.");
    }

    /**
     * Resumes playback if a clip is currently paused.
     * Does nothing if nothing is paused.
     */
    public void resumeClip() {
        if (!isPaused) {
            System.out.println("Nothing to resume.");
            return;
        }
        isPaused = false;
        isPlaying = true;
        System.out.println("Resuming playback.");
    }

    /**
     * Enables or disables loop mode.
     *
     * @param loop true to enable looping, false to disable
     */
    public void setLoop(boolean loop) {
        this.loop = loop;
        System.out.println("Loop mode: " + (loop ? "enabled" : "disabled"));
    }
}