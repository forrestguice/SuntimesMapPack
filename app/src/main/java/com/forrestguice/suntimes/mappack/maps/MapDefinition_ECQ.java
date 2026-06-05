package com.forrestguice.suntimes.mappack.maps;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;

public abstract class MapDefinition_ECQ extends MapDefinition
{
    public MapDefinition_ECQ()
    {
        super();
        this.map_projection = WorldMapBackgroundContract.PROJECTION_EQC;
        this.map_projection_label = SuntimesMapAssets.MapProjections.EQUIRECTANGULAR.getDisplayString();
    }
}
