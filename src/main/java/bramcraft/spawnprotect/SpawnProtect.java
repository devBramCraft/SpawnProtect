package bramcraft.spawnprotect;

import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.*;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class SpawnProtect extends JavaPlugin implements Listener {

    @Override
    public void onEnable() {
        saveDefaultConfig();
        Bukkit.getPluginManager().registerEvents(this, this);
        getCommand("setpos1").setExecutor(new SetPosCommand("pos1"));


        getCommand("setpos2").setExecutor(new SetPosCommand("pos2"));
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
        Player player = event.getPlayer();

        if (isInSpawnZone(event.getBlock().getLocation())) {
            if (player.hasPermission("spawnprotect.bypass.break")) return;

            event.setCancelled(true);
            player.sendMessage("§cYou can't break blocks in the spawn area!");
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

    public class SetPosCommand implements CommandExecutor {
        private final String pos;

        public SetPosCommand(String pos) {
            this.pos = pos;
        }

        @Override
        public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            if (!(sender instanceof Player player)) return true;

            if (!player.hasPermission("spawnprotect.setpos")) {
                player.sendMessage("§cYou don't have permission to use this command.");
                return true;
            }

            Location loc = player.getLocation();
            FileConfiguration config = getConfig();
            config.set("spawnzone.world", loc.getWorld().getName());
            config.set("spawnzone." + pos + ".x", loc.getBlockX());
            config.set("spawnzone." + pos + ".y", loc.getBlockY());
            config.set("spawnzone." + pos + ".z", loc.getBlockZ());
            saveConfig();

            player.sendMessage("§aSpawn zone " + pos + " set at your location.");
            return true;
        }
    }
    }