package main;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.impl.StdCouchDbInstance;
import org.ektorp.http.HttpClient;

public class CouchDBController {

    // the database will be created if it doesn't exists
    public static CouchDbConnector creatConnection (HttpClient httpClient, String path) {
        CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient);
        CouchDbConnector db = dbInstance
                .createConnector(path, true);
        return db;
    }

	public static void insertData (CouchDbConnector db)
	{
		Scanner s = new Scanner(System.in);
		String input;
		String tweets;
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

			System.out.println("Please input the X");
			String x = s.nextLine();
			bysessionidView.put("x", x);
			System.out.println("Please input the Y");
			String y = s.nextLine();
			bysessionidView.put("y", y);

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


	public static void readDate(CouchDbConnector db) throws Exception
	{
		//StdCouchDbConnector db = new StdCouchDbConnector("my_first_database", dbInstance);
		List<String> ls = db.getAllDocIds();
		for(String id : ls)
		{
			Tweet doc = db.get(Tweet.class, id);
			System.out.println("Name: " + doc.getUser_name() + "\nText: " + doc.getText());
			SentimentAnalyser.scoreText(doc.getText());
		}

		//java.io.InputStream olderRev = db.getAsStream(id);
	}


	public static void addScore(CouchDbConnector db) throws Exception {
		List<String> docIds = db.getAllDocIds();
		for (String id : docIds) {
			Tweet tweet = db.get(Tweet.class, id);
			tweet.setScore(Float.toString(SentimentAnalyser.scoreText(tweet.getText())));
			db.update(tweet);
		}
	}
}
