package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name="Inventory")
public class Product {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = true)
	private int id;
	@Column
    private String name;
	@Transient
    private Amount publicPrice;
	@Transient
    private Amount wholesalerPrice;
	@Column
	private double price;
	@Column
    private boolean available;
	@Column
    private int stock;
	@Transient
    private static int totalProducts;
    
    
    public final static double EXPIRATION_RATE=0.60;
    
    public Product() {
	}
    
	public Product(String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.id=totalProducts;
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
		this.available = available;
		this.stock = stock;
		totalProducts++;
	
	}
	
	
	
	public Product(int id,String name, Amount wholesalerPrice, boolean available, int stock) {
		super();
		this.id=id;
		this.name = name;
		this.wholesalerPrice = wholesalerPrice;
		this.publicPrice = new Amount(wholesalerPrice.getValue() * 2);
		this.available = available;
		this.stock = stock;

	}
	
	public void beginTotalProducts(int num) {
		this.totalProducts=num;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	

	public Amount getPublicPrice() {
		return publicPrice;
	}

	public void setPublicPrice(Amount publicPrice) {
		this.publicPrice = publicPrice;
	}

	public Amount getWholesalerPrice() {
		return wholesalerPrice;
	}

	public void setWholesalerPrice(Amount wholesalerPrice) {
		this.wholesalerPrice = wholesalerPrice;
	}

	public boolean isAvailable() {
		return available;
	}

	public void setAvailable(boolean available) {
		this.available = available;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}
	
	

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public static int getTotalProducts() {
		return totalProducts;
	}

	public static void setTotalProducts(int totalProducts) {
		Product.totalProducts = totalProducts;
	}
	
	public void expire() {
		this.publicPrice.setValue(this.getPublicPrice().getValue()*EXPIRATION_RATE); ;
	}
	
	@Override
	public String toString() {
		return "Product [name=" + name + ", publicPrice=" + publicPrice + ", wholesalerPrice=" + wholesalerPrice
				+ ", available=" + available + ", stock=" + stock + "]";
	}

	
	
	
	
	

    

    
}
