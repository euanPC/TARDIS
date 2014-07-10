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
package me.eccentric_nz.TARDIS.arch;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.TARDISDatabaseConnection;
import me.libraryaddict.disguise.DisguiseAPI;
import me.libraryaddict.disguise.disguisetypes.PlayerDisguise;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISArchPersister {

    private final TARDIS plugin;
    private final TARDISDatabaseConnection service = TARDISDatabaseConnection.getInstance();
    private final Connection connection = service.getConnection();
    private PreparedStatement ps = null;
    private ResultSet rs = null;
    private int count = 0;

    public TARDISArchPersister(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void saveAll() {
        try {
            // save the arched players
            ps = connection.prepareStatement("INSERT INTO arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
            for (Map.Entry<UUID, TARDISWatchData> map : plugin.getTrackerKeeper().getJohnSmith().entrySet()) {
                ps = connection.prepareStatement("SELECT uuid FROM arched WHERE uuid = ?");
                ps.setString(1, map.getKey().toString());
                rs = ps.executeQuery();
                TARDISWatchData twd = map.getValue();
                long time = 0;
                long now = System.currentTimeMillis();
                if (now < twd.getTime()) {
                    // get time remaining
                    time = twd.getTime() - now;
                }
                if (rs.next()) {
                    // update the existing record
                    ps = connection.prepareStatement("UPDATE arched SET arch_name = ?, arch_time = ? WHERE uuid = ?");
                    ps.setString(1, twd.getName());
                    ps.setLong(2, time);
                    ps.setString(3, map.getKey().toString());
                } else {
                    // save the arched player
                    ps = connection.prepareStatement("INSERT INTO arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
                    ps.setString(1, map.getKey().toString());
                    ps.setString(2, twd.getName());
                    ps.setLong(3, time);
                }
                count += ps.executeUpdate();
            }
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " 'arched' players.");
        } catch (SQLException ex) {
            plugin.debug("Insert error for arched table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing arched statement: " + ex.getMessage());
            }
        }
    }

    public void save(UUID uuid) {
        try {
            ps = connection.prepareStatement("SELECT uuid FROM arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            TARDISWatchData twd = plugin.getTrackerKeeper().getJohnSmith().get(uuid);
            long time = 0;
            long now = System.currentTimeMillis();
            if (now < twd.getTime()) {
                // get time remaining
                time = twd.getTime() - now;
            }
            if (rs.next()) {
                // update the existing record
                ps = connection.prepareStatement("UPDATE arched SET arch_name = ?, arch_time = ? WHERE uuid = ?");
                ps.setString(1, twd.getName());
                ps.setLong(2, time);
                ps.setString(3, uuid.toString());
            } else {
                // save the arched player
                ps = connection.prepareStatement("INSERT INTO arched (uuid, arch_name, arch_time) VALUES (?, ?, ?)");
                ps.setString(1, uuid.toString());
                ps.setString(2, twd.getName());
                ps.setLong(3, time);
            }
            count += ps.executeUpdate();
            plugin.getConsole().sendMessage(plugin.getPluginName() + "Saved " + count + " 'arched' player.");
        } catch (SQLException ex) {
            plugin.debug("Insert error for arched table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing arched statement: " + ex.getMessage());
            }
        }
    }

    public void reArch(UUID uuid) {
        try {
            ps = connection.prepareStatement("SELECT * FROM arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            rs = ps.executeQuery();
            if (rs.next()) {
                Player player = plugin.getServer().getPlayer(uuid);
                if (player.isOnline()) {
                    // disguise the player
                    final String name = rs.getString("arch_name");
                    long time = System.currentTimeMillis() + rs.getLong("arch_time");
                    TARDISWatchData twd = new TARDISWatchData(name, time);
                    plugin.getTrackerKeeper().getJohnSmith().put(uuid, twd);
                    if (DisguiseAPI.isDisguised(player)) {
                        DisguiseAPI.undisguiseToAll(player);
                    }
                    PlayerDisguise playerDisguise = new PlayerDisguise(name);
                    DisguiseAPI.disguiseToAll(player, playerDisguise);
                    player.setDisplayName(name);
                    player.setPlayerListName(name);
                }
            }
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for arched table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
                if (rs != null) {
                    rs.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing arched statement or resultset: " + ex.getMessage());
            }
        }
    }

    public void checkAll() {
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                try {
                    ps = connection.prepareStatement("SELECT * FROM arched");
                    rs = ps.executeQuery();
                    if (rs.isBeforeFirst()) {
                        while (rs.next()) {
                            Player player = plugin.getServer().getPlayer(UUID.fromString(rs.getString("uuid")));
                            if (player != null && player.isOnline() && !DisguiseAPI.isDisguised(player)) {
                                // disguise the player
                                final String name = rs.getString("arch_name");
                                long time = System.currentTimeMillis() + rs.getLong("arch_time");
                                TARDISWatchData twd = new TARDISWatchData(name, time);
                                plugin.getTrackerKeeper().getJohnSmith().put(player.getUniqueId(), twd);
                                DisguiseAPI.undisguiseToAll(player);
                                PlayerDisguise playerDisguise = new PlayerDisguise(name);
                                DisguiseAPI.disguiseToAll(player, playerDisguise);
                                player.setDisplayName(name);
                                player.setPlayerListName(name);
                            }
                        }
                    }
                } catch (SQLException ex) {
                    plugin.debug("ResultSet error for arched table: " + ex.getMessage());
                } finally {
                    try {
                        if (ps != null) {
                            ps.close();
                        }
                        if (rs != null) {
                            rs.close();
                        }
                    } catch (SQLException ex) {
                        plugin.debug("Error closing arched statement or resultset: " + ex.getMessage());
                    }
                }

            }
        }, 30L);
    }

    public void removeArch(UUID uuid) {
        try {
            ps = connection.prepareStatement("DELETE FROM arched WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException ex) {
            plugin.debug("ResultSet error for arched table: " + ex.getMessage());
        } finally {
            try {
                if (ps != null) {
                    ps.close();
                }
            } catch (SQLException ex) {
                plugin.debug("Error closing arched statement or resultset: " + ex.getMessage());
            }
        }
    }
}