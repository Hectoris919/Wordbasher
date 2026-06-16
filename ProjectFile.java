import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents a Voice LAB project.
 * Holds the project name and manages the list of audio clips included in the project.
 * Supports saving and loading project data to and from disk.
 */
public class ProjectFile {

    private String projectName;
    private List<AudioClip> clips;

    /**
     * Creates a new project with the given name and an empty clip list.
     *
     * @param projectName the name of the project
     */
    public ProjectFile(String projectName) {
        this.projectName = projectName;
        this.clips = new ArrayList<>();
    }

    /**
     * Returns the name of this project.
     */
    public String getProjectName() {
        return projectName;
    }

    /**
     * Returns the list of clips in this project.
     */
    public List<AudioClip> getClips() {
        return clips;
    }

    /**
     * Sets the project name.
     */
    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    /**
     * Adds a clip to the project.
     *
     * @param clip the AudioClip to add
     */
    public void addClip(AudioClip clip) {
        clips.add(clip);
    }

    /**
     * Removes a clip from the project.
     *
     * @param clip the AudioClip to remove
     */
    public void removeClip(AudioClip clip) {
        clips.remove(clip);
    }

    /**
     * Returns the number of clips currently in the project.
     */
    public int getClipCount() {
        return clips.size();
    }

    /**
     * Saves the project to a file at the given path.
     * The first line is the project name, followed by one clip per line
     * in the format: fileName|filePath|category|tags|duration
     *
     * @param path the file path to save to
     */
    public void saveToFile(String path) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(path))) {
            writer.write(projectName);
            writer.newLine();
            for (AudioClip clip : clips) {
                writer.write(
                    clip.getFileName() + "|" +
                    clip.getFilePath() + "|" +
                    clip.getCategory() + "|" +
                    clip.getTags() + "|" +
                    clip.getDuration()
                );
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving project: " + e.getMessage());
        }
    }

    /**
     * Loads a project from a file at the given path.
     * Returns null if the file does not exist or cannot be read.
     *
     * @param path the file path to load from
     * @return the loaded ProjectFile or null
     */
    public static ProjectFile loadFromFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String projectName = reader.readLine();
            if (projectName == null) return null;

            ProjectFile project = new ProjectFile(projectName);

            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);
                if (parts.length == 5) {
                    AudioClip clip = new AudioClip(
                        parts[0],
                        parts[1],
                        parts[2],
                        parts[3],
                        Double.parseDouble(parts[4])
                    );
                    project.addClip(clip);
                }
            }
            return project;

        } catch (IOException e) {
            System.out.println("Error loading project: " + e.getMessage());
            return null;
        }
    }

    /**
     * Returns a summary of the project including the name and clip count.
     */
    @Override
    public String toString() {
        return "Project: " + projectName + " | Clips: " + clips.size();
    }
}