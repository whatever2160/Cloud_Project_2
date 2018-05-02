package main;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;


public class RunMain {
	public static void main(String[] args) throws Exception {

	 	HttpClient httpClient = new StdHttpClient.Builder()
				.url("http://localhost:5984")
				.username("admin")
				.password("123456789")
				.build();

		CouchDbConnector db = CouchDBController
                .creatConnection(httpClient, "my_first_database");
		CouchDBController.insertData(db);
		CouchDBController.addScore(db);



		/*
		 * test for inserting sentiment score data
		 */
		CouchDbConnector db2 = CouchDBController.creatConnection(httpClient, "temp");

	    }
	}


