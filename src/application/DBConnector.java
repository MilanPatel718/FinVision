package application;
import java.sql.*;


/**
 * @author Milan Patel
 *
 */
public class DBConnector {
	
	//Reference to self
	private static DBConnector instance= new DBConnector(); 
	public static final String driver= "com.mysql.jdbc.Driver";
	public static final String url = "jdbc:mysql://localhost/finvision";  
	public static final String user = "root";
	public static final String pass = "Franklinpark1!";  
	  
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
		   try{
			   System.out.println("connecting");
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



