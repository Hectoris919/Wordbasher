import org.junit.jupiter.api.Test;
import java.io.File;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for the ProjectFile class.
 * Tests cover project creation, clip management, file save/load, and toString.
 */
public class ProjectFileTest {

    /**
     * Verifies that a new project is created with the correct name.
     */
    @Test
    public void testProjectCreation() {
        ProjectFile project = new ProjectFile("MyProject");
        assertEquals("MyProject", project.getProjectName());
    }

    /**
     * Verifies that a new project starts with zero clips.
     */
    @Test
    public void testInitialClipCountIsZero() {
        ProjectFile project = new ProjectFile("TestProject");
        assertEquals(0, project.getClipCount());
    }

    /**
     * Verifies that adding a clip increases the clip count by one.
     */
    @Test
    public void testAddClipIncreasesCount() {
        ProjectFile project = new ProjectFile("TestProject");
        AudioClip clip = new AudioClip("yes.wav", "/audio/yes.wav", "Responses", "yes", 0.8);
        project.addClip(clip);
        assertEquals(1, project.getClipCount());
    }

    /**
     * Verifies that removing a clip decreases the clip count by one.
     */
    @Test
    public void testRemoveClipDecreasesCount() {
        ProjectFile project = new ProjectFile("TestProject");
        AudioClip clip = new AudioClip("no.wav", "/audio/no.wav", "Responses", "no", 0.6);
        project.addClip(clip);
        project.removeClip(clip);
        assertEquals(0, project.getClipCount());
    }

    /**
     * Verifies that a removed clip is no longer in the clip list.
     */
    @Test
    public void testRemovedClipNotInList() {
        ProjectFile project = new ProjectFile("TestProject");
        AudioClip clip = new AudioClip("bye.wav", "/audio/bye.wav", "Greetings", "bye", 1.0);
        project.addClip(clip);
        project.removeClip(clip);
        assertFalse(project.getClips().contains(clip));
    }

    /**
     * Verifies that saveToFile creates a file on disk.
     */
    @Test
    public void testSaveToFileCreatesFile() throws Exception {
        ProjectFile project = new ProjectFile("SaveTest");
        AudioClip clip = new AudioClip("hello.wav", "/audio/hello.wav", "Greetings", "hello", 2.0);
        project.addClip(clip);
        File temp = File.createTempFile("project", ".vlb");
        temp.deleteOnExit();
        project.saveToFile(temp.getAbsolutePath());
        assertTrue(temp.exists());
    }

    /**
     * Verifies that loadFromFile restores the correct project name.
     */
    @Test
    public void testLoadFromFileRestoresProjectName() throws Exception {
        ProjectFile project = new ProjectFile("LoadTest");
        File temp = File.createTempFile("project", ".vlb");
        temp.deleteOnExit();
        project.saveToFile(temp.getAbsolutePath());
        ProjectFile loaded = ProjectFile.loadFromFile(temp.getAbsolutePath());
        assertNotNull(loaded);
        assertEquals("LoadTest", loaded.getProjectName());
    }

    /**
     * Verifies that loadFromFile restores the correct number of clips.
     */
    @Test
    public void testLoadFromFileRestoresClipCount() throws Exception {
        ProjectFile project = new ProjectFile("LoadTest");
        project.addClip(new AudioClip("a.wav", "/audio/a.wav", "Misc", "a", 1.0));
        project.addClip(new AudioClip("b.wav", "/audio/b.wav", "Misc", "b", 2.0));
        File temp = File.createTempFile("project", ".vlb");
        temp.deleteOnExit();
        project.saveToFile(temp.getAbsolutePath());
        ProjectFile loaded = ProjectFile.loadFromFile(temp.getAbsolutePath());
        assertEquals(2, loaded.getClipCount());
    }

    /**
     * Verifies that loadFromFile returns null when the file does not exist.
     */
    @Test
    public void testLoadFromFileMissingFileReturnsNull() {
        ProjectFile loaded = ProjectFile.loadFromFile("/fake/path/doesnotexist.vlb");
        assertNull(loaded);
    }

    /**
     * Verifies that toString returns the expected format.
     */
    @Test
    public void testToStringFormat() {
        ProjectFile project = new ProjectFile("FormatTest");
        project.addClip(new AudioClip("x.wav", "/x.wav", "Misc", "x", 1.0));
        assertEquals("Project: FormatTest | Clips: 1", project.toString());
    }
}