package tweetObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.net.MalformedURLException;
import org.ektorp.CouchDbConnector;  
import org.ektorp.CouchDbInstance;
import org.ektorp.DocumentNotFoundException;
import org.ektorp.ViewQuery;
import org.ektorp.http.HttpClient;  
import org.ektorp.http.StdHttpClient;  
import org.ektorp.impl.StdCouchDbConnector;  
import org.ektorp.impl.StdCouchDbInstance;  
import org.ektorp.support.DesignDocument;
import org.ektorp.util.Base64.InputStream;

public class CouchdbConnector {


	public void creatDatabase(CouchDbInstance dbInstance)
	{
		 //--------------- Creating database----------------------------//  
		
		 CouchDbConnector db = new StdCouchDbConnector("abc", dbInstance);  
		 
		 db.createDatabaseIfNotExists();  
		
	}
		
	
	public void insertData (CouchDbInstance dbInstance)
	{
		Scanner s = new Scanner(System.in);
		String input;
		Boolean run = true;
		String q;
		 //--------------- Creating Document----------------------------//  
		//CouchDbConnector db = dbInstance.createConnector("my_first_database", true); // will create a database if the database is now exit
		// create a document inside the database
		//DesignDocument dd = new DesignDocument("light");  
		//db.create(dd);  
		StdCouchDbConnector db = new StdCouchDbConnector("my_first_database", dbInstance);
		db.createDatabaseIfNotExists();
		int a = 11;
		String getInfor;
			do
			{
				HashMap<String, String> bysessionidView = new HashMap<String, String>();  
				System.out.println("Record: " +a + "\nPlease enter customer name");
				bysessionidView.put("_id", ""+a);
				
				input = s.next();
				bysessionidView.put("name", input);	
				
				db.create(bysessionidView);
				//db.get(Sofa.class, a);
				System.out.println("The information have been insert: id -"+a +"\nName: " + input);
				a++;
				System.out.println("Press any to keep recording, and press 0 to exit" );
				q = s.next();
				if(q.equals("0"))
				{
					run = false;
				}
			}
			while(run);
	}
 
		public void readDate(CouchDbInstance dbInstance)
		{
			
			
			
			
				String id = ""+11;
				StdCouchDbConnector db = new StdCouchDbConnector("my_first_database", dbInstance);
				
				
					List ls = db.getAllDocIds();
					for(int i = 0; i< ls.size();i++)
					{
						id = (String) ls.get(i);
						Sofa doc = db.get(Sofa.class, id);
						System.out.println(doc.getName());
					}
					
					//java.io.InputStream olderRev = db.getAsStream(id);
		
		} 
	
	
	
	
	
				 
}	
