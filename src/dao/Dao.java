package dao;

import java.sql.SQLException;
import java.util.ArrayList;

import model.Employee;
import model.Product;

public interface Dao {
	
	public void connect();

	public void disconnect();
	
	public ArrayList<Product> getInventory();
	
	public boolean writeInventory(ArrayList<Product> inventory);

	public void addProduct(Product product) throws SQLException;
	
	public void updateProduct(Product product) throws SQLException;
	
	public void deleteProduct(int id)throws SQLException ;
	
	public Employee getEmployee(int employeeId, String password);
	
	public int beginTotalProduct (Dao dao);
}
