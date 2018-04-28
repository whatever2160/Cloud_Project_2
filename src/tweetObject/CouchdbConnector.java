package tweetObject;

import java.util.List;
import com.fourspaces.couchdb.Database;
import com.fourspaces.couchdb.Document;
import com.fourspaces.couchdb.Session;
import com.fourspaces.couchdb.ViewResults;
 
public class CouchdbConnector {
 
	
	Session dbSession = new Session("127.0.0.1", 5984,"Tom","sky13312999");
	
	// creat database
	public void creatDatabase() {

		String dbname = "employee";
		dbSession.createDatabase(dbname);
	}
	
	public void retriveDatabase()
	{
		List <String> listofdb = dbSession.getDatabaseNames();
	}
	
	public void deleteDatabase()
	{

		String dbname = "employee";
		dbSession.deleteDatabase(dbname);
	}
	public void putintoDatabase()
	{
		String dbname = "employee";
		Database db = dbSession.getDatabase(dbname);
		         
		Document doc = new Document();
		         
		doc.setId("1");
		doc.put("EmpNO", "1");
		doc.put("Name", "Mike");
		doc.put("Group", "J2EECOE");
		doc.put("Designation", "Manager");
		doc.put("Language", "Java");
		         
		db.saveDocument(doc);
	
	}
	
	HttpClient httpclient = new DefaultHttpClient();
	 
	HttpGet get = new HttpGet("http://localhost:5984/employee/_all_docs?startkey=%221%22&limit=5");
	 
	HttpResponse response = httpclient.execute(get);

	
}