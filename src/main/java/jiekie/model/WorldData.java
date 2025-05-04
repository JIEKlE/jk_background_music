package jiekie.model;

import org.bukkit.Location;

public class WorldData {
    private String name;
    private String world;
    private String type;
    private Location minLocation;
    private Location maxLocation;
    private SoundData soundData;

    public WorldData(String name, String world, String type, Location minLocation, Location maxLocation, SoundData soundData) {
        this.name = name;
        this.world = world;
        this.type = type;
        this.minLocation = minLocation;
        this.maxLocation = maxLocation;
        this.soundData = soundData;
    }

    public String getName() {
        return name;
    }

    public String getWorld() {
        return world;
    }

    public void setWorld(String world) {
        this.world = world;
    }

    public String getType() {
        return type;
    }

    public Location getMinLocation() {
        return minLocation;
    }

    public void setMinLocation(Location minLocation) {
        this.minLocation = minLocation;
    }

    public Location getMaxLocation() {
        return maxLocation;
    }

    public void setMaxLocation(Location maxLocation) {
        this.maxLocation = maxLocation;
    }

    public SoundData getSoundData() {
        return soundData;
    }

    public void setSoundData(SoundData soundData) {
        this.soundData = soundData;
    }

    public boolean contains(Location location) {
        return location.getWorld().getName().equals(world)
                && location.getX() >= minLocation.getX()
                && location.getX() <= maxLocation.getX()
                && location.getY() >= minLocation.getY()
                && location.getY() <= maxLocation.getY()
                && location.getZ() >= minLocation.getZ()
                && location.getZ() <= maxLocation.getZ();
    }
}
