package jiekie.model;

public class SoundData {
    private String sound;
    private float volume;
    private int duration;

    public SoundData(String sound, float volume, int duration) {
        this.sound = sound;
        this.volume = volume;
        this.duration = duration;
    }

    public String getSound() {
        return sound;
    }

    public void setSound(String sound) {
        this.sound = sound;
    }

    public float getVolume() {
        return volume;
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
