package com.forrestguice.suntimes.mappack;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.util.Log;

import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_FILE;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_ID;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_PROJECTION;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_PROJECTION_LABEL;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_SUMMARY;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.COLUMN_BACKGROUND_TITLE;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.QUERY_BACKGROUND_LIST;
import static com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundContract.QUERY_BACKGROUND_LIST_PROJECTION;

public class SuntimesMapProvider extends ContentProvider
{
    private static final int URIMATCH_BACKGROUND_LIST = 0;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        uriMatcher.addURI(getAuthority(), QUERY_BACKGROUND_LIST, URIMATCH_BACKGROUND_LIST);
    }

    public static String getAuthority() {
        return BuildConfig.AUTHORITY_ROOT + ".provider";
    }

    @Override
    public boolean onCreate()
    {
        initAssets(getContext());
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder)
    {
        Cursor cursor = null;
        int uriMatch = uriMatcher.match(uri);
        switch (uriMatch)
        {
            case URIMATCH_BACKGROUND_LIST:
                cursor = queryBackgroundList(projection);
                break;
        }
        return cursor;
    }

    public Cursor queryBackgroundList(@Nullable String[] projection)
    {
        String[] columns = (projection != null ? projection : QUERY_BACKGROUND_LIST_PROJECTION);
        MatrixCursor cursor = new MatrixCursor(columns);

        Context context = getContext();
        if (context != null)
        {
            for (WorldMapBackgroundItem item : getAllBackgroundItems())
            {
                Object[] row = new Object[columns.length];
                for (int i=0; i<columns.length; i++)
                {
                    switch (columns[i])
                    {
                        case COLUMN_BACKGROUND_ID:
                            row[i] = item.getID();
                            break;

                        case COLUMN_BACKGROUND_TITLE:
                            row[i] = item.getTitle();
                            break;

                        case COLUMN_BACKGROUND_SUMMARY:
                            row[i] = item.getSummary();
                            break;

                        case COLUMN_BACKGROUND_PROJECTION:
                            row[i] = item.getMapProjection();
                            break;

                        case COLUMN_BACKGROUND_PROJECTION_LABEL:
                            row[i] = item.getMapProjectionLabel();
                            break;

                        case COLUMN_BACKGROUND_FILE:
                            row[i] = item.getUri();
                            break;
                    }
                }
                cursor.addRow(row);
            }

        } else {
            Log.w(getClass().getSimpleName(), "queryBackgroundList: context is null!");
        }
        return cursor;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        return null;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    /**
     * initAssets; copies assets/maps/* into internalStorage/app/files
     * @param context Context
     */
    protected void initAssets(Context context)
    {
        AssetManager assets = context.getAssets();
        try {
            String[] srcFiles = assets.list("maps");
            if (srcFiles != null)
            {
                for (String srcFile : srcFiles)
                {
                    String dstPath = context.getFilesDir() + "/" + srcFile;
                    File dstFile = new File(dstPath);
                    if (!dstFile.exists())
                    {
                        try {
                            InputStream input = assets.open(srcFile);
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
                            Log.e("MapProvider", "init: failed to initialize asset! " + dstPath, e);
                        }
                    }// else {
                    //    Log.d("MapProvider", "init: " + dstFile.getName() + " already exists.");
                    //}
                }
            } else {
                Log.e("MapProvider", "init: required assets are missing!");
            }
        } catch (IOException e) {
            Log.e("MapProvider", "init: failed to initialize assets!", e);
        }
    }

    private void copyStream(InputStream in, OutputStream out) throws IOException
    {
        int r;
        byte[] b = new byte[1024];
        while ((r = in.read(b)) != -1) {
            out.write(b, 0, r);
        }
    }


    public static ArrayList<WorldMapBackgroundItem> ALL_BACKGROUNDS = new ArrayList<>();
    static {
        ALL_BACKGROUNDS.add(new WorldMapBackgroundItem(getAuthority(), "world_topo_bathy_aeqd_90_0", "Blue Marble Bathymetry (Polar North)", "Blue Marble Bathymetry (Polar North)", "Polar [north]", "aeqd_90,0", "TODO")); // TODO
        ALL_BACKGROUNDS.add(new WorldMapBackgroundItem(getAuthority(), "world_topo_bathy_aeqd_n90_0", "Blue Marble Bathymetry (Polar South)", "Blue Marble Bathymetry (Polar South)", "Polar [south]", "aeqd_-90,0", "TODO")); // TODO
        //public WorldMapBackgroundItem(String providerUri, int id, String title, String summary, String mapProjectionLabel, String mapProjection, String fileUri)
        // TODO
    }

    public List<WorldMapBackgroundItem> getAllBackgroundItems() {
        return ALL_BACKGROUNDS;
    }

}
