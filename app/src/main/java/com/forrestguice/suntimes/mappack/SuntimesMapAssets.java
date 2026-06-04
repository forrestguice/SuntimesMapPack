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

package com.forrestguice.suntimes.mappack;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.util.Log;

import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class SuntimesMapAssets
{
    /**
     * initAssets; copies assets/maps/* into internalStorage/app/files
     * @param context Context
     */
    public static void initAssets(@Nullable Context context)
    {
        if (context == null) {
            Log.e("MapProvider", "init: null context!");
            return;
        }
        AssetManager assets = context.getAssets();
        try {
            String[] srcFiles = assets.list("maps");
            if (srcFiles != null)
            {
                for (String srcFile : srcFiles)
                {
                    String srcPath = "maps/" + srcFile;
                    String dstPath = getFilesDir(context) + "/" + srcFile;
                    File dstFile = new File(srcPath);
                    if (!dstFile.exists())
                    {
                        try {
                            InputStream input = assets.open(srcPath);
                            OutputStream output = new FileOutputStream(dstPath);
                            try {
                                copyStream(input, output);
                                Log.i("MapProvider", "initialized asset: " + dstPath);

                            } finally {
                                output.flush();
                                output.close();
                                input.close();
                            }
                        } catch (IOException e) {
                            Log.e("MapProvider", "init: failed to initialize asset! " + srcPath + " (" + dstPath + ")", e);
                        }
                    } //else Log.d("MapProvider", "init: " + dstFile.getName() + " already exists.");
                }
            } else {
                Log.e("MapProvider", "init: required assets are missing!");
            }
        } catch (IOException e) {
            Log.e("MapProvider", "init: failed to initialize assets!", e);
        }
    }

    private static void copyStream(InputStream in, OutputStream out) throws IOException
    {
        int r;
        byte[] b = new byte[1024];
        while ((r = in.read(b)) != -1) {
            out.write(b, 0, r);
        }
    }

    public static String getFilesDir(Context context) {
        return context.getFilesDir().toString();
    }

    public static List<WorldMapBackgroundItem> getAllBackgroundItems(Context context)
    {
        if (ALL_BACKGROUNDS == null) {
            initBackgroundItems(context);
        }
        return ALL_BACKGROUNDS;
    }
    public static List<WorldMapBackgroundItem> getBackgroundItems(Context context, String mapProjection)
    {
        if (ALL_BACKGROUNDS == null) {
            initBackgroundItems(context);
        }
        List<WorldMapBackgroundItem> items = new ArrayList<>();
        for (WorldMapBackgroundItem item : ALL_BACKGROUNDS) {
            if (item.getMapProjection().equals(mapProjection)) {
                items.add(item);
            }
        }
        return items;
    }

    protected static ArrayList<WorldMapBackgroundItem> ALL_BACKGROUNDS = null;
    protected static void initBackgroundItems(Context context)
    {
        String[] ids = context.getResources().getStringArray(R.array.background_id);
        String[] files = context.getResources().getStringArray(R.array.background_file);
        String[] tint = context.getResources().getStringArray(R.array.background_tint);
        String[] titles = context.getResources().getStringArray(R.array.background_title);
        String[] summary = context.getResources().getStringArray(R.array.background_summary);
        String[] projections = context.getResources().getStringArray(R.array.background_projection);

        ALL_BACKGROUNDS = new ArrayList<>();
        for (int i=0; i<ids.length; i++)
        {
            File file = new File(getFilesDir(context) + "/" + files[i]);
            Uri uri = FileProvider.getUriForFile(context, "suntimes.mappack.fileprovider", file);
            MapProjections projection = MapProjections.find(projections[i]);
            ALL_BACKGROUNDS.add(new WorldMapBackgroundItem(null, ids[i], titles[i], summary[i],
                    (projection != null ? projection.getDisplayString() : "unknown"), projections[i],
                    uri.toString(), Boolean.parseBoolean(tint[i])));
        }
    }

    /**
     * Map Projections
     */
    public enum MapProjections
    {
        EQUIRECTANGULAR(WorldMapBackgroundContract.PROJECTION_EQC, "Equirectangular"),
        AZIMUTHAL_NORTH(WorldMapBackgroundContract.PROJECTION_AEQD_NORTH, "Azimuthal North"),
        AZIMUTHAL_SOUTH(WorldMapBackgroundContract.PROJECTION_AEQD_SOUTH ,"Azimuthal South"),
        AZIMUTHAL_EQUIDISTANT(WorldMapBackgroundContract.PROJECTION_AEQD_, "Azimuthal Equidistant"),
        MERCATOR(WorldMapBackgroundContract.PROJECTION_MERC, "Mercator"),
        SINUISOIDAL(WorldMapBackgroundContract.PROJECTION_SINU, "Mercator equal-area"),
        VAN_DER_GRINTEN(WorldMapBackgroundContract.PROJECTION_VANDG, "Van der Grinten");

        MapProjections(String projection, String displayString) {
            this.projection = projection;
            this.displayString = displayString;
        }

        private String projection;
        public String getProjection() {
            return projection;
        }

        private String displayString;
        public String getDisplayString() {
            return displayString;
        }

        public void setDisplayString(String value) {
            displayString = value;
        }

        public String toString() {
            return displayString;
        }

        @Nullable
        public static MapProjections find(String projection)
        {
            for (MapProjections p : MapProjections.values()) {
                if (p.getProjection().equals(projection)) {
                    return p;
                }
            }
            for (MapProjections p : MapProjections.values()) {
                if (p.getProjection().startsWith(projection)) {
                    return p;
                }
            }
            return null;
        }

        public static void initDisplayStrings(@Nullable Context context)
        {
            if (context != null)
            {
                EQUIRECTANGULAR.setDisplayString(context.getString(R.string.projection_equirectangular));
                AZIMUTHAL_NORTH.setDisplayString(context.getString(R.string.projection_azimuthal_north));
                AZIMUTHAL_SOUTH.setDisplayString(context.getString(R.string.projection_azimuthal_south));
                AZIMUTHAL_EQUIDISTANT.setDisplayString(context.getString(R.string.projection_azimuthal_equidistant));
                MERCATOR.setDisplayString(context.getString(R.string.projection_mercator));
                SINUISOIDAL.setDisplayString(context.getString(R.string.projection_sinuisoidal));
                VAN_DER_GRINTEN.setDisplayString(context.getString(R.string.projection_van_der_grinten));
            }
        }
    }

}
