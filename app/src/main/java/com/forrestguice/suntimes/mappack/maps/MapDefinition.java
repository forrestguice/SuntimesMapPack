package com.forrestguice.suntimes.mappack.maps;

import android.content.Context;
import android.util.Log;

import com.forrestguice.suntimes.mappack.SuntimesMapAssets;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

import java.io.File;

public abstract class MapDefinition extends WorldMapBackgroundItem
{
    public MapDefinition() {
        this.provider_uri = null;
        this.id = getClass().getSimpleName();
        this.map_projection_center = null;
    }

    private boolean initialized = false;
    public void initialize(Context context)
    {
        if (!initialized)
        {
            file_uri = SuntimesMapAssets.getUriForFile(context, new File(SuntimesMapAssets.getFilesDir(context) + "/" + file_uri)).toString();
            if (summary == null) {
                summary = title;
            }
            initialized = true;
            Log.d("MapProvider", "initialized: " + getID());
        }
    }
    public boolean isInitialized() {
        return initialized;
    }
}
