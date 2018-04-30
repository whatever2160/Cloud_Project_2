package tweetObject;

import java.util.List;

import java.net.MalformedURLException;
import org.ektorp.CouchDbConnector;  
import org.ektorp.CouchDbInstance;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;  
import org.ektorp.http.StdHttpClient;  
import org.ektorp.impl.StdCouchDbConnector;  
import org.ektorp.impl.StdCouchDbInstance;  
import org.ektorp.support.DesignDocument;

public class CouchdbConnector {


	public void creatDatabase(CouchDbInstance dbInstance)
	{
		 //--------------- Creating database----------------------------//  
		
		 CouchDbConnector db = new StdCouchDbConnector("abc", dbInstance);  
		 
		 db.createDatabaseIfNotExists();  
		
	}
		
	
	public void creatDocument (CouchDbInstance dbInstance)
	{
		 //--------------- Creating Document----------------------------//  
		CouchDbConnector db = dbInstance.createConnector("my_first_database", true); // will create a database if the database is now exit
		// create a document inside the database
		DesignDocument dd = new DesignDocument("light");  
		db.create(dd);  
		
		
	} 
	
	public void getView(CouchDbInstance dbInstance)
	{
		CouchDbConnector db = dbInstance.createConnector("my_first_database", true); 
		 ViewQuery query = new ViewQuery()
			        .designDocId("_design/light")
			        .viewName("example")
			        .key("name");

			//List<Sofa> redSofas = db.queryView(query, Sofa.class);
	}

	
				 
}	
