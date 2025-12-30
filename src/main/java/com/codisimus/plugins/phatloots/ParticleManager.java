package com.codisimus.plugins.phatloots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 * Manages particle effects for PhatLootChests
 *
 * @author Codisimus
 */
public class ParticleManager implements Runnable {
    private static long tick = 0;

    @Override
    public void run() {
        tick++;
        for (PhatLoot phatLoot : PhatLoots.getPhatLoots()) {
            if (phatLoot.particleType == null || phatLoot.particleType.isEmpty()) {
                continue;
            }

            if (tick % phatLoot.particleDelay != 0) {
                continue;
            }

            Particle particle;
            try {
                particle = Particle.valueOf(phatLoot.particleType.toUpperCase());
            } catch (Exception ex) {
                continue;
            }

            for (PhatLootChest chest : phatLoot.getChests()) {
                if (PhatLootChest.chestsToRespawn.contains(chest)) {
                    continue;
                }

                World world = Bukkit.getWorld(chest.getWorldName());
                if (world == null || !world.isChunkLoaded(chest.getX() >> 4, chest.getZ() >> 4)) {
                    continue;
                }

                // Check for players nearby to save performance
                boolean playerNearby = false;
                for (Player player : world.getPlayers()) {
                    if (player.getLocation().distanceSquared(new Location(world, chest.getX(), chest.getY(), chest.getZ())) < 256) { // 16 blocks
                        playerNearby = true;
                        break;
                    }
                }

                if (!playerNearby) {
                    continue;
                }

                Location loc = new Location(world, chest.getX() + 0.5, chest.getY() + 0.5, chest.getZ() + 0.5);
                loc.add(phatLoot.particleXOffset, phatLoot.particleYOffset + phatLoot.particleHeight, phatLoot.particleZOffset);

                world.spawnParticle(
                        particle,
                        loc,
                        phatLoot.particleAmount,
                        0.1, 0.1, 0.1, // small spread
                        phatLoot.particleSpeed
                );
            }
        }
    }
}
