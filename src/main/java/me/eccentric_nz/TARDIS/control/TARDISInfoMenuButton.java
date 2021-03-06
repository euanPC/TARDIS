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
package me.eccentric_nz.TARDIS.control;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.chatGUI.TARDISUpdateChatGUI;
import me.eccentric_nz.TARDIS.info.TARDISInfoMenu;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
public class TARDISInfoMenuButton {

    private final TARDIS plugin;
    private final Player player;

    private final String JSON = "{\"text\":\"%s\",\"color\":\"gold\",\"extra\":[{\"text\":\"%s\",\"color\":\"white\",\"clickEvent\":{\"action\":\"run_command\",\"value\":\"/tardisinfo %s\"},\"hoverEvent\": {\"action\": \"show_text\",\"value\": {\"text\": \"Click me!\"}},\"extra\":[{\"text\":\"%s\",\"color\":\"gold\"}]}]}";

    public TARDISInfoMenuButton(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
    }

    public void clickButton() {
        plugin.getTrackerKeeper().getInfoMenu().put(player.getUniqueId(), TARDISInfoMenu.TIS);
        player.sendMessage(ChatColor.GOLD + "-----------TARDIS Information System-----------");
        player.sendMessage(ChatColor.GOLD + "---*" + plugin.getLanguage().getString("TIS_INFO") + "*---");
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> TARDIS ", "M", "M", "anual"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "I", "I", "tems"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "C", "C", "omponents"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "S", "S", "onic Components"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "D", "D", "isks"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> C", "o", "o", "mmands"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "T", "T", "ARDIS Types"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "R", "R", "ooms"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "F", "F", "ood & Accessories"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "P", "P", "lanets"), player);
        TARDISUpdateChatGUI.sendJSON(String.format(JSON, "> ", "E", "E", "xit"), player);
    }
}
