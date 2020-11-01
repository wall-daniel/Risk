package risk.Enums;

public enum DrawingEnum {
   WAITING("Add continent or country"), CONTINENTS ("Draw the continents"), COUNTRIES("Draw the countries");

    private String text;

    DrawingEnum(String text){
        this.text = text;
    }

    public String getText(){
        return text;
    }
}
