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
package me.eccentric_nz.TARDIS.api.event;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetTardisArtron;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class TARDISArtronEvent extends Event {

    private final Player player;
    private final int amount;
    private final int tardis_id;
    private int level = 0;
    private static final HandlerList HANDLERS = new HandlerList();

    public TARDISArtronEvent(Player player, int amount, int tardis_id) {
        this.player = player;
        this.amount = amount;
        this.tardis_id = tardis_id;
        ResultSetTardisArtron rs = new ResultSetTardisArtron(TARDIS.plugin);
        if (rs.fromID(this.tardis_id)) {
            level = rs.getArtronLevel();
        }
    }

    /**
     * Returns the player involved in this event.
     *
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Returns the amount of energy involved in this event. This could be a positive or negative amount.
     *
     * @return the amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Returns the TARDIS id involved in this event.
     *
     * @return the TARDIS id
     */
    public int getTardis_id() {
        return tardis_id;
    }

    /**
     * Returns the Artron Level after the amount has been added / subtracted.
     *
     * @return the player
     */
    public int getLevel() {
        return level;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }
}
