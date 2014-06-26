/*
 * Copyright (C) 2014 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISUpdateCommand {

    private final TARDIS plugin;

    public TARDISUpdateCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean startUpdate(Player player, String[] args) {
        if (player.hasPermission("tardis.update")) {
            String[] validBlockNames = {"advanced", "ars", "artron", "back", "backdoor", "button", "chameleon", "condenser", "creeper", "direction", "door", "eps", "farm", "handbrake", "hinge", "info", "keyboard", "light", "rail", "save-sign", "scanner", "stable", "storage", "temporal", "terminal", "toggle_wool", "village", "world-repeater", "x-repeater", "y-repeater", "z-repeater", "zero"};
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            String tardis_block = args[1].toLowerCase(Locale.ENGLISH);
            if (!Arrays.asList(validBlockNames).contains(tardis_block)) {
                new TARDISUpdateLister(plugin, player).list();
                return true;
            }
            if (tardis_block.equals("hinge")) {
                Block block = player.getTargetBlock(null, 10);
                if (block.getType().equals(Material.IRON_DOOR_BLOCK)) {
                    if (args.length == 3) {
                        byte b = plugin.getUtils().parseByte(args[2]);
                        block.setData(b, true);
                    } else {
                        byte blockData = block.getData();
                        if (blockData == 8) {
                            block.setData((byte) 9, true);
                        } else {
                            block.setData((byte) 8, true);
                        }
                    }
                }
                return true;
            }
            if (tardis_block.equals("advanced") && !player.hasPermission("tardis.advanced")) {
                TARDISMessage.send(player, "NO_PERM_ADV");
                return true;
            }
            if (tardis_block.equals("storage") && !player.hasPermission("tardis.storage")) {
                TARDISMessage.send(player, "NO_PERM_DISK");
                return true;
            }
            if (tardis_block.equals("backdoor") && !player.hasPermission("tardis.backdoor")) {
                TARDISMessage.send(player, "NO_PERM_BACKDOOR");
                return true;
            }
            if (tardis_block.equals("temporal") && !player.hasPermission("tardis.temporal")) {
                TARDISMessage.send(player, "NO_PERM_TEMPORAL");
                return true;
            }
            if ((tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("village")) && !player.hasPermission("tardis.farm")) {
                TARDISMessage.send(player, "UPDATE_NO_PERM", tardis_block);
                return true;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("uuid", player.getUniqueId().toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            // must grow a room first
            if (tardis_block.equals("farm") || tardis_block.equals("stable") || tardis_block.equals("village") || tardis_block.equals("rail")) {
                if (tardis_block.equals("farm") && rs.getFarm().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_FARM");
                    return true;
                }
                if (tardis_block.equals("stable") && rs.getStable().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_STABLE");
                    return true;
                }
                if (tardis_block.equals("village") && rs.getVillage().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_VILLAGE");
                    return true;
                }
                if (tardis_block.equals("rail") && rs.getRail().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_RAIL");
                    return true;
                }
                if (tardis_block.equals("zero") && rs.getZero().isEmpty()) {
                    TARDISMessage.send(player, "UPDATE_ZERO");
                    return true;
                }
            }
            if (tardis_block.equals("ars")) {
                if (!player.hasPermission("tardis.ars")) {
                    TARDISMessage.send(player, "NO_PERM_ARS");
                    return true;
                }
                if (!plugin.getUtils().canGrowRooms(rs.getChunk())) {
                    TARDISMessage.send(player, "ARS_OWN_WORLD");
                    return true;
                }
                SCHEMATIC schm = rs.getSchematic();
                if (schm.equals(SCHEMATIC.CUSTOM)) {
                    TARDISMessage.send(player, "ARS_NO_CUSTOM");
                    return true;
                }
            }
            if (!tardis_block.equals("backdoor")) {
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("uuid", player.getUniqueId().toString());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
                if (!rst.resultSet()) {
                    TARDISMessage.send(player, "NOT_IN_TARDIS");
                    return false;
                }
            }
            plugin.getTrackerKeeper().getPlayers().put(player.getUniqueId(), tardis_block);
            TARDISMessage.send(player, "UPDATE_CLICK", tardis_block);
            if (tardis_block.equals("direction")) {
                TARDISMessage.send(player, "HOOK_REMIND");
            }
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
