package tweetObject;

import java.net.MalformedURLException;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;

public class RunMain {

		   public static void main(String[] args) throws MalformedURLException {  
			
			   HttpClient httpClient = new StdHttpClient.Builder()  
						 .url("http://localhost:5984")
						 .username("Tom")
						 .password("123456789")
						 .build();  
				 CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient); 
				 CouchdbConnector cc = new CouchdbConnector();
				 cc.creatDatabase(dbInstance);
				 cc.creatDocument(dbInstance);
				 
		   }
	}


