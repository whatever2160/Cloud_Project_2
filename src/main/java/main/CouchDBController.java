package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.snatik.polygon.Point;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.http.HttpClient;

import com.gtranslate.*;

public class CouchDBController {

    // the database will be created if it doesn't exists
    public static CouchDbConnector creatConnection (HttpClient httpClient, String path) {
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector db = dbInstance
                .createConnector(path, true);
        return db;
    }


	public static void readRawStream (CouchDbConnector db) throws Exception {
        List<String> docIds = db.getAllDocIds();
        for (String id : docIds) {
            InputStream doc = db.getAsStream(id);
            BufferedReader br = new BufferedReader(new InputStreamReader(doc));
            System.out.println(br.readLine());
        }
    }

	/*
	 * Fetch all tweets in one call
	 */
	public static List<Tweet> loadBulkTweets (CouchDbConnector db) throws Exception {
        List<String> docIds = db.getAllDocIds();
        ViewQuery q = new ViewQuery()
                .allDocs()
                .includeDocs(true)
                .keys(docIds);
        List<Tweet> bulkLoaded = db.queryView(q, Tweet.class);
        return bulkLoaded;
    }


	/*
	 * method for translation / 403
	 */
	public static String translate(String text, String language) {
        Translator translator = Translator.getInstance();
        return translator.translate(text, Language.ENGLISH, language);
    }


	public static void setSentiment(Tweet tweet) throws Exception {
        String content = tweet.getContent();
        /*
         *  Google Sentiment API
         *  tweet.setScore(Float.toString(SentimentAnalyser.scoreText(tweet.getText())));
         */

        /*
         * Sentiment base on AFINN
         */
        tweet.setSentiment(Integer.toString(TextSentimentAnalyzer.analyze(content).getScore()));

	}

	public static void setSuburb(Tweet tweet, List<Suburb> suburbs) throws Exception {
        Double coordinate_X = Double.parseDouble(tweet.getCoordinate_x());
        Double coordinate_Y = Double.parseDouble(tweet.getCoordinate_y());
        Point point = new Point(coordinate_X, coordinate_Y);
        for (Suburb suburb : suburbs) {
            if (suburb.isInPolygon(point)) {
                System.out.println("~~~~~");
                System.out.println(suburb.getMB_CODE11() + " " + suburb.getSA2_NAME11());
                tweet.setMb_code11(suburb.getMB_CODE11());
                tweet.setSa2_name11(suburb.getSA2_NAME11());
            }
        }
    }

    /*
     * set sentiment score and find suburb location for each tweet.
     */
    public static void processTweets(
            CouchDbConnector db1, List<Suburb> suburbs, CouchDbConnector db2) throws Exception {
        List<String> docIds = db1.getAllDocIds();
        for (String id : docIds) {
            Tweet tweet = db1.get(Tweet.class, id);
            setSuburb(tweet, suburbs);
            setSentiment(tweet);
            Tweet updatedTweet = new Tweet(tweet);
            db2.create(updatedTweet);
        }
    }


    public static void insertData (CouchDbConnector db)
    {
        Scanner s = new Scanner(System.in);
        String input;
        String tweets;
        String lang;
        Boolean run = true;
        String q;
        //--------------- Creating Document----------------------------//
        //CouchDbConnector db = dbInstance.createConnector("my_first_database", true); // will create a database if the database is now exit
        // create a document inside the database
        //DesignDocument dd = new DesignDocument("light");
        //db.create(dd);

//		StdCouchDbConnector db = new StdCouchDbConnector("my_first_database", dbInstance);
//		db.createDatabaseIfNotExists();

        int size = db.getAllDocIds().size();

        do
        {
            HashMap<String, String> bysessionidView = new HashMap<String, String>();
            //using hasmap to insert the key and value to the database.
            // all those system input should change to read value from the previous
            System.out.println("Record: " + (size+1) + "\nPlease enter customer name");
            String id = Integer.toString(size + 1);
            bysessionidView.put("_id", id);
            // the name of the user
            input = s.nextLine();
            bysessionidView.put("user_name", input);
            // the Tweet of the user's input
            System.out.println("Please enter the Text");
            tweets = s.nextLine();
            bysessionidView.put("text", tweets);
            String tweet_creat_time = "2018-6-13";
            bysessionidView.put("tweet_create_time", tweet_creat_time);
            System.out.println("please enter the language");
            lang = s.nextLine();
            bysessionidView.put("lang", lang);
            System.out.println("Please input the X");
            String x = s.nextLine();
            bysessionidView.put("coordiante_x", x);
            System.out.println("Please input the Y");
            String y = s.nextLine();
            bysessionidView.put("coordinate_y", y);

            // create the database with information listed above
            db.create(bysessionidView);

            System.out.println("The information have been insert: id -"+ (size + 1) +"\nName: " + input);
            size++;
            System.out.println("Press any to keep recording, and press 0 to exit" );
            q = s.nextLine();
            if(q.equals("0"))
            {
                run = false;
            }
        }
        while(run);
    }
}
