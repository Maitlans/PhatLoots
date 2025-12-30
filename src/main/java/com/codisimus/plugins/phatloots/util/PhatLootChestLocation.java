package com.codisimus.plugins.phatloots.util;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.util.Objects;

/**
 * Represents a physical block location for efficient Map lookups
 *
 * @author Codisimus
 */
public final class PhatLootChestLocation {
    private final String worldName;
    private final int x, y, z;
    private final int hashCode;

    public PhatLootChestLocation(Block block) {
        this(block.getWorld().getName(), block.getX(), block.getY(), block.getZ());
    }

    public PhatLootChestLocation(Location loc) {
        this(loc.getWorld().getName(), loc.getBlockX(), loc.getBlockY(), loc.getBlockZ());
    }

    public PhatLootChestLocation(String worldName, int x, int y, int z) {
        this.worldName = worldName;
        this.x = x;
        this.y = y;
        this.z = z;
        this.hashCode = Objects.hash(worldName, x, y, z);
    }

    public String getWorldName() {
        return worldName;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getZ() {
        return z;
    }

    public Location toLocation() {
        World world = Bukkit.getWorld(worldName);
        return world != null ? new Location(world, x, y, z) : null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof PhatLootChestLocation)) return false;
        PhatLootChestLocation that = (PhatLootChestLocation) o;
        return x == that.x && y == that.y && z == that.z && worldName.equals(that.worldName);
    }

    @Override
    public int hashCode() {
        return hashCode;
    }

    @Override
    public String toString() {
        return worldName + "'" + x + "'" + y + "'" + z;
    }
}
