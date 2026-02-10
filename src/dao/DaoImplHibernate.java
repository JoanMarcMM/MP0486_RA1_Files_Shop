package dao;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.query.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import model.Amount;
import model.Employee;
import model.Product;
import model.ProductHistory;

public class DaoImplHibernate implements Dao {
	
Connection connection;
private Session session;
private Transaction tx;
	
	@Override
	public int beginTotalProduct(Dao dao) {
		int num=getTotalProducts();
		return num;
	}
	
	
	
	public DaoImplHibernate() {
		super();
		init();
	}
	
	
	public void init() {
		initSession();
	}
	
	public void initSession() {
		Configuration configuration = new Configuration().configure("hibernate.cfg.xml");
		org.hibernate.SessionFactory sessionFactory = configuration.buildSessionFactory();
    	session = sessionFactory.openSession();		
	}


	public int getTotalProducts() {
		Transaction tx = null;

	    try {
	        tx = session.beginTransaction();

	        Number result = (Number) session
	            .createNativeQuery(
	                "SELECT AUTO_INCREMENT " +
	                "FROM information_schema.TABLES " +
	                "WHERE TABLE_SCHEMA = DATABASE() " +
	                "AND TABLE_NAME = 'Inventory'"
	            )
	            .getSingleResult();

	        tx.commit();

	        return result.intValue();

	    } catch (Exception e) {
	        if (tx != null) tx.rollback();
	        e.printStackTrace();
	        return 1;
	    }
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		
		 try {
		        tx = session.beginTransaction();

		        Employee employee = session.createQuery(
		                "select e from Employee e " +
		                "where e.employeeId = :employeeId and e.password = :password",
		                Employee.class
		            )
		            .setParameter("employeeId", employeeId)
		            .setParameter("password", password)
		            .uniqueResult();

		        tx.commit();
		        return employee;

		    } catch (Exception ex) {
		        if (tx != null) tx.rollback();
		        ex.printStackTrace();
		        return null;
		    }
	}

	@Override
	public ArrayList<Product> getInventory() {
		
		ArrayList<Product> inventory = new ArrayList<Product>();
		
		try {
			tx = session.beginTransaction();
			
			Query q = session.createQuery("SELECT p FROM Product p");
			
			List<Product> inventoryList =q.list();
			
			for(Product p : inventoryList) {
				Amount publicPrice = new Amount (p.getPrice()*2);
				Amount wholesalerPrice = new Amount (p.getPrice());
				p.setPublicPrice(publicPrice);
				p.setWholesalerPrice(wholesalerPrice);
				
			}
			
			System.out.println(""+inventoryList);
			inventory.addAll(inventoryList);
			tx.commit();
			
		}catch (HibernateException e) {
			if(tx!=null) tx.rollback();
		}
		
		
		return inventory;
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
		tx = session.beginTransaction();
		try {
			for(Product product : inventory) {
				
				ProductHistory productH = new ProductHistory(product.getId(), product.getName(),
															product.getPublicPrice(), product.getWholesalerPrice(), 
															product.getPrice(), product.isAvailable(), product.getStock());
				session.save(productH);
			}
				tx.commit();
				
			
					
		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); 
			e.printStackTrace();
			return false;
		}
		
		
		return true;
	}
	
	public void addProduct(Product product) {
		
		try {
			int bool;
			if(product.isAvailable()==true) {
				bool=1;
			}else {
				bool=0;
			}
				tx = session.beginTransaction();
				product.setPrice(product.getWholesalerPrice().getValue());
				session.save(product);
				tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); 
			e.printStackTrace();
			
		}
		
	}

	public void updateProduct(Product product) {
		try {
			
				tx = session.beginTransaction();
				session.save(product);
				tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); 
			e.printStackTrace();
			
		}
		
	}
	
	public void deleteProduct(int id){
		
		try {
			tx = session.beginTransaction();

	        int rows = session.createQuery("delete from Product p where p.id = :id")
	                          .setParameter("id", id)
	                          .executeUpdate();

	        tx.commit();

		} catch (HibernateException e) {
			if (tx != null)
				tx.rollback(); 
			e.printStackTrace();
			
		}
		
	}



	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

}
