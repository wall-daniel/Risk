package risk.Enums;

public enum FileNames {
    COUNTRIES("countries\\"),
    LOCATIONS("location\\"),
    POLYGONS("countryPolygons\\"),
    CONTINENTS("continents\\");

    private String path;

    FileNames(String path){
        this.path = path;
    }

    public String getPath() {
        return path;
    }
}
