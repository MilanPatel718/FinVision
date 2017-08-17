package application;

import java.sql.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import com.mysql.jdbc.Statement;

public class connectionTest {
	
	  public static void main(String [] args) throws SQLException, ClassNotFoundException{
		  	BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
	        System.out.println("Enter First Name:");
	        
	        String firstName;
	        try{
	        	firstName=(br.readLine().toString());
	        	br.close();
	        	connectionTest test= new connectionTest();
	        	Person person=test.getPerson(firstName);
	        	System.out.print(person.getfirstName());
	        }
	        catch(IOException e){
	        	e.printStackTrace();
	        	
	        }
		}
	  
	  public Person getPerson(String firstName){
		  ResultSet rs=null;
		  Connection connection=null;
		  Statement statement=null;
		  
		  Person person=null;
		  String query ="SELECT * FROM person WHERE first_name='" + firstName+"'";
		  try{
			  connection=DBConnector.getConnection();
			  statement=(Statement) connection.createStatement();
			  rs=statement.executeQuery(query);
			  
			  if(rs.next()){
				  person=new Person();
				  person.setfirstName(rs.getString("first_name"));
				  person.setlastName(rs.getString("last_name"));
				  person.setemail(rs.getString("email"));
			  }
		  }
			  catch(SQLException e){
				  e.printStackTrace();
			  }
			  finally{
				  if(connection!=null){
					  try{
						  connection.close();
					  }
					  catch(SQLException e){
						  e.printStackTrace();
					  }
				  }
			  }
			return person; 
		  }
}
	   
