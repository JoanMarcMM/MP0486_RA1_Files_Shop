package model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(name="historical_inventory")
public class ProductHistory {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name = "id", unique = true, nullable = true)
	private int id;
	@Column
    private int id_product;
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
	@CreationTimestamp
	@Column
    private LocalDateTime createdAt;
	
	public ProductHistory() {
		super();
	}

	public ProductHistory(int id_product, String name, Amount publicPrice, Amount wholesalerPrice, double price,
			boolean available, int stock) {
		super();
		this.id_product = id_product;
		this.name = name;
		this.publicPrice = publicPrice;
		this.wholesalerPrice = wholesalerPrice;
		this.price = price;
		this.available = available;
		this.stock = stock;
	}
	
	
	
}
