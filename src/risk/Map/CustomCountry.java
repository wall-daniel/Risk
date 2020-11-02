package risk.Map;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import risk.Enums.MapColor;
import risk.Model.Country;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CustomCountry extends Country {
    private Polygon countryPolygon;

    public CustomCountry(Polygon polygon, String name) {
        super(name);

        this.countryPolygon = polygon;
    }

    public CustomCountry(JsonObject json) {
        super(json);

        // Load the polygon from the json
        JsonArray pointArray = json.getAsJsonArray("points");
        int[] xPoints = new int[pointArray.size()];
        int[] yPoints = new int[pointArray.size()];

        for (int i = 0; i < xPoints.length; i++) {
            JsonObject point = pointArray.get(i).getAsJsonObject();
            xPoints[i] = point.get("x").getAsInt();
            yPoints[i] = point.get("y").getAsInt();
        }

        this.countryPolygon = new Polygon(xPoints, yPoints, xPoints.length);
    }

    public boolean isPointInPolygon(int x, int y){
        return countryPolygon.contains(x, y);
    }


    /**
     * Maybe change the border color if a new player conquers the country.
     */
    public void setPolygonBorder(MapColor borderColor){

    }

    @Override
    public JsonObject toJson() {
        JsonObject obj = super.toJson();

        // Add the points of the polygon
        JsonArray pointArray = new JsonArray(countryPolygon.npoints);
        for (int i = 0; i < countryPolygon.npoints; i++) {
            JsonObject point = new JsonObject();

            point.addProperty("x", countryPolygon.xpoints[i]);
            point.addProperty("y", countryPolygon.ypoints[i]);

            pointArray.add(point);
        }
        obj.add("points", pointArray);

        return obj;
    }
}
