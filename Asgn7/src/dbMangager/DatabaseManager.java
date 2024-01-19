package dbMangager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;


public class DatabaseManager {
	private static DatabaseManager instance;
	private Connection connection;
	private static final String DEFAULT_CONNECTION_URL = "jdbc:sqlserver://cypress.csil.sfu.ca;user=s_tqc;password=3HQ2723rq4m36MtE";
	private DatabaseManager() {
		try {
			connection = DriverManager.getConnection(DEFAULT_CONNECTION_URL);
		} catch (SQLException e) {
			System.out.println("fail to connect to CSIL; exit now\n\n");
			return;
		}
	}
	
	public Connection getConnection() {
        return this.connection;
    }
	
	 public static synchronized DatabaseManager getInstance() {
	        if (instance == null) {
	            instance = new DatabaseManager();
	        }
	        return instance;
	    }
	
	public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
	
	
}
