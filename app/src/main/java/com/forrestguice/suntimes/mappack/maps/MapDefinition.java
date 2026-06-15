/*
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
