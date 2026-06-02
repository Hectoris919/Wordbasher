// Data model representing a Voice LAB project.
// Stores project information and maintains a collection of audio clips used within the current project.

import java.util.ArrayList;
import java.util.List;

public class ProjectFile {

    private String projectName;
    private List<AudioClip> clips;

    public ProjectFile(String projectName) {

        this.projectName = projectName;
        this.clips = new ArrayList<>();
    }

    // Getters

    public String getProjectName() {
        return projectName;
    }

    public List<AudioClip> getClips() {
        return clips;
    }

    // Setters

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    // Methods

    public void addClip(AudioClip clip) {
        clips.add(clip);
    }

    public void removeClip(AudioClip clip) {
        clips.remove(clip);
    }

    public int getClipCount() {
        return clips.size();
    }

    @Override
    public String toString() {

        return "Project: " +
                projectName +
                " | Clips: " +
                clips.size();
    }
}