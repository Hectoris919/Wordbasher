public class TimelineClip {

    private int channelNumber;
    private AudioClip clip;

    public TimelineClip(int channelNumber, AudioClip clip) {
        this.channelNumber = channelNumber;
        this.clip = clip;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public AudioClip getClip() {
        return clip;
    }

    public String getFileName() {
        return clip.getFileName();
    }

    @Override
    public String toString() {
        return "Channel " + channelNumber + ": " + clip.getFileName();
    }
}
