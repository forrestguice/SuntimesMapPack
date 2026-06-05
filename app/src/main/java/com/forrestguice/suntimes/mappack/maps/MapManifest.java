package com.forrestguice.suntimes.mappack.maps;

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
     * 1) copy the background file into assets/maps
     * 2) extends from MapDefinition
     * 3) call add from the static block below
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
