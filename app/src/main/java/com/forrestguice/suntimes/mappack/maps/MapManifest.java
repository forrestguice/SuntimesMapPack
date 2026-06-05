/**
    Copyright (C) 2026 Forrest Guice
    This file is part of SuntimesMapPack.

    SuntimesMapPack is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SuntimesMapPack is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SuntimesMapPack.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.maps.bluemarble.*;

import java.util.ArrayList;
import java.util.List;

public final class MapManifest
{
    private final static List<MapDefinition> maps = new ArrayList<>();
    private static void add(MapDefinition definition) {
        maps.add(definition);
    }
    public static List<MapDefinition> getDefinitions() {
        return maps;
    }

    /*
     * To add maps:
     * 1) copy the background file into `assets/maps`
     * 2) extends from `MapDefinition`
     * 3) call add from the static block below
     * 4) include attribution in `about-media` array
     */
    static {
        add(new BlueMarble_Bathy_AEQD_Cairo());
        add(new BlueMarble_Bathy_AEQD_North());
        add(new BlueMarble_Bathy_AEQD_Phoenix());
        add(new BlueMarble_Bathy_AEQD_South());
        add(new BlueMarble_Bathy_ECQ());
        add(new BlueMarble_Bathy_MERC());
        add(new BlueMarble_Bathy_VANDG());
        add(new BlueMarble_Topo_AEQD_North());
        add(new BlueMarble_Topo_AEQD_Phoenix());
        add(new BlueMarble_Topo_AEQD_South());
        add(new BlueMarble_Topo_ECQ());
    }
}
