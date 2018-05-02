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
       private  String user_name;
       @JsonProperty
       private  String user_id;
       @JsonProperty
       private  String text;
       @JsonProperty
       private  String tweet_create_time;
       @JsonProperty
       private  String x;
       @JsonProperty
       private  String y;


       public tweet() 
       {

       }

       //getter and setter
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


	public String getUser_name() {
		return user_name;
	}


	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}


	public String getUser_id() {
		return user_id;
	}


	public void setUser_id(String user_id) {
		this.user_id = user_id;
	}


	public String getText() {
		return text;
	}


	public void setText(String text) {
		this.text = text;
	}


	public String getTweet_create_time() {
		return tweet_create_time;
	}


	public void setTweet_create_time(String tweet_create_time) {
		this.tweet_create_time = tweet_create_time;
	}


	public String getX() {
		return x;
	}


	public void setX(String x) {
		this.x = x;
	}


	public String getY() {
		return y;
	}


	public void setY(String y) {
		this.y = y;
	}

     
       
       
       
       
	   }