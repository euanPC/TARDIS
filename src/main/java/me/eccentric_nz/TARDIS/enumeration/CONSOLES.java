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
package me.eccentric_nz.TARDIS.enumeration;

import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeMap;

/**
 * @author eccentric_nz
 */
public class CONSOLES {

    private final static TreeMap<String, SCHEMATIC> BY_NAMES = new TreeMap<>();
    private final static HashMap<String, SCHEMATIC> BY_PERMS = new HashMap<>();
    private final static HashMap<String, SCHEMATIC> BY_MATERIALS = new HashMap<>();
    private final static List<SCHEMATIC> NO_BEACON = new ArrayList<>();

    /**
     * Attempts to get the SCHEMATIC with the given name.
     *
     * @param perm permission node of the SCHEMATIC to get
     * @return SCHEMATIC if found, or null
     */
    public static SCHEMATIC SCHEMATICFor(String perm) {
        return BY_PERMS.get(perm);
    }

    /**
     * Attempts to get the SCHEMATIC with the given seed Material.
     *
     * @param mat Name of the SCHEMATIC to get
     * @return SCHEMATIC if found, or null
     */
    public static SCHEMATIC SCHEMATICFor(Material mat) {
        return BY_MATERIALS.get(mat.toString());
    }

    public static void loadLookups() {
        for (SCHEMATIC ts : BY_NAMES.values()) {
            if (!BY_PERMS.containsKey(ts.getPermission())) {
                BY_PERMS.put(ts.getPermission(), ts);
            }
            if (!BY_MATERIALS.containsKey(ts.getSeed())) {
                BY_MATERIALS.put(ts.getSeed(), ts);
            }
            if (!NO_BEACON.contains(ts) && !ts.hasBeacon()) {
                NO_BEACON.add(ts);
            }
        }
        BY_PERMS.put("junk", new SCHEMATIC("MILK_BUCKET", "junk", "Junk Console", ConsoleSize.SMALL, false, false, false));
        SCHEMATIC archive = new SCHEMATIC("BONE_BLOCK", "archive", "Archived Console", ConsoleSize.MEDIUM, false, false, true);
        BY_PERMS.put("archive", archive);
        BY_MATERIALS.put("BONE_BLOCK", archive);
    }

    public static TreeMap<String, SCHEMATIC> getBY_NAMES() {
        return BY_NAMES;
    }

    public static HashMap<String, SCHEMATIC> getBY_MATERIALS() {
        return BY_MATERIALS;
    }

    public static List<SCHEMATIC> getNO_BEACON() {
        return NO_BEACON;
    }
}
