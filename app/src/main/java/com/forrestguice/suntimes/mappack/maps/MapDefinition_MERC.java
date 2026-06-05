package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_MERC extends MapDefinition
{
    public MapDefinition_MERC()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_MERC;
        this.map_projection_label = SuntimesMapAssets.MapProjections.MERCATOR.getDisplayString();
    }
}
