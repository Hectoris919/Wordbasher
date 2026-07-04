// Builds a playback order from the clips currently placed on the timeline.

import java.util.ArrayList;
import java.util.List;

public class TimelinePlaybackEngine {

    // Returns all timeline clips in their current channel order.
    public List<AudioClip> getPlaybackOrder(TimelinePanel timelinePanel) {

        List<AudioClip> playbackOrder = new ArrayList<>();

        if (timelinePanel == null) {
            return playbackOrder;
        }

        // Reads each TimelineClip and extracts its AudioClip for playback.
        for (TimelineClip timelineClip : timelinePanel.getTimelineClips()) {

            if (timelineClip != null && timelineClip.getClip() != null) {
                playbackOrder.add(timelineClip.getClip());
            }
        }

        return playbackOrder;
    }

    // Returns true if the timeline contains at least one playable clip.
    public boolean hasPlayableClips(TimelinePanel timelinePanel) {
        return !getPlaybackOrder(timelinePanel).isEmpty();
    }
}
