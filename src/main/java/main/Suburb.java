package main;
import com.snatik.polygon.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Suburb {

    private String MB_CODE11;
    private Polygon polygon;

    public Suburb(String MB_CODE11, Polygon polygon){
        this.MB_CODE11 = MB_CODE11;
        this.polygon = polygon;
    }

    public String getMB_CODE11() {
        return MB_CODE11;
    }

    public void setMB_CODE11(String MB_CODE11) {
        this.MB_CODE11 = MB_CODE11;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    public boolean isInPolygon(Point point) {
        //System.out.println(this.polygon.contains(point));
        return this.polygon.contains(point);

    }

}
