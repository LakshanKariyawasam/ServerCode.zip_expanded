package com.perisic.mongoclient;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import static com.mongodb.client.model.Filters.eq;

public class SurveyAccessService {
	
	private static final String collectionName = "access_pane";

    public String getUserAccessHash(final String userid) {
        try {
            final MongoConnector dbo = new MongoConnector();
            final MongoDatabase database = dbo.getConnection();
            final Document usrDoc = database.getCollection(collectionName).find(eq("userid", userid)).first();
            return usrDoc.get("password").toString();
        } catch (final Exception ex) {
            System.out.println(ex);
            return null;
        } finally {

        }
    }

    public String getUserIdByUserName(final String usrname) {
        try {
            final MongoConnector dbo = new MongoConnector();
            final MongoDatabase database = dbo.getConnection();
            final Document usrDoc = database.getCollection(collectionName).find(eq("username", usrname)).first();
            return usrDoc.get("userid").toString();
        } catch (final Exception ex) {
            System.out.println(ex);
            return null;
        } finally {

        }
    }

    public Integer createPaneUser(String currntUserName, String usrname, String pswd,
            final boolean isSuper) {
        try {
            final byte[] salt = new byte[0];
            final MongoConnector dbo = new MongoConnector();
            final MongoDatabase database = dbo.getConnection();
            final MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            final FindIterable<org.bson.Document> sprusr = collection.find(eq("superuser", true));
            final List<Document> newUser = new ArrayList<Document>();
            final String userid = Long.toString(collection.countDocuments() + 1);

            for (final org.bson.Document spruser : sprusr.collation(null)) {
                if (spruser.get("username").equals(currntUserName)) {
                    if (!usrname.isEmpty() && !pswd.isEmpty()) {
                        final Document isAvilable = collection.find(eq("username", usrname)).first();
                        if (isAvilable == null) {
                            final SurveyLogingService secure = new SurveyLogingService();
                            final String hashcode = secure.encryptLogings(pswd, userid, salt, true);
                            if (hashcode != null) {
                                newUser.add(new Document("userid", userid).append("username", usrname)
                                        .append("password", hashcode).append("superuser", isSuper));
                                collection.insertMany(newUser);
                                return 1;
                            } else {
                                throw new Exception("Failed hashed");
                            }
                        } else {
                            throw new Exception("User name alredy exist");
                        }
                    }
                }
            }
        } catch (final Exception ex) {
            System.out.println(ex);
            return 0;

        } finally {

        }
        return 0;
    }

    public ArrayList<Document> getBasicUsers() {
        ArrayList<Document> userDoc = null;
        try {
            MongoConnector dbo = new MongoConnector();
            MongoDatabase database = dbo.getConnection();
            userDoc = new ArrayList<Document>();
            MongoCollection<org.bson.Document> collection = database.getCollection(collectionName);
            FindIterable<org.bson.Document> cus = collection.find();
            for (org.bson.Document a : cus.collation(null)) {
            	userDoc.add(new Document("User_Name", a.get("username")).append("Answer_Type", a.get("ques_type_code")).append("Question_Status", a.get("basic")).append("Answer", a.get("ques_type")));
            }
            return userDoc;

        } catch (Exception ex) {
            System.out.println(ex);
            return userDoc;
        } finally {

        }
    }
}
