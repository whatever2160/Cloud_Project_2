# -*- coding: utf-8 -*-
import couchdb
import sys
import twitter
from oauth_login import oauth_login
import getopt
def setup_db(hostname, port, db):
    # input the username of administrator
    admin_name = "admin"

    # input the password of admin
    password = "admin"

    # input the hostname and port
    # http://admin:admin@127.0.0.1:5984/
    # server = couchdb.Server('http://admin:admin@127.0.0.1:5984/')
    couchdb_url = "http://" + admin_name + ":" + password + "@" + hostname + ":" + str(port) + "/"
    server = couchdb.Server(couchdb_url)

    # input the name of database
    database = db
    # print(server[database])
    # if server[database] is None:

    if database in server:
        db = server[database]
    else:
        db = server.create(database)
    return db


def save_to_db(tweet, db):

    # put the lines for assining these values here
    content = tweet['text']
    language = tweet['lang']
    timestamp = tweet['timestamp_ms']
    coordinate_x = tweet['coordinates']['coordinates'][0]
    coordinate_y = tweet['coordinates']['coordinates'][1]
    tweet_id = tweet['id']
    # build a dictionary of the data
    data_dic = {"content": content, "language": language, "timestamp": timestamp, "coordinate_x": coordinate_x,
                "coordinate_y": coordinate_y, "tweet_id": tweet_id}

    # print the
    print(db.save(data_dic))

def main(argv):
    # Query terms

    print(sys.stderr, 'Filtering the public timeline for locations: melbourne')
    port = 5984
    hostname = '127.0.0.1'
    db = 'tweet'
    try:
        opts, args = getopt.getopt(argv, "p:h:d:")
    except getopt.GetoptError as error:
        print(error)
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-p':
            port = arg
        elif opt in "-h":
            hostname = arg
        elif opt in "-d":
            db = arg

    t = oauth_login()  # Returns an instance of twitter.Twitter
    twitter_stream = twitter.TwitterStream(auth=t.auth)  # Reference the self.auth parameter

    melbourne = "144.35,-38.26,145.30,-37.30"
    stream = twitter_stream.statuses.filter(locations=melbourne)

    # For illustrative purposes, when all else fails, search for Justin Bieber
    # and something is sure to turn up (at least, on Twitter)
    db = setup_db(hostname=hostname, port=port, db=db)
    for tweet in stream:
        if "text" in tweet.keys():
            if "coordinates" in tweet.keys() and tweet['coordinates'] is not None:
                if 144.35 < tweet['coordinates']['coordinates'][0] < 145.30 and -38.26 < tweet['coordinates']['coordinates'][1] < -37.30:
                    try:
                        print(tweet['text'])
                        save_to_db(tweet, db)
                    except:
                        pass


if __name__ == "__main__":
    main(sys.argv[1:])



