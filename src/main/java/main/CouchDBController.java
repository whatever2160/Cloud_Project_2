package main;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.Buffer;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import com.snatik.polygon.Point;
import com.sun.org.apache.xpath.internal.operations.Bool;
import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.http.HttpClient;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import com.gtranslate.*;

public class CouchDBController {

    static int LAST_POSITION = 0;

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
                System.out.println(suburb.getSA2_CODE11() + " " + suburb.getSA2_NAME11());
                tweet.setSa2_code11(suburb.getSA2_CODE11());
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

    public static void processTweets_ (
            CouchDbConnector db1, List<Suburb> suburbs, CouchDbConnector db2, int rank, int size) throws Exception {
        List<String> docIds = db1.getAllDocIds();
        int db1Size = docIds.size();
        List<String> docIds_ = docIds.subList(LAST_POSITION, db1Size);
        int count = 0;
        for (String id : docIds_) {
            if (count % size == rank) {
                try {
                    Tweet tweet = db1.get(Tweet.class, id);
                    System.out.println(tweet.getTweet_id());
                    setSuburb(tweet, suburbs);
                    setSentiment(tweet);
                    Tweet updatedTweet = new Tweet(tweet);
                    db2.create(updatedTweet);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            count++;
        }
        LAST_POSITION = db1Size;
    }

    public static void deDuplicate (CouchDbConnector db, int rank, int size) throws Exception {
        String charset = java.nio.charset.StandardCharsets.UTF_8.name();
        String url_dupList
                = "http://115.146.93.244:5984/processed_test/_design/dup/_list/mylist/count?group=true";
        String url_dupKey
                = "http://115.146.93.244:5984/processed_test/_design/dup/_view/count?reduce=false&";

        URLConnection connection = new URL(url_dupList).openConnection();
        connection.setRequestProperty("Accept-Charset", charset);
        InputStream response = connection.getInputStream();
        Scanner scanner = new Scanner(response);
        String responseBody = scanner.useDelimiter("\\A").next();
        responseBody = "{\"rows\": " + responseBody + "}";

        JSONParser parser = new JSONParser();
        JSONObject dupObject = (JSONObject) parser.parse(responseBody);
        List<JSONObject> rows = (List<JSONObject>) dupObject.get("rows");
        int count = 0;
        for (JSONObject row : rows) {
            if (count % size == rank) {
                String query_dupKey = "key=%22" + (String) row.get("key") + "%22";
//            int value = Integer.parseInt((String) row.get("value"));
                String query_key = String.format("%s", query_dupKey, charset);
                URLConnection connection_ = new URL(url_dupKey + query_key).openConnection();
                JSONObject dup_tweet = (JSONObject) parser.parse(new InputStreamReader(connection_.getInputStream()));
                List<JSONObject> tweet_rows = (List<JSONObject>) dup_tweet.get("rows");
                for (int i = 0; i < tweet_rows.size() - 1; i++) {
                    String id = (String) tweet_rows.get(i).get("id");
                    try {
                        Tweet tweet = db.get(Tweet.class, id);
                        db.delete(tweet);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            count++;
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
