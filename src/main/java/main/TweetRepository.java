package main;

import java.util.*;
import org.ektorp.*;
import org.ektorp.support.CouchDbRepositorySupport;


public class TweetRepository extends CouchDbRepositorySupport<Tweet> {


    public TweetRepository(CouchDbConnector db) {
        super(Tweet.class, db);
    }

//    public List<Tweet> findByCoordinate(String x, String y) {
//        return queryView();
//    }

}
