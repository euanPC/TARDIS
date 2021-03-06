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
package me.eccentric_nz.TARDIS.rooms;

import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 * @author eccentric_nz
 */
class TARDISRoomDirection {

    private final Block b;
    private boolean found;
    private BlockFace face;
    private COMPASS compass;

    TARDISRoomDirection(Block b) {
        this.b = b;
        found = false;
    }

    /**
     * Gets the compass the room should be grown by finding the pressure plate in front of the door.
     */
    public void getDirection() {
        for (COMPASS c : COMPASS.values()) {
            BlockFace tmp = BlockFace.valueOf(c.toString());
            Material plate = b.getRelative(tmp).getType();
            if (Tag.WOODEN_PRESSURE_PLATES.isTagged(plate) || plate.equals(Material.STONE_PRESSURE_PLATE)) {
                face = tmp;
                found = true;
                compass = c;
            }
        }
    }

    public boolean isFound() {
        return found;
    }

    public BlockFace getFace() {
        return face;
    }

    public COMPASS getCompass() {
        return compass;
    }
}
