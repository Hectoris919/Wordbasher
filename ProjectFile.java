// Data model representing a Voice LAB project.
// Stores project information and maintains a collection of audio clips used within the current project.

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ProjectFile {

    private String projectName;
    private List<AudioClip> clips;

    public ProjectFile(String projectName) {
        this.projectName = projectName;
        this.clips = new ArrayList<>();
    }

    public String getProjectName() {
        return projectName;
    }

    public List<AudioClip> getClips() {
        return clips;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public void addClip(AudioClip clip) {
        if (clip != null) {
            clips.add(clip);
        }
    }

    public void removeClip(AudioClip clip) {
        clips.remove(clip);
    }

    public int getClipCount() {
        return clips.size();
    }

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

    public static ProjectFile loadFromFile(String path) {
        File file = new File(path);

        if (!file.exists()) {
            return null;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String projectName = reader.readLine();

            if (projectName == null) {
                return null;
            }

            ProjectFile project = new ProjectFile(projectName);

            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\\|", 5);

                if (parts.length == 5) {
                    try {
                        AudioClip clip = new AudioClip(
                                parts[0],
                                parts[1],
                                parts[2],
                                parts[3],
                                Double.parseDouble(parts[4])
                        );

                        project.addClip(clip);

                    } catch (NumberFormatException e) {
                        System.out.println("Skipping invalid clip duration: " + line);
                    }
                }
            }

            return project;

        } catch (IOException e) {
            System.out.println("Error loading project: " + e.getMessage());
            return null;
        }
    }

    @Override
    public String toString() {
        return "Project: " + projectName + " | Clips: " + clips.size();
    }
}
