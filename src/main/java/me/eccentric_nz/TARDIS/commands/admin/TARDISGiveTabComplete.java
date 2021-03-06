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
package me.eccentric_nz.TARDIS.commands.admin;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * TabCompleter for /tardisgive
 */
public class TARDISGiveTabComplete extends TARDISCompleter implements TabCompleter {

    private final ImmutableList<String> GIVE_SUBS = ImmutableList.of("artron", "kit", "a-circuit", "acid-battery", "arrow-circuit", "ars-circuit", "battery", "blaster", "bow-tie", "bio-circuit", "biome-disk", "blank", "c-circuit", "cell", "communicator", "custard", "d-circuit", "e-circuit", "filter", "fish-finger", "furnace", "generator", "glasses", "handles", "i-circuit", "ignite-circuit", "invisible", "jammy-dodger", "jelly-baby", "key", "keyboard", "l-circuit", "locator", "m-circuit", "memory-circuit", "mushroom", "oscillator", "pad", "painter", "paper-bag", "player-disk", "preset-disk", "p-circuit", "r-circuit", "r-key", "randomiser-circuit", "reader", "remote", "rift-circuit", "rift-manipulator", "rust", "s-circuit", "save-disk", "scanner-circuit", "seed", "sonic", "t-circuit", "telepathic", "tachyon", "vortex", "wand", "watch");
    private final ImmutableList<String> GIVE_KNOWLEDGE = ImmutableList.of("knowledge", "1", "2", "64");
    private final ImmutableList<String> KIT_SUBS;
    private final List<String> SEED_SUBS = new ArrayList<>();
    private final ImmutableList<String> MUSHROOM_SUBS = ImmutableList.of("the_moment", "siege_cube", "tardis_blue", "tardis_white", "tardis_orange", "tardis_magenta", "tardis_light_blue", "tardis_yellow", "tardis_lime", "tardis_pink", "tardis_gray", "tardis_light_gray", "tardis_cyan", "tardis_purple", "tardis_brown", "tardis_green", "tardis_red", "tardis_black", "tardis_blue_south", "tardis_white_south", "tardis_orange_south", "tardis_magenta_south", "tardis_light_blue_south", "tardis_yellow_south", "tardis_lime_south", "tardis_pink_south", "tardis_gray_south", "tardis_light_gray_south", "tardis_cyan_south", "tardis_purple_south", "tardis_brown_south", "tardis_green_south", "tardis_red_south", "tardis_black_south", "tardis_blue_west", "tardis_white_west", "tardis_orange_west", "tardis_magenta_west", "tardis_light_blue_west", "tardis_yellow_west", "tardis_lime_west", "tardis_pink_west", "tardis_gray_west", "tardis_light_gray_west", "tardis_cyan_west", "tardis_purple_west", "tardis_brown_west", "tardis_green_west", "tardis_red_west", "tardis_black_west", "tardis_blue_north", "tardis_white_north", "tardis_orange_north", "tardis_magenta_north", "tardis_light_blue_north", "tardis_yellow_north", "tardis_lime_north", "tardis_pink_north", "tardis_gray_north", "tardis_light_gray_north", "tardis_cyan_north", "tardis_purple_north", "tardis_brown_north", "tardis_green_north", "tardis_red_north", "tardis_black_north", "ars", "bigger", "budget", "coral", "deluxe", "eleventh", "ender", "plank", "pyramid", "redstone", "steampunk", "thirteenth", "factory", "tom", "twelfth", "war", "small", "medium", "tall", "legacy_bigger", "legacy_budget", "legacy_deluxe", "legacy_eleventh", "legacy_redstone", "pandorica", "master", "creative", "compound", "reducer", "constructor", "lab", "product", "blue_lamp_on", "green_lamp_on", "purple_lamp_on", "red_lamp_on", "blue_lamp", "green_lamp", "purple_lamp", "red_lamp", "heat_block", "custom", "hexagon", "roundel", "roundel_offset", "cog", "advanced_console", "disk_storage", "lamp_off", "lantern_off", "blue_box");
    private final List<String> MAT_SUBS = new ArrayList<>();

    public TARDISGiveTabComplete(TARDIS plugin) {
        Set<String> kits = plugin.getKitsConfig().getConfigurationSection("kits").getKeys(false);
        KIT_SUBS = ImmutableList.copyOf(kits);
        for (String seed : CONSOLES.getBY_NAMES().keySet()) {
            if (!seed.equals("SMALL") && !seed.equals("MEDIUM") && !seed.equals("TALL") && !seed.equals("ARCHIVE")) {
                SEED_SUBS.add(seed);
            }
        }
        TARDISWalls.BLOCKS.forEach((m) -> MAT_SUBS.add(m.toString()));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        String lastArg = args[args.length - 1];
        if (args.length <= 1) {
            return null;
        } else if (args.length == 2) {
            return partial(lastArg, GIVE_SUBS);
        } else if (args.length == 3) {
            String sub = args[1];
            if (sub.equals("kit")) {
                return partial(lastArg, KIT_SUBS);
            }
            if (sub.equals("seed")) {
                return partial(lastArg, SEED_SUBS);
            }
            return partial(lastArg, GIVE_KNOWLEDGE);
        } else if (args.length == 4 && args[1].equalsIgnoreCase("mushroom")) {
            return partial(lastArg, MUSHROOM_SUBS);
        } else if (args.length >= 4 && args[1].equalsIgnoreCase("seed")) {
            return partial(lastArg, MAT_SUBS);
        }
        return ImmutableList.of();
    }
}
