package main;

import com.snatik.polygon.Polygon;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;


public class RunMain {
	public static void main(String[] args) throws Exception {

        /*
         * Create the suburb polygons
         */
        URI MELGEOMRI = AFINNWords.class.getResource("/MelbGeo.geojson").toURI();
        Path path = Paths.get(MELGEOMRI);
        InputStream inputStream = new FileInputStream(path.toFile());
        List<Suburb> suburbs = GeoParser.createSuburb(inputStream);

        /*
         * Connect to the CouchDB
         */
		HttpClient httpClient = new StdHttpClient.Builder()
				.url("http://115.146.93.244:5984")
				.username("admin")
				.password("admin")
				.build();
		CouchDbConnector db = CouchDBController
                .creatConnection(httpClient, "twitter_data");

		/*
		 * Process the tweets
		 */
//        CouchDBController.insertData(db);
//        CouchDBController.readRawStream(db);
        CouchDBController.judgeSuburb(db,suburbs);
//        CouchDBController.readTweet(db);
//		  CouchDBController.addScore(db);

	}
}


