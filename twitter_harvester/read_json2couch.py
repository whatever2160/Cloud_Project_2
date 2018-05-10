from mpi4py import MPI
import sys
import json
sys.path.append("/couchdb")
import couchdb
import datetime
import getopt

def setup_db(hostname="127.0.0.1", port=5984, db="db"):
    # input the username of administrator
    admin_name = "admin"

    # input the password of admin
    password = "admin"

    # input the hostname and port
    hostname = hostname
    port = port

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
    coordinate_x = tweet['coordinates']['coordinates'][0]
    coordinate_y = tweet['coordinates']['coordinates'][1]
    tweet_id = tweet['id_str']
    # build a dictionary of the data
    data_dic = {"content": content, "language": language, "coordinate_x": coordinate_x,
                "coordinate_y": coordinate_y, "tweet_id": tweet_id}

    # print the
    print(data_dic)
    print(db.save(data_dic))


def process_tweets(rank, processes, file):
    try:
        with open(file, encoding='utf8') as f:
            tweets = []
            for counter, value in enumerate(f):
                if counter % processes == rank:
                    if '"coordinates":{' in value:
                        if counter > 0 and value not in ']}\n':
                            if value[-3:] in ']}\n':
                                tweet = json.loads(value[: -3])
                            elif value[-2] in '}':
                                tweet = json.loads(value[: -1])
                            else:
                                tweet = json.loads(value[: -2])
                            # print(tweet['doc']['coordinates']['coordinates'])
                            try:
                                if 'coordinates' in tweet['doc']:
                                    if tweet['doc']['coordinates'] is not None:
                                        coordinate = tweet['doc']['coordinates']['coordinates']
                                        if coordinate[0] is not None and coordinate[1] is not None:
                                            tweets.append(tweet)
                            except:
                                pass
    except json.decoder.JSONDecodeError:
        print(value)
        pass
    return tweets


def main(argv):
    print("Start at ", datetime.datetime.now())
    # Get the rank, and run either master or slave function

    hostname = "127.0.0.1"
    db = "melbourne_melbourne"
    try:
        opts, args = getopt.getopt(argv, "f:h:d:")
    except getopt.GetoptError as error:
        print(error)
        sys.exit(2)
    for opt, arg in opts:
        if opt == '-f':
            file = arg
        elif opt in "-h":
            hostname = arg
        elif opt in "-d":
            db = arg

    comm = MPI.COMM_WORLD
    rank = comm.Get_rank()
    size = comm.Get_size()
    data = process_tweets(rank, size, file)
    to_db = setup_db(hostname=hostname, db=db)
    for row in data:
        try:
            doc = row['doc']
            if 146 > doc['coordinates']['coordinates'][0] > 144 and -37 > doc['coordinates']['coordinates'][1] > -39:
                save_to_db(doc, to_db)
        except:
            pass
    print("end at ", datetime.datetime.now())


if __name__ == "__main__":
    main(sys.argv[1:])
