package Listeners;

import java.sql.Connection;
import java.sql.SQLException;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

import Utils.DBConnectionManager;

@WebListener
public class AppContextListener implements ServletContextListener {

	//If the server is closed close the connection with the db
	@Override
	public void contextDestroyed(ServletContextEvent servletContextEvent) {
		Connection con = (Connection)servletContextEvent.getServletContext().getAttribute("DBConnection");
		try{
			con.close();
			System.out.println("DB connection closed successfully!");
		}catch(SQLException ex){
			ex.printStackTrace();
		}

	}

	// When the server initializes connect to the db
	@Override
	public void contextInitialized(ServletContextEvent servletContextEvent) {
		ServletContext ctx = servletContextEvent.getServletContext();
		
		// Initialize the DB connection
		String dbURL = ctx.getInitParameter("dbURL");
		String user = ctx.getInitParameter("dbUser");
		String pwd = ctx.getInitParameter("dbPassword");
		
		try{
			DBConnectionManager connectionManager = new DBConnectionManager(dbURL, user, pwd);
			ctx.setAttribute("DBConnection", connectionManager.getConnection());
			System.out.println("DB connection initialized successfully");
		}catch(ClassNotFoundException ex){
			ex.printStackTrace();
		}catch(SQLException ex){
			ex.printStackTrace();
		}
	}
}
