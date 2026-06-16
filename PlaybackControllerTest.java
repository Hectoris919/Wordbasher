import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the PlaybackController class.
 * Tests cover initial state, loop mode, and graceful handling of edge cases.
 */
public class PlaybackControllerTest {

    /**
     * Verifies that isPlaying is false when the controller is first created.
     */
    @Test
    public void testInitialStateNotPlaying() {
        PlaybackController controller = new PlaybackController();
        assertFalse(controller.isPlaying());
    }

    /**
     * Verifies that isPaused is false when the controller is first created.
     */
    @Test
    public void testInitialStateNotPaused() {
        PlaybackController controller = new PlaybackController();
        assertFalse(controller.isPaused());
    }

    /**
     * Verifies that loop mode is disabled by default.
     */
    @Test
    public void testLoopDisabledByDefault() {
        PlaybackController controller = new PlaybackController();
        assertFalse(controller.isLoop());
    }

    /**
     * Verifies that setLoop enables loop mode when passed true.
     */
    @Test
    public void testSetLoopEnable() {
        PlaybackController controller = new PlaybackController();
        controller.setLoop(true);
        assertTrue(controller.isLoop());
    }

    /**
     * Verifies that setLoop disables loop mode when passed false.
     */
    @Test
    public void testSetLoopDisable() {
        PlaybackController controller = new PlaybackController();
        controller.setLoop(true);
        controller.setLoop(false);
        assertFalse(controller.isLoop());
    }

    /**
     * Verifies that playing a clip with a missing file does not throw an exception.
     */
    @Test
    public void testPlayMissingFileDoesNotCrash() {
        PlaybackController controller = new PlaybackController();
        AudioClip clip = new AudioClip("missing.wav", "/fake/missing.wav", "Misc", "", 1.0);
        assertDoesNotThrow(() -> controller.playClip(clip));
    }

    /**
     * Verifies that calling stopClip when nothing is playing does not throw an exception.
     */
    @Test
    public void testStopWhenNothingPlayingDoesNotCrash() {
        PlaybackController controller = new PlaybackController();
        assertDoesNotThrow(() -> controller.stopClip());
    }

    /**
     * Verifies that calling pauseClip when nothing is playing does not throw an exception.
     */
    @Test
    public void testPauseWhenNothingPlayingDoesNotCrash() {
        PlaybackController controller = new PlaybackController();
        assertDoesNotThrow(() -> controller.pauseClip());
    }

    /**
     * Verifies that calling resumeClip when nothing is paused does not throw an exception.
     */
    @Test
    public void testResumeWhenNothingPausedDoesNotCrash() {
        PlaybackController controller = new PlaybackController();
        assertDoesNotThrow(() -> controller.resumeClip());
    }

    /**
     * Verifies that stopping resets isPaused back to false.
     */
    @Test
    public void testStopResetsPausedState() {
        PlaybackController controller = new PlaybackController();
        assertDoesNotThrow(() -> controller.stopClip());
        assertFalse(controller.isPaused());
    }
}