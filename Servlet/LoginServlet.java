package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.mysql.cj.jdbc.PreparedStatement;

import Beans.User;

/**
 * Servlet implementation class LoginServlet
 */
@WebServlet("/LoginServlet")
public class LoginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;


	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public LoginServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// Get the input
		String username = request.getParameter("user");
		String pwd = request.getParameter("password");
		String errorMsg = null;
		
		// Error check it
		if (username == null || username == "") {
			errorMsg="Username cannot be empty!";
		}
		if(pwd == null || pwd == ""){
			errorMsg="Password cannot be empty!";
		}
		
		if(errorMsg != null){
			forwardError(request, response, errorMsg);
		}
		// if all is ok, query db for the username
		else{
			Connection con = (Connection)getServletContext().getAttribute("DBConnection");
			PreparedStatement stm = null;
			ResultSet rs = null;
			
			try{
				stm = (PreparedStatement) con.prepareStatement("select id, fullname, email,"
						+ " username, password, level"
						+ " from USERS"
						+ " where username=?;");
				stm.setString(1, username);
				rs = stm.executeQuery();
				
				// if such username exists
				if(rs != null && rs.next()){
					//if the password is correct (this will become a hased compare/storage)
					if(rs.getString("password").equals(pwd)){
						// Create a user from the db
						User user = new User(rs.getString("username"), rs.getString("fullname"),
								rs.getString("email"), rs.getString("level"), rs.getInt("id"));
						// Pass the user obj to the session
						HttpSession session = request.getSession();
						session.setAttribute("User", user);
						// Check the users clearance and redirect 
						if(user.getLevel().equals("secr")){
							response.sendRedirect("protected/secretary_panel.jsp");
						}else if(user.getLevel().equals("tech")){
							response.sendRedirect("protected/technician_panel.jsp");
						}
					}else{
						errorMsg = "Incorrect Password";
						forwardError(request, response, errorMsg);
					}
				}
				else{
					errorMsg = "Username not found";
					forwardError(request, response, errorMsg);
				}
			}
			catch(SQLException ex){
				ex.printStackTrace();
			}
			finally{
				try {
					rs.close();
					stm.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	// Simple method to throw a bootstrap alert for errors.
	private void forwardError(HttpServletRequest request, HttpServletResponse response, String errorMsg) 
			throws IOException, ServletException{
		
		RequestDispatcher rd = getServletContext().getRequestDispatcher("/login.html");
		PrintWriter out = response.getWriter();
		out.println("<div class=\"container\">");
		out.println("<div class=\"alert alert-warning alert-dismissible\">");
		out.println("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>");
		out.println("<strong>Warning!</strong>" + errorMsg + ".");
		out.println("</div>");
		out.println("</div>");
						
		rd.include(request, response);
	}
}
