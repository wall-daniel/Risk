package risk.MapCreator;

public enum CountryDrawingEnum {
    FIRST_POINT ("Click the first point."),
    NEXT_POINT("Click the next point to create the border."),
    FINISHED("You are finished drawing."),
    FILL("Select a point to fill the shape");


    String description;

    CountryDrawingEnum(String description){
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
