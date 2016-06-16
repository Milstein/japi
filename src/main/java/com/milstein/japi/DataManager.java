package com.milstein.japi;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;

import com.milstein.japi.services.v1.User;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;
import com.mongodb.ServerAddress;

public class DataManager {

	private static final Logger logger = Logger.getLogger(DataManager.class
			.getName());

	private static DB japiDB;
	private static DBCollection userCollection;

	private static MongoClient mongoClient;

	public static final String DB_NAME = "japi";
	private static final String host = "localhost";
	private static final int port = 27017;

	private static DataManager INSTANCE;

	public static DataManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new DataManager();
		}
		return INSTANCE;
	}

	public DataManager() {
		try {
			mongoClient = getMongo();
			japiDB = mongoClient.getDB(DB_NAME);
			userCollection = japiDB.getCollection("users");
		} catch (Exception e) {
			logger.error("db connection error e=", e);
		}
	}

	public static MongoClient getMongo() {
		if (mongoClient == null) {
			try {
				// To directly connect to a single MongoDB server
				// (this will not auto-discover the primary even if it's a
				// member of
				// a replica set)
				// MongoClient mongoClient = new MongoClient();

				// ***** or use Servers *****
				// or
				// MongoClient mongoClient = new MongoClient( "localhost" );

				// or
				// MongoClient mongoClient = new MongoClient( "localhost" ,
				// 27017 );

				// MongoClient mongoClient = new MongoClient(new ServerAddress(
				// "localhost", 27017));

				mongoClient = new MongoClient(new ServerAddress(host, port));

				// or, to connect to a replica set, with auto-discovery of the
				// primary, supply a seed list of members
				// MongoClient mongoClient = new MongoClient(
				// Arrays.asList(new ServerAddress("localhost", 27017),
				// new ServerAddress("localhost", 27018),
				// new ServerAddress("localhost", 27019)));

				// ***** or use Servers *****

				// ***** or use a connection string *****
				// "mongodb://host:27017,host2:27017/?replicaSet=rs0",

				// MongoClientURI connectionString = new MongoClientURI(
				// "mongodb://localhost:27017,localhost:27018,localhost:27019");

				// Using other options
				// MongoClientURI connectionString = new MongoClientURI(
				// "mongodb://localhost:27017", MongoClientOptions.builder()
				// .cursorFinalizerEnabled(false));

				// MongoClient mongoClient = new MongoClient(connectionString);

				// ***** or use a connection string *****

				logger.debug("New Mongo Connection with [" + host + "] and ["
						+ port + "]");

			} catch (MongoException e) {
				logger.error(e.getMessage());
			}
		}
		return mongoClient;
	}

	public User findUserById(String userId) {
		if (userId == null) {
			return null;
		}
		try {
			DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));
			DBObject userObj = userCollection.findOne(searchById);

			// userCollection.find(query, User.class);
			// System.out.println("Tested:" + userDoc.toJson());

			if (userObj != null) {
				// // serialize data
				// ObjectMapper mapper = new ObjectMapper(new BsonFactory());
				//
				// // deserialize data
				// return mapper.readValue(userDoc.toJson(), User.class);

				return mapUserFromDBObject(userObj);
			} else {
				return null;
			}

		} catch (Exception e) {
			logger.error("DBManager:::findUserById Exception e=", e);
		}

		return null;
	}

	// public static final DBObject toDBObject(Person person) {
	// return new BasicDBObject("_id", person.getId())
	// .append("name", person.getName())
	// .append("address",
	// new BasicDBObject("street", person.getAddress()
	// .getStreet()).append("city",
	// person.getAddress().getTown()).append("phone",
	// person.getAddress().getPhone()))
	// .append("books", person.getBookIds());
	// }

	private static User mapUserFromDBObject(DBObject dbObject) {
		User user = new User();

		user.setId((ObjectId) dbObject.get("_id"));
		user.setName((String) dbObject.get("name"));

		return user;
	}

	public List<User> findAllUsers() {
		List<User> users = new ArrayList<User>();

		try {
			DBCursor cursor = userCollection.find();

			if (cursor != null) {
				while (cursor.hasNext()) {
					BasicDBObject doc = (BasicDBObject) cursor.next();
					User item = mapUserFromDBObject(doc);
					users.add(item);
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
		}

		return users;
	}

	public User updateUserAttribute(String userId, String attribute, String value) {

		String updateValue = value;

		BasicDBObject doc = new BasicDBObject();

		doc.append("$set", new BasicDBObject().append(attribute, updateValue));

		DBObject searchById = new BasicDBObject("_id", new ObjectId(userId));

		userCollection.update(searchById, doc);

		return findUserById(userId);
	}
}
