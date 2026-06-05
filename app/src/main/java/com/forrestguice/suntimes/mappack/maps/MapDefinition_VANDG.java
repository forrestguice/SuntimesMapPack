package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_VANDG extends MapDefinition
{
    public MapDefinition_VANDG()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_VANDG;
        this.map_projection_label = SuntimesMapAssets.MapProjections.VAN_DER_GRINTEN.getDisplayString();
    }
}
