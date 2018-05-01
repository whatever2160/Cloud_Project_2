package tweetObject;

import java.net.MalformedURLException;

import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;


public class RunMain {
		   public static void main(String[] args) throws MalformedURLException {  
				 System.out.println("Tese");
			   HttpClient httpClient = new StdHttpClient.Builder()  
						 .url("http://localhost:5984")
						 .username("admin")
						 .password("123456789")
						 .build();  
			   
				 CouchDbInstance dbInstance = new StdCouchDbInstance(httpClient); 
				CouchdbConnector cc = new CouchdbConnector();
				//cc.getView(dbInstance);
				cc.insertData(dbInstance);
				cc.readDate(dbInstance);
		   }
	}


