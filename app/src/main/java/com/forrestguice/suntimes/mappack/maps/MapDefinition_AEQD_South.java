package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_AEQD_South extends MapDefinition
{
    public MapDefinition_AEQD_South()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_AEQD_SOUTH;
        this.map_projection_label = SuntimesMapAssets.MapProjections.AZIMUTHAL_SOUTH.getDisplayString();
        this.map_projection_center = new double[] { -90, 0 };
    }
}
