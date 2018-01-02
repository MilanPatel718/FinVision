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
	
	/**
	 * @param PortfolioName
	 * @return boolean
	 * @throws SQLException
	 * Creates portfolio with requested name, given that no table with the same name exists
	 */
	public boolean createPortfolio(String PortfolioName) throws SQLException{
		Statement stmt=null;
		Connection connection=null;
		try{
		connection=DBConnector.getConnection();
		stmt=(Statement) connection.createStatement();
		String query="CREATE TABLE "+PortfolioName+ 
					  " (Ticker VARCHAR(20) not NULL, "+
					  "Name VARCHAR(20), "+
					  "PRIMARY KEY (Ticker))";
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
	
	/**
	 * @param Ticker
	 * @param stockName
	 * @return boolean
	 * @throws SQLException
	 * Adds Stocks to portfolio with Ticker as primary key
	 */
	public boolean addStock(String Ticker, String stockName, String portfolioName) throws SQLException{
		Statement stmt=null;
		Connection connection=null;
		try{
		connection=DBConnector.getConnection();
		stmt=(Statement) connection.createStatement();
		String query="INSERT INTO "+portfolioName+
					  " VALUES ("+"'"+Ticker+"'"+","+"'"+stockName+"'"+")";
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

	/**
	 * @param portfolioName
	 * @return
	 * @throws SQLException
	 * Retrieves list of stocks in a given portfolio
	 */
	public List<String> retrieveStocks(String portfolioName, Boolean Check) throws SQLException {
		List<String> stockNames=new ArrayList<>();
		if(Check){
			Connection connection=null;
			ResultSet rs=null;
			try{
			connection=DBConnector.getConnection();
			PreparedStatement ps=connection.prepareStatement("SELECT * FROM "+portfolioName);
			rs=ps.executeQuery();
			while(rs.next()){
				stockNames.add(rs.getString("Ticker").toUpperCase());
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
		}
		
		else{
			Connection connection=null;
			ResultSet rs=null;
			try{
			connection=DBConnector.getConnection();
			PreparedStatement ps=connection.prepareStatement("SELECT * FROM "+portfolioName);
			rs=ps.executeQuery();
			while(rs.next()){
				stockNames.add((rs.getString("Ticker")+ " "+ rs.getString("Name")).toUpperCase());
			}
			
			}
			catch(Exception e){
				e.printStackTrace();
			}
			
		}
		return stockNames;
	
	}
	
	/**
	 * @param Original
	 * @param rename
	 * @return
	 * @throws SQLException
	 * Executes query to rename portfolio
	 */
	public boolean renamePortfolio(String Original, String rename) throws SQLException{
		Connection connection=null;
		Statement stmt=null;
		try{
			connection=DBConnector.getConnection();
			stmt=(Statement) connection.createStatement();
			String query="RENAME TABLE "+Original+ 
						  " TO "+ rename;
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
	
	/**
	 * @param portfolioName
	 * @param Ticker
	 * @return
	 * @throws SQLException
	 * Deletes specified stock in current portfolio
	 */
	public boolean deleteStock(String portfolioName, String Ticker) throws SQLException{
		Connection connection=null;
		Statement stmt=null;
		try{
			connection=DBConnector.getConnection();
			stmt=(Statement) connection.createStatement();
			String query="DELETE FROM "+portfolioName+ 
						  " WHERE Ticker= "+ "'"+Ticker+"'";
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
