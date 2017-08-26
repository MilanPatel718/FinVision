package application;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.*;


/**
 * @author Milan Patel
 *
 */
public class DBConnector {
	
	//Reference to self
	private static DBConnector instance= new DBConnector(); 
	public static final String driver= "com.mysql.jdbc.Driver";
	public static String url = null;  
	public static String user = null;
	public static String pass = null;  
	  
	   //constructor
	   private DBConnector(){  
		   try{
			   Class.forName(driver);
		   }
		   catch (ClassNotFoundException e){
			   e.printStackTrace();
		   }
	   }
	   
	   //Establish MySQL connection
	   private Connection createConnection(){
		   Connection connection=null;
		   try {
			BufferedReader br=new BufferedReader(new FileReader("Credentials.txt"));
			try {
				url=br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				user=br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				pass=br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		   try{
			   connection=DriverManager.getConnection(url, user, pass);
		   }
		   catch (SQLException e){
			   System.out.println("Unable to connect to database");
		   }
		   return connection;
	   }
	   
	   public static Connection getConnection(){  
	     return instance.createConnection();
	   }
	   
}



