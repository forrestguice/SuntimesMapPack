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
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.forrestguice.suntimes.mappack.maps.MapManifest;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract;
import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

public class SuntimesMapAssets
{
    /**
     * initAssets; copies assets/maps/* into internalStorage/app/files and pre-grants uri permissions to Suntimes
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
                    File dstFile = new File(dstPath);
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
                    grantUriPermissions(context, BuildConfig.SUNTIMES_APPLICATION_ID, getUriForFile(context, dstFile));
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
        return sortItems(context, new ArrayList<>(ALL_BACKGROUNDS.values()));
    }
    public static List<WorldMapBackgroundItem> getBackgroundItems(Context context, String mapProjection)
    {
        if (ALL_BACKGROUNDS == null) {
            initBackgroundItems(context);
        }
        ArrayList<WorldMapBackgroundItem> items = new ArrayList<>();

        if (mapProjection.startsWith(WorldMapBackgroundContract.PROJECTION_AEQD_)
                && !mapProjection.equals(WorldMapBackgroundContract.PROJECTION_AEQD_NORTH)
                && !mapProjection.equals(WorldMapBackgroundContract.PROJECTION_AEQD_SOUTH))
        {
            for (WorldMapBackgroundItem item : ALL_BACKGROUNDS.values()) {
                if (item.getMapProjection().startsWith(WorldMapBackgroundContract.PROJECTION_AEQD_)) {
                    items.add(item);
                }
            }

        } else {
            for (WorldMapBackgroundItem item : ALL_BACKGROUNDS.values()) {
                if (item.getMapProjection().equals(mapProjection)) {
                    items.add(item);
                }
            }
        }

        sortItems(context, items);
        return items;
    }

    protected static List<WorldMapBackgroundItem> sortItems(Context context, ArrayList<WorldMapBackgroundItem> items)
    {
        final String defaultTitle = context.getString(R.string.default_item_title).toLowerCase(Locale.ROOT).trim();
        Collections.sort(items, new Comparator<WorldMapBackgroundItem>()
        {
            @Override
            public int compare(WorldMapBackgroundItem o, WorldMapBackgroundItem o1)
            {
                if (defaultTitle.equals(o.getTitle().toLowerCase(Locale.ROOT).trim())) {
                    return -1;
                } else if (defaultTitle.equals(o1.getTitle().toLowerCase(Locale.ROOT).trim())) {
                    return 1;
                } else {
                    return o.getTitle().compareTo(o1.getTitle());
                }
            }
        });
        return items;
    }

    protected static Map<String,WorldMapBackgroundItem> ALL_BACKGROUNDS = null;
    protected static void initBackgroundItems(Context context)
    {
        ALL_BACKGROUNDS = new HashMap<>();
        MapManifest.initBackgroundItems(context, ALL_BACKGROUNDS);
        initBackgroundItems_manifests(context, ALL_BACKGROUNDS);
        initBackgroundItems_arrays(context, ALL_BACKGROUNDS);
    }

    protected static void initBackgroundItems_manifests(Context context, Map<String,WorldMapBackgroundItem> map)
    {
        Resources res = context.getResources();
        TypedArray manifests = res.obtainTypedArray(R.array.map_manifests);
        Log.i("MapProvider", "found " + manifests.length() + " map manifests.");

        for (int i=0; i<manifests.length(); i++)
        {
            int manifestId = manifests.getResourceId(i, 0);
            if (manifestId != 0)
            {
                String manifestName = res.getResourceEntryName(manifestId);
                TypedArray manifest = res.obtainTypedArray(manifestId);
                Log.i("MapProvider", "found " + manifest.length() + " items in " + manifestName);
                for (int j=0; j<manifest.length(); j++)
                {
                    int itemId = manifest.getResourceId(j, 0);
                    if (itemId != 0)
                    {
                        String mapID = res.getResourceEntryName(itemId);
                        String[] mapItem = res.getStringArray(itemId);

                        WorldMapBackgroundItem item = new WorldMapBackgroundItem(null, mapID, mapItem);
                        if (item.isValid())
                        {
                            File file = new File(getFilesDir(context) + "/" + item.getUri());
                            if (file.exists())
                            {
                                item.setUri(getUriForFile(context, file).toString());
                                map.put(item.getID(), item);
                                Log.d("MapProvider", "initialized " + item.getID());

                            } else {
                                Log.w("MapProvider", "Item assets for " + mapID + " not found! " + item.getUri());
                            }
                        } else {
                            Log.e("MapProvider", "Item is invalid: " + mapID + "; ignoring...");
                        }
                    } else {
                        Log.e("MapProvider", "Manifest item at line " + j + " not found!");
                    }
                }
                manifest.recycle();

            } else {
                Log.e("MapProvider", "Manifest at line " + i + " not found!.");
            }
        }
        manifests.recycle();
    }

    protected static void initBackgroundItems_arrays(Context context, Map<String,WorldMapBackgroundItem> map)
    {
        Resources res = context.getResources();
        String[] ids = res.getStringArray(R.array.background_id);
        String[] files_day = res.getStringArray(R.array.background_day_file);
        String[] types = res.getStringArray(R.array.background_type);
        String[] tint = res.getStringArray(R.array.background_tint);
        String[] titles = res.getStringArray(R.array.background_title);
        String[] summary = res.getStringArray(R.array.background_summary);
        String[] projections = res.getStringArray(R.array.background_projection);
        String[] centers = res.getStringArray(R.array.background_center);

        for (int i=0; i<ids.length; i++)
        {
            if (map.containsKey(ids[i])) {
                Log.d("MapProvider", "background " + ids[i] + " is already defined; skipping...");
                continue;
            }

            File file = new File(getFilesDir(context) + "/" + files_day[i]);
            String uri = getUriForFile(context, file).toString();

            //MapProjections projection = MapProjections.find(projections[i]);
            map.put(ids[i], new WorldMapBackgroundItem(null, types[i], ids[i], titles[i], summary[i], projections[i], centers[i],
                    uri, tint[i]));
            Log.d("MapProvider", "initialized " + ids[i]);
        }
    }

    public static Uri getUriForFile(Context context, File file) {
        return FileProvider.getUriForFile(context, "suntimes.mappack.fileprovider", file);
    }
    protected static void grantUriPermissions(Context context, String packageName, Uri uri) {
        if (Build.VERSION.SDK_INT >= 19) {
            context.grantUriPermission(packageName, uri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
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
