package main;
import com.snatik.polygon.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Suburb implements Serializable{

    private String SA2_CODE11;
    private String SA2_NAME11;
    private Polygon polygon;

    public Suburb(String SA2_CODE11, Polygon polygon){
        this.SA2_CODE11 = SA2_CODE11;
        this.polygon = polygon;
    }

    public Suburb(String SA2_CODE11, String SA2_NAME11, Polygon polygon) {
        this.SA2_CODE11 = SA2_CODE11;
        this.polygon = polygon;
        this.SA2_NAME11 = SA2_NAME11;
    }

    public String getSA2_CODE11() {
        return SA2_CODE11;
    }

    public void setSA2_CODE11(String SA2_CODE11) {
        this.SA2_CODE11 = SA2_CODE11;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public String getSA2_NAME11() {
        return SA2_NAME11;
    }

    public void setSA2_NAME11(String SA2_NAME11) {
        this.SA2_NAME11 = SA2_NAME11;
    }

    public boolean isInPolygon(Point point) {
        //System.out.println(this.polygon.contains(point));
        return this.polygon.contains(point);

    }

}
