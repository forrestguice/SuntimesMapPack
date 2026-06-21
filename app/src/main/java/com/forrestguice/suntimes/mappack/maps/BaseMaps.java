package com.forrestguice.suntimes.mappack.maps;

public class BaseMaps
{
    public static class BaseMap_ECQ extends MapDefinition_ECQ
    {
        public BaseMap_ECQ() {
            super();
            this.file_uri = "basemap_eqc.png";
            this.title = "Default";
            this.tint = true;
        }
    }

    public static class BaseMap_AEQD_North extends MapDefinition_AEQD_North
    {
        public BaseMap_AEQD_North() {
            super();
            this.file_uri = "basemap_aeqd_north.png";
            this.title = "Default (North)";
            this.tint = true;
        }
    }

    public static class BaseMap_AEQD_South extends MapDefinition_AEQD_South
    {
        public BaseMap_AEQD_South() {
            super();
            this.file_uri = "basemap_aeqd_south.png";
            this.title = "Default (South)";
            this.tint = true;
        }
    }

    public static class BaseMap_AEQD_Cairo extends MapDefinition_AEQD
    {
        public BaseMap_AEQD_Cairo() {
            super();
            this.file_uri = "basemap_aeqd_cairo_30,31.png";
            this.map_projection_center = new double[] { 30, 31 };
            this.title = "Default (Cairo)";
            this.tint = true;
        }
    }

    public static class BaseMap_AEQD_Phoenix extends MapDefinition_AEQD
    {
        public BaseMap_AEQD_Phoenix() {
            super();
            this.file_uri = "basemap_aeqd_33,-111.png";
            this.map_projection_center = new double[] { 33, -111 };
            this.title = "Default (North America)";
            this.tint = true;
        }
    }

    public static class BaseMap_MERC extends MapDefinition_MERC
    {
        public BaseMap_MERC() {
            super();
            this.file_uri = "basemap_merc.png";
            this.title = "Default";
            this.tint = true;
        }
    }

    public static class BaseMap_VANDG extends MapDefinition_VANDG
    {
        public BaseMap_VANDG() {
            super();
            this.file_uri = "basemap_vandg.png";
            this.title = "Default";
            this.tint = true;
        }
    }

    public static class BaseMap_SINU extends MapDefinition_SINU
    {
        public BaseMap_SINU() {
            super();
            this.file_uri = "basemap_sinu.png";
            this.title = "Default";
            this.tint = true;
        }
    }
}
