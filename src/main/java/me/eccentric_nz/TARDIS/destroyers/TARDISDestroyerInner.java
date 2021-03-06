/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.TARDIS.destroyers;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISInteriorPostioning;
import me.eccentric_nz.TARDIS.builders.TARDISTIPSData;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.Chest;

import java.util.Collections;
import java.util.HashMap;

/**
 * Destroys the inner TARDIS.
 * <p>
 * If a TARDIS landed in the same space and time as another TARDIS, a time ram could occur, destroying both TARDISes,
 * their occupants and even cause a black hole that would tear a hole in the universe
 *
 * @author eccentric_nz
 */
public class TARDISDestroyerInner {

    private final TARDIS plugin;

    public TARDISDestroyerInner(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Destroys the inside of the TARDIS.
     *
     * @param schm the name of the schematic file to use can be DEFAULT, BIGGER or DELUXE.
     * @param id   the unique key of the record for this TARDIS in the database.
     * @param w    the world where the TARDIS is to be built.
     * @param slot the TIPS slot number
     */
    public void destroyInner(SCHEMATIC schm, int id, World w, int slot) {
        // destroy TARDIS
        if (!plugin.getConfig().getBoolean("creation.create_worlds") && !plugin.getConfig().getBoolean("creation.default_world")) {
            plugin.debug(TARDIS.plugin.getLanguage().getString("CONFIG_CREATION_WORLD"));
            return;
        }
        Location wgl;
        TARDISInteriorPostioning tips = new TARDISInteriorPostioning(plugin);
        TARDISTIPSData coords;
        if (schm.getPermission().equals("junk")) {
            coords = tips.getTIPSJunkData();
        } else {
            coords = tips.getTIPSData(slot);
        }
        tips.reclaimChunks(w, id);
        wgl = new Location(w, coords.getMinX(), 64, coords.getMinZ());
        // remove blocks saved to blocks table (iron/gold/diamond/emerald)
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        plugin.getQueryFactory().doDelete("blocks", where);
        // remove from protectBlockMap - remove(id) would only remove the first one
        plugin.getGeneralKeeper().getProtectBlockMap().values().removeAll(Collections.singleton(id));
        if (plugin.isWorldGuardOnServer()) {
            plugin.getWorldGuardUtils().removeRegion(wgl);
        }
    }

    // Originally stolen from Babarix. Thank you :)
    private Chest getDoubleChest(Block block) {
        Chest chest = null;
        if (block.getRelative(BlockFace.NORTH).getType().equals(Material.CHEST)) {
            chest = (Chest) block.getRelative(BlockFace.NORTH).getState();
            return chest;
        } else if (block.getRelative(BlockFace.EAST).getType().equals(Material.CHEST)) {
            chest = (Chest) block.getRelative(BlockFace.EAST).getState();
            return chest;
        } else if (block.getRelative(BlockFace.SOUTH).getType().equals(Material.CHEST)) {
            chest = (Chest) block.getRelative(BlockFace.SOUTH).getState();
            return chest;
        } else if (block.getRelative(BlockFace.WEST).getType().equals(Material.CHEST)) {
            chest = (Chest) block.getRelative(BlockFace.WEST).getState();
            return chest;
        }
        return null;
    }
}
