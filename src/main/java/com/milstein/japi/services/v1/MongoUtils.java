package com.milstein.japi.services.v1;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.bson.BSON;
import org.bson.BSONObject;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

import de.undercouch.bson4jackson.BsonFactory;
import de.undercouch.bson4jackson.BsonParser;

public class MongoUtils {

	private static ObjectMapper mapper;

	static {
		BsonFactory bsonFactory = new BsonFactory();
		bsonFactory.enable(BsonParser.Feature.HONOR_DOCUMENT_LENGTH);
		mapper = new ObjectMapper(bsonFactory);
	}

	public static DBObject getDbObject(Object o) {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			mapper.writeValue(baos, o);

			BSONObject decode = BSON.decode(baos.toByteArray());
			return new BasicDBObject(decode.toMap());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
