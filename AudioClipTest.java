import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the AudioClip class.
 * Tests cover object creation, getters, setters, file existence checks, and toString.
 */
public class AudioClipTest {

    /**
     * Verifies that an AudioClip is created correctly and all getters return the right values.
     */
    @Test
    public void testObjectCreation() {
        AudioClip clip = new AudioClip("hello.wav", "/audio/hello.wav", "Greetings", "hello", 2.5);
        assertEquals("hello.wav", clip.getFileName());
        assertEquals("/audio/hello.wav", clip.getFilePath());
        assertEquals("Greetings", clip.getCategory());
        assertEquals("hello", clip.getTags());
        assertEquals(2.5, clip.getDuration());
    }

    /**
     * Verifies that all setters update the clip's fields correctly.
     */
    @Test
    public void testSetters() {
        AudioClip clip = new AudioClip("old.wav", "/old/path.wav", "Misc", "old", 1.0);
        clip.setFileName("new.wav");
        clip.setFilePath("/new/path.wav");
        clip.setCategory("Voice");
        clip.setTags("new");
        clip.setDuration(3.0);
        assertEquals("new.wav", clip.getFileName());
        assertEquals("/new/path.wav", clip.getFilePath());
        assertEquals("Voice", clip.getCategory());
        assertEquals("new", clip.getTags());
        assertEquals(3.0, clip.getDuration());
    }

    /**
     * Verifies that fileExists returns false when the file path points to a missing file.
     */
    @Test
    public void testFileExistsMissingFile() {
        AudioClip clip = new AudioClip("missing.wav", "/fake/path/missing.wav", "Misc", "", 1.0);
        assertFalse(clip.fileExists());
    }

    /**
     * Verifies that fileExists returns false when the file path is null.
     */
    @Test
    public void testFileExistsNullPath() {
        AudioClip clip = new AudioClip("null.wav", null, "Misc", "", 1.0);
        assertFalse(clip.fileExists());
    }

    /**
     * Verifies that toString returns the file name of the clip.
     */
    @Test
    public void testToStringReturnsFileName() {
        AudioClip clip = new AudioClip("laugh.wav", "/audio/laugh.wav", "Laughs", "laugh", 1.2);
        assertEquals("laugh.wav", clip.toString());
    }

    /**
     * Verifies that fileExists returns true when the file actually exists on disk.
     */
    @Test
    public void testFileExistsRealFile() throws Exception {
        java.io.File temp = java.io.File.createTempFile("testclip", ".wav");
        temp.deleteOnExit();
        AudioClip clip = new AudioClip(temp.getName(), temp.getAbsolutePath(), "Test", "test", 0.5);
        assertTrue(clip.fileExists());
    }
}