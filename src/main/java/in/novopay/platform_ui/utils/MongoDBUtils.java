package in.novopay.platform_ui.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import org.apache.log4j.BasicConfigurator;
import org.bson.Document;
import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class MongoDBUtils extends JavaUtils {

	private MongoDatabase db;
	private MongoCollection<Document> coll;
	private MongoClient mongo_client;
	private MongoClientURI uri;

	// private static Logger log = Logger.getLogger(MongoDBUtils.class);

	public void connectMongo(String db_name, String db_col_name) {

		BasicConfigurator.configure();

		// Mongodb initialization parameters.
		String auth_user = getMongoDetailsfromIni("mongoDbUserName"),
				auth_pwd = getMongoDetailsfromIni("mongoDbPassword"), dbUrl = getMongoDetailsfromIni("mongoDbUrl"),
				encoded_pwd = "";

		try {
			encoded_pwd = URLEncoder.encode(auth_pwd, "UTF-8");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}

		// Mongodb connection string.
		String client_url = "mongodb://" + auth_user + ":" + encoded_pwd + "@" + dbUrl + db_name;

		uri = new MongoClientURI(client_url);

		// Connecting to the mongodb server using the given client uri.
		mongo_client = new MongoClient(uri);

		// Fetching the database from the mongodb.
		db = mongo_client.getDatabase(db_name);

		// Fetching the collection from the mongodb.
		coll = db.getCollection(db_col_name);
	}

	public void getDocument() {

		connectMongo("novopayCms", "fullerton_semi_static_data");

		// Performing a read operation on the collection.
		FindIterable<Document> fi = coll.find(new BasicDBObject("collectionOfficerEmployeeId", "111"));
		MongoCursor<Document> cursor = fi.iterator();
		try {
			while (cursor.hasNext()) {
				Log.info(cursor.next().toJson());
			}
		} finally {
			cursor.close();
		}
	}

	public String getOffMobNum() {

		connectMongo("novopayCms", "fullerton_semi_static_data");

		// Performing a read operation on the collection.
		FindIterable<Document> fi = coll.find(new BasicDBObject("collectionOfficerEmployeeId", "111"));
		MongoCursor<Document> cursor = fi.iterator();
		ArrayList<String> arr = new ArrayList<String>();
		String str;
		while (cursor.hasNext()) {
			str = (String) cursor.next().get("collectionOfficerMobileNumber");
			arr.add(str);
		}
		return arr.get(0);
	}
	
	public String getAddMobNum() {

		connectMongo("novopayCms", "fullerton_semi_static_data");

		// Performing a read operation on the collection.
		FindIterable<Document> fi = coll.find(new BasicDBObject("collectionOfficerEmployeeId", "111"));
		MongoCursor<Document> cursor = fi.iterator();
		ArrayList<String> arr = new ArrayList<String>();
		String str;
		while (cursor.hasNext()) {
			str = (String) cursor.next().get("additionalMobileNumber");
			arr.add(str);
		}
		return arr.get(0);
	}

	public void updateValues(String empId, String date, int totalDueAmount, int totalPaidAmount, String status,
			String officerMobNum, String addMobNum) {

		connectMongo("novopayCms", "fullerton_semi_static_data");

		BasicDBObject employeeId = new BasicDBObject("collectionOfficerEmployeeId", empId);
		BasicDBObject dueDate = new BasicDBObject("dueDate", date);
		BasicDBObject dueAmount = new BasicDBObject("totalDueAmount", totalDueAmount);
		BasicDBObject paidAmount = new BasicDBObject("paidAmount", totalPaidAmount);
		BasicDBObject payStatus = new BasicDBObject("paymentStatus", status);
		BasicDBObject offMobNumber = new BasicDBObject("collectionOfficerMobileNumber", officerMobNum);
		BasicDBObject addMobNumber = new BasicDBObject("additionalMobileNumber", addMobNum);
		
		// Performing an update operation on the collection.
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", employeeId));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", dueDate));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", dueAmount));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", paidAmount));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", payStatus));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", offMobNumber));
		coll.updateOne(new BasicDBObject(), new BasicDBObject("$set", addMobNumber));
		System.out.println("Records updated");
	}
}