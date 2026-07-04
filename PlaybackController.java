import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.File;
import java.util.List;

public class PlaybackController {

    private MediaPlayer mediaPlayer;
    private List<AudioClip> timelineClips;
    private int currentTimelineIndex;
    private boolean loop;
    private boolean timelineMode;

    public boolean isLoop() {
        return loop;
    }

    public void playClip(AudioClip clip) {
        if (clip == null) {
            System.out.println("No clip selected.");
            return;
        }

        if (!clip.fileExists()) {
            System.out.println("File not found: " + clip.getFilePath());
            return;
        }

        timelineMode = false;
        stopClip();
        startMediaPlayer(clip, null);
    }

    public void playTimeline(List<AudioClip> clips) {
        if (clips == null || clips.isEmpty()) {
            System.out.println("No timeline clips to play.");
            return;
        }

        stopClip();
        timelineClips = clips;
        currentTimelineIndex = 0;
        timelineMode = true;
        playCurrentTimelineClip();
    }

    private void playCurrentTimelineClip() {
        if (currentTimelineIndex >= timelineClips.size()) {
            timelineMode = false;
            System.out.println("Timeline playback finished.");
            return;
        }

        AudioClip clip = timelineClips.get(currentTimelineIndex);

        startMediaPlayer(clip, () -> {
            currentTimelineIndex++;
            playCurrentTimelineClip();
        });
    }

    private void startMediaPlayer(AudioClip clip, Runnable onFinished) {
        try {
            disposeCurrentPlayer();

            File file = new File(clip.getFilePath());
            String uri = file.toURI().toString();

            System.out.println("Trying to play: " + uri);

            Media media = new Media(uri);

            media.setOnError(() -> {
                System.out.println("Media error: " + media.getError());
            });

            mediaPlayer = new MediaPlayer(media);

            mediaPlayer.setOnError(() -> {
                System.out.println("MediaPlayer error: " + mediaPlayer.getError());
            });

            mediaPlayer.setOnReady(() -> {
                System.out.println("Media ready. Playing: " + clip.getFileName());

                if (!timelineMode && loop) {
                    mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
                }

                mediaPlayer.play();
            });

            if (onFinished != null) {
                mediaPlayer.setOnEndOfMedia(onFinished);
            }

        } catch (Exception e) {
            System.out.println("Playback exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void pauseClip() {
        if (mediaPlayer != null) {
            mediaPlayer.pause();
            System.out.println("Pausing playback.");
        }
    }

    public void resumeClip() {
        if (mediaPlayer != null) {
            mediaPlayer.play();
            System.out.println("Resuming playback.");
        }
    }

    public void stopClip() {
        disposeCurrentPlayer();

        if (timelineMode) {
            timelineMode = false;
            currentTimelineIndex = 0;
        }
    }

    private void disposeCurrentPlayer() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.dispose();
            mediaPlayer = null;
        }
    }

    public void setLoop(boolean loop) {
        this.loop = loop;
        System.out.println("Loop mode: " + (loop ? "enabled" : "disabled"));
    }
}
