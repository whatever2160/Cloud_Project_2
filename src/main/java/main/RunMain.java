package main;

import com.snatik.polygon.Polygon;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import mpi.MPI;


public class RunMain {

	public static void main(String[] args) throws Exception {
	    MPI.Init(args);
	    long startTime = System.currentTimeMillis();
	    int RANK = MPI.COMM_WORLD.Rank();
	    int SIZE = MPI.COMM_WORLD.Size();

        List<CouchDbConnector> dbs = connect();
        List<Suburb> suburbs = getSubburbs();

        //Process the tweets in multi process
        CouchDBController.processTweets_(dbs.get(0), suburbs, dbs.get(1), RANK, SIZE);

        //single process
        //CouchDBController.processTweets(dbs.get(0), suburbs, dbs.get(1));

        /*
         * Timer: print out the processing time
         */
        System.out.println("=====================================");
        long endTime = System.currentTimeMillis();
        float seconds = (endTime - startTime) / 1000F;
        System.out.println("Time: " + Float.toString(seconds) + " seconds.");

        MPI.Finalize();
    }


    /*
     * Connect to the CouchDB
     */
    public static List<CouchDbConnector> connect() throws Exception{
        HttpClient httpClient = new StdHttpClient.Builder()
                .url("http://115.146.93.244:5984")
                .username("admin")
                .password("admin")
                .build();
        CouchDbConnector db = CouchDBController
                .creatConnection(httpClient, "melbourne_melbourne");

        CouchDbConnector db2 = CouchDBController
                .creatConnection(httpClient, "processed_melbourne_tweets");

        List<CouchDbConnector> dbs = new ArrayList<>();
        dbs.add(db);
        dbs.add(db2);
        return dbs;
    }

    /*
     * Create the suburb polygons
     */
    public static List<Suburb> getSubburbs() throws Exception{
        URI MELGEOURI = AFINNWords.class.getResource("/Melb_SA2.geojson").toURI();
        Path path = Paths.get(MELGEOURI);
        InputStream inputStream = new FileInputStream(path.toFile());
        List<Suburb> suburbs = GeoParser.createSuburb(inputStream);
        return suburbs;
    }
}


