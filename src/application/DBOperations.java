package application;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import com.mysql.jdbc.Statement;

/**
 * @author milan
 * Class of instance methods that connect with Database to create a persistent state application
 */
public class DBOperations {
	
	/**
	 * @return PortfolioNames
	 * @throws SQLException 
	 * Retrives list of all Portfolio names (tables in DB) 
	 */
	public List<String> retrieveNames() throws SQLException{
		  ResultSet rs=null;
		  Connection connection=null;
		  List<String> PortfolioNames=new ArrayList<>();
		  
		  connection=DBConnector.getConnection();
		  DatabaseMetaData Md=connection.getMetaData();
		  rs = Md.getTables(null, null, "%", null);
		  while(rs.next()){
			  PortfolioNames.add(rs.getString(3));
		  }
		  
		  return PortfolioNames;
		
	}
	
	/**
	 * @param deletePortfolio
	 * @return boolean
	 * @throws SQLException
	 * Deletes selected portfolio from database, returns true if successful and false otherwise
	 */
	public boolean deletePortfolio(String deletePortfolio) throws SQLException{
		Statement stmt=null;
		Connection connection=null;
		try{
		connection=DBConnector.getConnection();
		stmt=(Statement) connection.createStatement();
		String query="DROP TABLE "+deletePortfolio;
		stmt.executeUpdate(query);
		}
		catch(SQLException e){
			e.printStackTrace();
			return false;
		}
		finally{
		      try{
		         if(stmt!=null)
		            connection.close();
		      }catch(SQLException e){
		      }
		      try{
		         if(connection!=null)
		            connection.close();
		      }catch(SQLException e){
		         e.printStackTrace();
		      }
		   }
		return true;
	}
}
