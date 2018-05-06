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
import mpi.MPI;


public class RunMain {

    public static int MASTER  = 0;
	public static void main(String[] args) throws Exception {

        /*
         * Create the suburb polygons
         */
        URI MELGEOURI = AFINNWords.class.getResource("/MelbGeo.geojson").toURI();
        Path path = Paths.get(MELGEOURI);
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
                .creatConnection(httpClient, "melbourne_melbourne");

		CouchDbConnector db2 = CouchDBController
                .creatConnection(httpClient, "processed_melbourne_tweets");
		/*
		 * Process the tweets
		 */
//        CouchDBController.insertData(db);
//        CouchDBController.readRawStream(db);
        CouchDBController.processTweets(db,suburbs, db2);
//        CouchDBController.readTweet(db);
//		  CouchDBController.addScore(db);

	}
}


