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

import android.content.Context;

import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class MapManifest
{
    private final static List<MapDefinition> maps = new ArrayList<>();
    private static void add(MapDefinition definition) {
        maps.add(definition);
    }
    public static List<MapDefinition> getDefinitions() {
        return maps;
    }

    public static void initBackgroundItems(Context context, Map<String, WorldMapBackgroundItem> map)
    {
        List<MapDefinition> definitions = MapManifest.getDefinitions();
        for (int i=0; i<definitions.size(); i++)
        {
            MapDefinition definition = definitions.get(i);
            if (definition != null && !definition.isInitialized())
            {
                definition.initialize(context);
                if (!map.containsKey(definition.getID())) {
                    map.put(definition.getID(), definition);
                }
            }
        }
    }

    /*
     * To add maps to hardcoded manifest:
     * 1) copy the background file into `assets/maps`
     * 2) extends from `MapDefinition`
     * 3) call add from the static block below
     * 4) include attribution in `about-media` array
     */
    static {
        add(new BaseMaps.BaseMap_AEQD_North());
        add(new BaseMaps.BaseMap_AEQD_South());
        add(new BaseMaps.BaseMap_AEQD_Cairo());
        add(new BaseMaps.BaseMap_AEQD_Phoenix());
        add(new BaseMaps.BaseMap_ECQ());
        add(new BaseMaps.BaseMap_MERC());
        add(new BaseMaps.BaseMap_SINU());
        add(new BaseMaps.BaseMap_VANDG());
    }

}
