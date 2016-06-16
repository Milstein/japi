package com.milstein.japi;

import java.net.UnknownHostException;
import java.util.List;

import org.apache.log4j.Logger;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;
import org.mongodb.morphia.dao.BasicDAO;
import org.mongodb.morphia.query.Query;
import org.mongodb.morphia.query.UpdateOperations;

import com.milstein.japi.services.v1.User;
import com.mongodb.MongoClient;
import com.mongodb.MongoException;

public class UserDAO extends BasicDAO<User, String> {
	public static final String DB_NAME = "japi";
	public static final String COLLECTION_NAME = "users";
	private static final Logger logger = Logger.getLogger(DataManager.class
			.getName());

	private static Morphia morphia;
	private static MongoClient mongoClient = DataManager.getMongo();
	private static Datastore ds;

	@Override
	public Datastore getDatastore() {
		if (ds == null) {
			try {
				// Morphia
				ds = getMorphia().createDatastore(DataManager.getMongo(),
						DB_NAME);
			} catch (UnknownHostException | MongoException e) {
				e.printStackTrace();
			}
		}
		ds.ensureIndexes();
		return ds;
	}

	public static Morphia getMorphia() throws UnknownHostException,
			MongoException {
		if (morphia == null) {
			morphia = new Morphia().map(User.class); // .map(UserAccount.class);
														// .mapPackage("com.milstein.japi.services.v1")
		}
		return morphia;
	}

	public UserDAO(MongoClient mongoClient, Morphia morphia, String dbName) {
		super(mongoClient, morphia, dbName);
	}

	// Insert a User

	public User insertUser(User user) {
		try {
			// Get a document object
			// Document doc = new Document("name", "MongoDB")
			// .append("type", "database").append("count", 1)
			// .append("info", new Document("x", 203).append("y", 102));

			// Document doc = new Document();
			//
			// // Add in name
			// doc.put("name", user.getName());
			//
			// // Insert document into users collection
			// userCollection.insertOne(doc);

			// Using Morphia
			ds = getDatastore();
			ds.save(user);
		} catch (Exception e) {
			logger.error("DBManager:::insertUser Exception e=", e);
		}
		// return new object
		return user;
	}

	// Find a User by Id

	public User findUserById(String userId) {
		if (userId == null) {
			return null;
		}
		try {
			ObjectId id = new ObjectId(userId);

			ds = getDatastore();
			return ds.createQuery(User.class).field("_id").equal(id).get();

		} catch (Exception e) {
			logger.error("DBManager:::findUserById Exception e=", e);
		}

		return null;
	}

	public List<User> getAllUsers() {
		ds = getDatastore();
		return ds.createQuery(User.class).asList();
	}

	public User updateUserAttribute(String userId, String attribute,
			String value) {
		ObjectId id = new ObjectId(userId);

		ds = getDatastore();
		Query<User> updateQuery = ds.createQuery(User.class).field("_id")
				.equal(id);

		// change the name of the User
		UpdateOperations<User> ops = ds.createUpdateOperations(User.class).set(
				attribute, value);

		ds.update(updateQuery, ops);
		User user = ds.createQuery(User.class).field("_id").equal(id).get();

		// Update using Save
		// User user = ds.createQuery(User.class).field("_id").equal(id).get();
		// user.setName(value);
		// ds.save(user);

		return user;

	}

	public void deleteUserById(String userId) {
		ObjectId id = new ObjectId(userId);

		ds = getDatastore();
		User user = ds.createQuery(User.class).field("_id").equal(id).get();
		ds.delete(user);
	}
}
