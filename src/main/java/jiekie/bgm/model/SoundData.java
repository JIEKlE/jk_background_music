package jiekie.bgm.model;

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

    public float getVolume() {
        return volume;
    }

    public int getDuration() {
        return duration;
    }
}
