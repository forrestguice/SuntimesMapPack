/**
    Copyright (C) 2026 Forrest Guice
    This file is part of SuntimesWidget.

    SuntimesWidget is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SuntimesWidget is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SuntimesWidget.  If not, see <http://www.gnu.org/licenses/>.
*/

package com.forrestguice.suntimeswidget.map.backgrounds;

public class WorldMapBackgroundItem
{
    public WorldMapBackgroundItem() {}
    public WorldMapBackgroundItem(String providerUri, String id, String title, String summary, String mapProjectionLabel, String mapProjection, String fileUri, boolean tint)
    {
        this.provider_uri = providerUri;
        this.id = id;
        this.title = title;
        this.summary = summary;
        this.map_projection_label = mapProjectionLabel;
        this.map_projection = mapProjection;
        this.file_uri = fileUri;
        this.tint = tint;
    }

    protected String id = null;
    public String getID() {
        return id;
    }

    protected String title;
    public void setTitle(String value) {
        title = (value != null ? value : "");
    }
    public String getTitle() {
        return title;
    }

    protected String summary;
    public String getSummary() {
        return summary;
    }

    protected String map_projection;
    public String getMapProjection() {
        return map_projection;
    }

    protected String map_projection_label;
    public String getMapProjectionLabel() {
        return map_projection_label;
    }

    protected String provider_uri;
    public String getProviderUri() {
        return provider_uri;
    }

    protected String file_uri;
    public String getUri() {
        return file_uri;
    }

    protected boolean tint;
    public boolean shouldTint() {
        return tint;
    }
}

