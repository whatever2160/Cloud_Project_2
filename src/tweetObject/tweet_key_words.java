package tweetObject;

/**
 * @author tomyu
 *
 */
public class tweet_key_words {

	int tweet_id;
	

	String tweet_String;
	
	// user section
	int tweet_user_id;
	String location;
	
	//coornates
	double x, y;
	/* --------
	 * There  have a place also have the coordinate 
	 * with extra information like country  which one should we used
	 */
	
	
	public tweet_key_words(int tweet_id, String tweet_String, int tweet_user_id, String location, double x, double y) {
		super();
		this.tweet_id = tweet_id;
		this.tweet_String = tweet_String;
		this.tweet_user_id = tweet_user_id;
		this.location = location;
		this.x = x;
		this.y = y;
	}


	public int getTweet_id() {
		return tweet_id;
	}


	public void setTweet_id(int tweet_id) {
		this.tweet_id = tweet_id;
	}


	public String getTweet_String() {
		return tweet_String;
	}


	public void setTweet_String(String tweet_String) {
		this.tweet_String = tweet_String;
	}


	public int getTweet_user_id() {
		return tweet_user_id;
	}


	public void setTweet_user_id(int tweet_user_id) {
		this.tweet_user_id = tweet_user_id;
	}


	public String getLocation() {
		return location;
	}


	public void setLocation(String location) {
		this.location = location;
	}


	public double getX() {
		return x;
	}


	public void setX(double x) {
		this.x = x;
	}


	public double getY() {
		return y;
	}


	public void setY(double y) {
		this.y = y;
	}
	
	
	
	
	
}
