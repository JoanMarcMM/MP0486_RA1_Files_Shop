package dao;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import model.Employee;
import model.Product;

public class DaoImplObjectDB implements Dao {

    private EntityManagerFactory emf;
    private EntityManager em;

    public DaoImplObjectDB() {
    }

    @Override
    public void connect() {
        if (emf == null || !emf.isOpen()) {
            emf = Persistence.createEntityManagerFactory(
                "objectdb:C:/Users/Mark/git/MP0486_RA1_Files_Shop/objects/employee.odb"
            );
        }
        if (em == null || !em.isOpen()) {
            em = emf.createEntityManager();
        }
    }

    @Override
    public void disconnect() {
        if (em != null && em.isOpen()) {
            em.close();
        }
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }

    @Override
    public Employee getEmployee(int employeeId, String password) {
        if (em == null || !em.isOpen()) {
            connect();
        }

        TypedQuery<Employee> query = em.createQuery(
            "SELECT e FROM Employee e WHERE e.employeeId = :id AND e.password = :password",
            Employee.class
        );
        query.setParameter("id", employeeId);
        query.setParameter("password", password);

        List<Employee> results = query.getResultList();
        return results.isEmpty() ? null : results.get(0);
    }

    @Override
    public int beginTotalProduct(Dao dao) {
        return 0;
    }

    @Override
    public ArrayList<Product> getInventory() {
        return new ArrayList<>();
    }

    @Override
    public boolean writeInventory(ArrayList<Product> inventory) {
        return true;
    }

	@Override
	public void addProduct(Product product) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void updateProduct(Product product) throws SQLException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteProduct(int id) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}