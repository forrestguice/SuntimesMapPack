package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_SINU extends MapDefinition
{
    public MapDefinition_SINU()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_SINU;
        this.map_projection_label = SuntimesMapAssets.MapProjections.SINUISOIDAL.getDisplayString();
    }
}
