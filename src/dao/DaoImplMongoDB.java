package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Objects;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoException;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.result.UpdateResult;
import static com.mongodb.client.model.Filters.*;
import static com.mongodb.client.model.Updates.*;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplMongoDB implements Dao {
	String uri;
	MongoDatabase mongoDatabase;
	MongoCollection<Document> collectionInventory ;
	MongoCollection<Document> collectionEmployee;
	MongoCollection<Document> collectionHistoricalInventory;
	
	
	public DaoImplMongoDB() {
		super();
		uri = "mongodb://localhost:27017";
		MongoClientURI mongoClientURI = new MongoClientURI(uri);
		MongoClient mongoClient = new MongoClient(mongoClientURI);
		
		this.mongoDatabase = mongoClient.getDatabase("shop");
		this.collectionInventory = mongoDatabase.getCollection("inventory");
		this.collectionHistoricalInventory = mongoDatabase.getCollection("historical_inventory");
		this.collectionEmployee = mongoDatabase.getCollection("employee");
	}

	@Override
	public int beginTotalProduct(Dao dao) {
		int num=getTotalProducts();
		num++;
		return num;
	}
	
	@Override
	public void connect() {
	}
	

	@Override
	public void disconnect() {	
	}
	
	public int getTotalProducts() {
		
		
		try {
			Iterable<Document> documents = collectionHistoricalInventory.find();
			int id=0;
			for (Document doc : documents) {
				 if(doc.getInteger("id")>id) {
					 id=doc.getInteger("id");
				 };
			    }
			
			return id++; 
			} catch (MongoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return 0; 
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		Employee employee = null;

		Iterable<Document> documents = collectionEmployee.find();
		for (Document doc : documents) {
			 if(doc.getString("password").equals(password) && Objects.equals(doc.getInteger("employeeId"), employeeId)) {
				 Employee employeeFound = new Employee(doc.getInteger("employeeId"), doc.getString("name"),doc.getString("password"));
				 employee=employeeFound;
			 }
		    }
		
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		ArrayList<Product> inventory = new ArrayList<Product>();
		Iterable<Document> documents = collectionInventory.find();
		for (Document doc : documents) {
			Number n = doc.get("wholesalerPrice", Number.class); 
			double wholesalerPrice = (n == null) ? 0.0 : n.doubleValue();
			 Amount amount = new Amount (wholesalerPrice);
		        Product product = new Product(
		        		doc.getInteger("id"),
		        		doc.getString("name"),
		        		amount,
		        		doc.getBoolean("available"),
		        		doc.getInteger("stock")
		        		);
		        inventory.add(product);
		    }
		
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {

		try {
		for(Product product : inventory) {
			Document document = new Document("_id", new ObjectId())
					.append("id", product.getId())
					.append("name", product.getName())
					.append("wholesalerPrice", product.getWholesalerPrice().getValue())
					.append("available", product.isAvailable())
					.append("stock", product.getStock());
			collectionHistoricalInventory.insertOne(document);
		}
			
		} catch (MongoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public void addProduct(Product product) throws MongoException {

	
			
			Document document = new Document("_id", new ObjectId())
					.append("id", product.getId())
					.append("name", product.getName())
					.append("wholesalerPrice", product.getWholesalerPrice().getValue())
					.append("available", product.isAvailable())
					.append("stock", product.getStock());
			collectionInventory.insertOne(document);

		
		
	}

	public void updateProduct(Product product) throws MongoException {

		
		
		UpdateResult result = collectionInventory.updateOne(
				 eq("id",product.getId()),
				 set("stock",product.getStock())
				);

		
	}
	
	public void deleteProduct(int id) throws SQLException{
		
		DeleteResult result = collectionInventory.deleteOne(eq("id",id));

		
	}

}
