// Handles audio playback operations.
// Provides methods for playing, pausing, and stopping
// audio clips and serves as the connection point
// between the GUI and backend audio engine.

public class PlaybackController {

    public void playClip(AudioClip clip) {

        System.out.println(
                "Playing: " + clip.getFileName()
        );
    }

    public void stopClip() {

        System.out.println(
                "Stopping playback"
        );
    }

    public void pauseClip() {

        System.out.println(
                "Pausing playback"
        );
    }
}