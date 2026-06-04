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

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import com.forrestguice.suntimeswidget.map.backgrounds.WorldMapBackgroundItem;

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
    private static final int URIMATCH_BACKGROUND_LIST_BY_PROJ = 10;

    private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static
    {
        uriMatcher.addURI(getAuthority(), QUERY_BACKGROUND_LIST, URIMATCH_BACKGROUND_LIST);
        uriMatcher.addURI(getAuthority(), QUERY_BACKGROUND_LIST + "/*", URIMATCH_BACKGROUND_LIST_BY_PROJ);
    }

    public static String getAuthority() {
        return BuildConfig.AUTHORITY_ROOT + ".provider";
    }

    @Override
    public boolean onCreate()
    {
        SuntimesMapAssets.initAssets(getContext());
        SuntimesMapAssets.MapProjections.initDisplayStrings(getContext());
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
                Log.d("DEBUG", "URIMATCH_BACKGROUND_LIST: " + uri);
                cursor = queryBackgroundList(projection, null);
                break;

            case URIMATCH_BACKGROUND_LIST_BY_PROJ:
                Log.d("DEBUG", "URIMATCH_BACKGROUND_LIST_BY_PROJ: " + uri + " :: " + uri.getLastPathSegment());
                cursor = queryBackgroundList(projection, uri.getLastPathSegment());
                break;
        }
        return cursor;
    }

    public Cursor queryBackgroundList(@Nullable String[] projection, @Nullable String mapProjection)
    {
        String[] columns = (projection != null ? projection : QUERY_BACKGROUND_LIST_PROJECTION);
        MatrixCursor cursor = new MatrixCursor(columns);

        Context context = getContext();
        if (context != null)
        {
            List<WorldMapBackgroundItem> items = (mapProjection != null
                    ? SuntimesMapAssets.getBackgroundItems(context, mapProjection)
                    : SuntimesMapAssets.getAllBackgroundItems(context));
            for (WorldMapBackgroundItem item : items)
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
                            if (Build.VERSION.SDK_INT >= 19) {
                                context.grantUriPermission(getCallingPackage(), Uri.parse(item.getUri()), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                            }
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
}
