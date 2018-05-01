package tweetObject;

import org.ektorp.support.CouchDbDocument;
import org.ektorp.support.TypeDiscriminator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;


@TypeDiscriminator("doc.type === 'EligibleCouple'")

public class tweet extends CouchDbDocument {
       
       @JsonProperty("_id")
       private String id;
       @JsonProperty
       private String _rev;
       @JsonProperty
       private  String name;
       @JsonProperty
       private  String text;
     
     
       
      
       
    public String getText() {
		return text;
	}





	public void setText(String text) {
		this.text = text;
	}





	public tweet() {

     }





	public String getId() {
		return id;
	}





	public void setId(String id) {
		this.id = id;
	}





	public String get_rev() {
		return _rev;
	}





	public void set_rev(String _rev) {
		this._rev = _rev;
	}





	public String getName() {
		return name;
	}





	public void setName(String name) {
		this.name = name;
	}

   

   }