package dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import javax.xml.parsers.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.*;
import javax.xml.transform.stream.*;
import org.xml.sax.*;
import org.w3c.dom.*;

import model.Amount;
import model.Employee;
import model.Product;
import model.Sale;

public class DaoImplXML implements Dao{

	@Override
	public void connect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void disconnect() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Employee getEmployee(int employeeId, String password) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<Product> getInventory() {
		//Get inventory created from already existing shop.readInventory 	
		ArrayList<Product> inventory = new ArrayList<Product>();
			// locate file, path and name
			File f = new File(System.getProperty("user.dir") + File.separator + "files/inputInventory.txt");
			
			try {			
				// wrap in proper classes
				FileReader fr;
				fr = new FileReader(f);				
				BufferedReader br = new BufferedReader(fr);
				
				// read first line
				String line = br.readLine();
				
				// process and read next line until end of file
				while (line != null) {
					// split in sections
					String[] sections = line.split(";");
					
					String name = "";
					double wholesalerPrice=0.0;
					int stock = 0;
					
					// read each sections
					for (int i = 0; i < sections.length; i++) {
						// split data in key(0) and value(1) 
						String[] data = sections[i].split(":");
						
						switch (i) {
						case 0:
							// format product name
							name = data[1];
							break;
							
						case 1:
							// format price
							wholesalerPrice = Double.parseDouble(data[1]);
							break;
							
						case 2:
							// format stock
							stock = Integer.parseInt(data[1]);
							break;
							
						default:
							break;
						}
					}
					// Create product and add product to inventory
					Product product = new Product(name, new Amount(wholesalerPrice), true, stock);
					inventory.add(product);
					
					// read next line
					line = br.readLine();
				}
				
				
				fr.close();
				br.close();
				
				
				
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			// Return inventory
			
			return inventory;
		
	}

	@Override
	public boolean writeInventory(ArrayList<Product> inventory) {
			
		
			
			// define file name based on date
			LocalDate myObj = LocalDate.now();
			String fileName = "inventory_" + myObj.toString() + ".txt";
			
			// locate file, path and name
			File f = new File(System.getProperty("user.dir") + File.separator + "files" + File.separator + fileName);
			
			
			boolean validation = false;
			
			if(!f.exists()) {
				
				try {
					f.createNewFile();
					// wrap in proper classes
					FileWriter fw;
					fw = new FileWriter(f, true);
					PrintWriter pw = new PrintWriter(fw);
					
					// write line by line
					int counterProduct=0;
					for (Product product : inventory) {	
						// increment counter product
						counterProduct++;
						// format first line TO BE -> 1;Product:apple;Stock:2;
						System.out.print(product.getName());;
						StringBuilder line = new StringBuilder(counterProduct+";Product:"+product.getName()+";Stock=" +product.getStock()+";");
						pw.write(line.toString());
						fw.write("\n");
						
					}
					
					StringBuilder line = new StringBuilder("Total number of products:"+counterProduct+";");
					pw.write(line.toString());
					fw.write("\n");
					
					// close files
					pw.close();
					fw.close();
					
					validation=true;
					
				} catch (IOException e) {
					validation=false;
				}		
				
			}else {
				validation=false;
			}
			
			return validation;
			
		}
	
public boolean addProduct(Product product) {
		
		return true;
	}

	public boolean updateProduct(Product product) {
		
		return true;
	}
	
	public boolean deleteProduct(int id){
		
		return true;
	}

	@Override
	public int beginTotalProduct(Dao dao) {
		// TODO Auto-generated method stub
		return 0;
	}
	

}

