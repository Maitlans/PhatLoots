package com.codisimus.plugins.phatloots;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Manages particle effects for PhatLootChests
 *
 * @author Codisimus
 */
public class ParticleManager implements Runnable {
    private static long tick = 0;
    private final Location reusableLoc = new Location(null, 0, 0, 0);

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

                // Reuse location object for distance check
                reusableLoc.setWorld(world);
                reusableLoc.setX(chest.getX() + 0.5);
                reusableLoc.setY(chest.getY() + 0.5);
                reusableLoc.setZ(chest.getZ() + 0.5);

                // Check for players nearby to save performance
                boolean playerNearby = false;
                List<Player> players = world.getPlayers();
                for (int i = 0; i < players.size(); i++) {
                    Player player = players.get(i);
                    if (player.getLocation().distanceSquared(reusableLoc) < 256) { // 16 blocks
                        playerNearby = true;
                        break;
                    }
                }

                if (!playerNearby) {
                    continue;
                }

                // Adjust for offsets
                reusableLoc.add(phatLoot.particleXOffset, phatLoot.particleYOffset + phatLoot.particleHeight, phatLoot.particleZOffset);

                world.spawnParticle(
                        particle,
                        reusableLoc,
                        phatLoot.particleAmount,
                        0.1, 0.1, 0.1, // small spread
                        phatLoot.particleSpeed
                );
            }
        }
    }
}
