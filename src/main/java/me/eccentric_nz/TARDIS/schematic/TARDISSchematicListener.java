package me.eccentric_nz.TARDIS.schematic;

import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class TARDISSchematicListener implements Listener {

    private final TARDIS plugin;
    private final Material wand;

    public TARDISSchematicListener(TARDIS plugin) {
        this.plugin = plugin;
        this.wand = getWand();
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!player.getItemInHand().getType().equals(wand)) {
            return;
        }
        Block b = event.getClickedBlock();
        if (b == null) {
            return;
        }
        Location l = b.getLocation();
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            plugin.getTrackerKeeper().getStartLocation().put(uuid, l);
            player.sendMessage(plugin.getPluginName() + "Start block selected!");
        }
        if (event.getAction().equals(Action.LEFT_CLICK_BLOCK)) {
            plugin.getTrackerKeeper().getEndLocation().put(uuid, l);
            player.sendMessage(plugin.getPluginName() + "End block selected!");
        }
        event.setCancelled(true);
    }

    private Material getWand() {
        Material mat;
        try {
            mat = Material.valueOf(this.plugin.getConfig().getString("preferences.wand"));
        } catch (IllegalArgumentException e) {
            mat = Material.BONE;
        }
        return mat;
    }
}
