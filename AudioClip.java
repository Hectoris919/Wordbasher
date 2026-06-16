import java.io.File;

/**
 * Represents a single audio clip in the Voice LAB library.
 * Stores metadata like the file name, path, category, tags, and duration.
 */
public class AudioClip {

    private String fileName;
    private String filePath;
    private String category;
    private String tags;
    private double duration;

    /**
     * Creates an AudioClip with the given metadata.
     *
     * @param fileName name of the audio file
     * @param filePath full path to the file on disk
     * @param category category this clip belongs to
     * @param tags searchable tags for the clip
     * @param duration length of the clip in seconds
     */
    public AudioClip(String fileName,
                     String filePath,
                     String category,
                     String tags,
                     double duration) {

        this.fileName = fileName;
        this.filePath = filePath;
        this.category = category;
        this.tags = tags;
        this.duration = duration;
    }

    /**
     * Returns the file name of this clip.
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * Returns the file path of this clip.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Returns the category this clip is assigned to.
     */
    public String getCategory() {
        return category;
    }

    /**
     * Returns the tags associated with this clip.
     */
    public String getTags() {
        return tags;
    }

    /**
     * Returns the duration of this clip in seconds.
     */
    public double getDuration() {
        return duration;
    }

    /**
     * Sets the file name of this clip.
     */
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    /**
     * Sets the file path of this clip.
     */
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    /**
     * Sets the category of this clip.
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * Sets the tags for this clip.
     */
    public void setTags(String tags) {
        this.tags = tags;
    }

    /**
     * Sets the duration of this clip in seconds.
     */
    public void setDuration(double duration) {
        this.duration = duration;
    }

    /**
     * Checks whether the audio file actually exists on disk.
     * Returns false if the path is null or the file is not found.
     */
    public boolean fileExists() {
        if (filePath == null) {
            return false;
        }
        File file = new File(filePath);
        return file.exists();
    }

    /**
     * Returns the file name as the string representation of this clip.
     */
    @Override
    public String toString() {
        return fileName;
    }
}