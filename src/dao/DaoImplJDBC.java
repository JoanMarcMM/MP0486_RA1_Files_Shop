package dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import model.Amount;
import model.Employee;
import model.Product;

public class DaoImplJDBC implements Dao {
	Connection connection;
	
	@Override
	public int beginTotalProduct(Dao dao) {
		int num=getTotalProducts();
		return num;
	}
	
	@Override
	public void connect() {
		// Define connection parameters
		String url = "jdbc:mysql://localhost:3306/shop";
		String user = "root";
		String pass = "";
		try {
			this.connection = DriverManager.getConnection(url, user, pass);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		if (connection != null) {
			try {
				connection.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public int getTotalProducts() {
		connect();
		String query = "SELECT AUTO_INCREMENT\r\n"
				+ "FROM INFORMATION_SCHEMA.TABLES\r\n"
				+ "WHERE TABLE_SCHEMA = 'shop'\r\n"
				+ "  AND TABLE_NAME = 'inventory'";
		long num=0;
		
		try (PreparedStatement ps = connection.prepareStatement(query);
			     ResultSet rs = ps.executeQuery()) {

			    if (rs.next()) {
			        num= rs.getLong("AUTO_INCREMENT");
			        
			    }
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return (int)num;
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		
		connect();
		Employee employee = null;
		String query = "select * from employee where employeeId= ? and password = ? ";
		
		try (PreparedStatement ps = connection.prepareStatement(query)) { 
    		ps.setInt(1,employeeId);
    	  	ps.setString(2,password);
    	  	//System.out.println(ps.toString());
            try (ResultSet rs = ps.executeQuery()) {
            	if (rs.next()) {
            		employee = new Employee(rs.getInt(1), rs.getString(2), rs.getString(3));
            	}
            }
        } catch (SQLException e) {
			// in case error in SQL
			e.printStackTrace();
		}
    	return employee;
	}

	@Override
	public ArrayList<Product> getInventory() {
		connect();
		ArrayList<Product> inventory = new ArrayList<Product>();
		String query = "SELECT * FROM inventory";
		
		
		try {
			Statement stmt = connection.createStatement();
			ResultSet rs = stmt.executeQuery(query);
			
			while(rs.next()) {
				Double amountBD = rs.getDouble("wholesalerPrice");
				Amount amount = new Amount (amountBD);
				Product producto = new Product(rs.getInt("id"),rs.getString("name"), amount, rs.getBoolean("available"), rs.getInt("stock"));
				inventory.add(producto);
			}
			
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			
		}
		
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		connect();
		try {
			for(Product product : inventory) {
				String query="INSERT INTO historical_inventory (id_product, name, wholesalerPrice, available, stock) VALUES ("+product.getId()+", '"+product.getName()+"', '"+product.getWholesalerPrice().getValue()+"', "+product.isAvailable()+", "+product.getStock()+")";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(query);
			}
					
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public boolean addProduct(Product product) {
		connect();
		try {
			int bool;
			if(product.isAvailable()==true) {
				bool=1;
			}else {
				bool=0;
			}
				String query="INSERT INTO inventory (id,name, wholesalerPrice, available, stock) VALUES ("+product.getId()+",'"+product.getName()+"', '"+product.getWholesalerPrice().getValue()+"', "+bool+", "+product.getStock()+")";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	public boolean updateProduct(Product product) {
		connect();
		try {
				String query="UPDATE inventory SET stock="+product.getStock()+" WHERE id="+product.getId()+" ";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean deleteProduct(int id){
		connect();
		try {
				String query="DELETE FROM inventory WHERE id="+id+" ";
				Statement stmt = connection.createStatement();
				stmt.executeUpdate(query);

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

}
