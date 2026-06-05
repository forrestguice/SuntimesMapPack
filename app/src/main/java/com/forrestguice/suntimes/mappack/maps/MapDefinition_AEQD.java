package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_AEQD extends MapDefinition
{
    public MapDefinition_AEQD()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_AEQD_;
        this.map_projection_label = SuntimesMapAssets.MapProjections.AZIMUTHAL_EQUIDISTANT.getDisplayString();
    }
}
