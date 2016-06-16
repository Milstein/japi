package com.milstein.japi;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.mongodb.morphia.Morphia;

import com.milstein.japi.services.v1.User;
import com.mongodb.MongoClient;

public class BusinessManager {

	private static final Logger log = Logger.getLogger(BusinessManager.class
			.getName());

	MongoClient mongoClient = null;
	Morphia morphia = null;
	String dbName = "japi";
	UserDAO userDAO = null;

	DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");

	private static BusinessManager INSTANCE = new BusinessManager();

	public static BusinessManager getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new BusinessManager();
		}
		return INSTANCE;
	}

	public BusinessManager() {
		mongoClient = DataManager.getMongo();
		morphia = new Morphia();
		morphia.map(User.class);
		userDAO = new UserDAO(mongoClient, morphia, dbName);
	}

	public User findUser(String userId) throws Exception {

		log.info("BusinessManager::findUser started");

		// User user = new User();
		// user.setId("112233");
		// user.setName("Milstein Munakami");
		// return user;

		User user = DataManager.getInstance().findUserById(userId);

		// User user = userDAO.findUserById(userId);

		if (user == null) {
			throw new Exception("Nothing found!");
		}
		return user;
	}

	public List<User> findUsers() {

		// List<User> users = new ArrayList<User>();
		//
		// User user1 = new User();
		// user1.setId(new ObjectId());
		// user1.setName("Milstein Munakami");
		// users.add(user1);
		//
		// User user2 = new User();
		// user2.setId(new ObjectId());
		// user2.setName("Nisha Shrestha");
		// users.add(user2);

		// return DataManager.getInstance().findAllUsers();

		return userDAO.getAllUsers();
	}

	public User addUser(User user) {
		// user.setId("112233");

		User newUser = userDAO.insertUser(user);

		return newUser;
	}

	public User updateUserAttribute(String userId, String attribute,
			String value) {
		// User user = new User();
		// user.setId(new ObjectId(userId));
		//
		// if (attribute.equals("name")) {
		// user.setName(name);
		// }
		// return user;

		// return DataManager.getInstance().updateUserAttribute(userId,
		// attribute,
		// value);

		return userDAO.updateUserAttribute(userId, attribute, value);

	}

	public void deleteUser(String userId) {
		// return;

		userDAO.deleteUserById(userId);
	}

}
