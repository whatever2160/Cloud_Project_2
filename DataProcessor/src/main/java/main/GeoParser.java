package main;


import com.snatik.polygon.Point;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.awt.*;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.function.DoubleBinaryOperator;

import com.snatik.polygon.Polygon;

public class GeoParser {

    public static List<Suburb> createSuburb(InputStream inputStream) throws Exception {
        List<JSONObject> features = getfeatures(inputStream);
        List<Suburb> suburbs = new ArrayList<>();
        for (JSONObject feature : features) {
            String SA2_CODE11 = getSuburbCode(feature);
            List<Polygon> polygons = getMultiPolygon(feature);
            for(Polygon polygon : polygons) {
                String SA2_NAME11 = getSuburbName(feature);
                Suburb suburb = new Suburb(SA2_CODE11, SA2_NAME11, polygon);
                suburbs.add(suburb);
            }
        }
        return suburbs;
    }

    public static List<JSONObject> getfeatures(InputStream inputStream) throws Exception {
        JSONParser parser = new JSONParser();
        JSONObject melbGeo = (JSONObject) parser.parse(new InputStreamReader(inputStream));
        List<JSONObject> features = (List<JSONObject>) melbGeo.get("features");
        return features;
    }

    public static String getSuburbCode(JSONObject feature) {
        JSONObject properties = (JSONObject) feature.get("properties");
        return (String) properties.get("SA2_MAINCODE_2011");
    }

    public static String getSuburbName(JSONObject feature) {
        JSONObject properties  = (JSONObject) feature.get("properties");
        return (String) properties.get("SA2_NAME_2011");
    }

    public static List<List<List<List<Double>>>> getCoordinates(JSONObject feature) {
        JSONObject geometry = (JSONObject) feature.get("geometry");
        List<List<List<List<Double>>>> coordinates = new ArrayList<>();
        if (getGeoType(feature).equals("Polygon")) {
            List<List<List<Double>>> geoObject = (List<List<List<Double>>>) geometry.get("coordinates");
            coordinates.add(geoObject);
        } else {
            coordinates = (List<List<List<List<Double>>>>) geometry.get("coordinates");
        }
        return coordinates;
    }

    public static String getGeoType(JSONObject feature) {
        JSONObject geometry = (JSONObject) feature.get("geometry");
        return (String)geometry.get("type");
    }

    public static Polygon getPolygon (List<List<Double>> coordinates) {
        Double X_0 = coordinates.get(0).get(0);
        Double Y_0 = coordinates.get(0).get(1);
        Double X_1 = coordinates.get(1).get(0);
        Double Y_1 = coordinates.get(1).get(1);
        Double X_2 = coordinates.get(2).get(0);
        Double Y_2 = coordinates.get(2).get(1);
        Polygon.Builder polygonBulider = Polygon.Builder()
                .addVertex(new Point(X_0, Y_0))
                .addVertex(new Point(X_1, Y_1))
                .addVertex(new Point(X_2, Y_2));

        for (int i = 3; i < coordinates.size(); i++) {
            Double coordinates_X = coordinates.get(i).get(0);
            Double coordinates_Y = coordinates.get(i).get(1);
            polygonBulider.addVertex(new Point(coordinates_X, coordinates_Y));
        }
        Polygon polygon = polygonBulider.build();
        return polygon;
    }

    public static List<Polygon> getMultiPolygon (JSONObject feature) {
        List<Polygon> polygons = new ArrayList<>();
        List<List<List<List<Double>>>> coordinates = getCoordinates(feature);
        for (List<List<List<Double>>> ls : coordinates) {
            polygons.add(getPolygon(ls.get(0)));
        }
        return polygons;
    }

}
