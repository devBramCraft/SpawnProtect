package bramcraft.spawnprotect;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnProtect extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("SpawnProtect enabled");
    }

    public boolean isInSpawnZone(Location loc) {
        FileConfiguration config = getConfig();
        if (!config.contains("spawnzone")) return false;

        String worldName = config.getString("spawnzone.world");
        World world = Bukkit.getWorld(worldName);
        if (world == null || !loc.getWorld().equals(world)) return false;

        int x1 = config.getInt("spawnzone.pos1.x");
        int y1 = config.getInt("spawnzone.pos1.y");
        int z1 = config.getInt("spawnzone.pos1.z");
        int x2 = config.getInt("spawnzone.pos2.x");
        int y2 = config.getInt("spawnzone.pos2.y");
        int z2 = config.getInt("spawnzone.pos2.z");

        int minX = Math.min(x1, x2), maxX = Math.max(x1, x2);
        int minY = Math.min(y1, y2), maxY = Math.max(y1, y2);
        int minZ = Math.min(z1, z2), maxZ = Math.max(z1, z2);

        int x = loc.getBlockX();
        int y = loc.getBlockY();
        int z = loc.getBlockZ();

        return x >= minX && x <= maxX && y >= minY && y <= maxY && z >= minZ && z <= maxZ;
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (isInSpawnZone(event.getBlock().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou can't break blocks in the spawn area!");
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        String msg = event.getMessage().toLowerCase();
        if ((msg.startsWith("/cpvpbot") || msg.startsWith("/cpvpbot ")) &&
                isInSpawnZone(event.getPlayer().getLocation())) {
            event.setCancelled(true);
            event.getPlayer().sendMessage("§cYou can't use this command in the spawn area!");
        }
    }
}
