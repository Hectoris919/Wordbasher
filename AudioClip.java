// Data model representing a single audio clip.
// Stores metadata including file name, file path, category, tags, and clip duration.

public class AudioClip {

    private String fileName;
    private String filePath;
    private String category;
    private String tags;
    private double duration;

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

    // Getters

    public String getFileName() {
        return fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public String getCategory() {
        return category;
    }

    public String getTags() {
        return tags;
    }

    public double getDuration() {
        return duration;
    }

    // Setters

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public void setDuration(double duration) {
        this.duration = duration;
    }

    @Override
    public String toString() {
        return fileName;
    }
}