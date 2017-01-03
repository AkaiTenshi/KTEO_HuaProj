package Utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnectionManager {
	private Connection connection;

	// Interface to connect to our database.
	public DBConnectionManager(String dbURL, String usr, String pwd) throws ClassNotFoundException, SQLException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		this.connection = DriverManager.getConnection(dbURL, usr, pwd);
	}
	
	// returs the connection obj
	public Connection getConnection(){
		return this.connection;
	}

}
