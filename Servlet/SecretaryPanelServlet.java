package Servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mysql.cj.jdbc.PreparedStatement;;

/**
 * Servlet implementation class SecretaryPanelServlet
 */
@WebServlet("/SecretaryPanelServlet")
public class SecretaryPanelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public SecretaryPanelServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Get the plates and the list choice
		String plates = request.getParameter("plates");
		String action = request.getParameter("action");
		String message = null;

		// Decide what to do
		switch (action) {
		case "check":
			try {
				CheckGet(request, response, plates, message);
			} catch (SQLException e) {
				e.printStackTrace();
			}
			break;
		case "update":
			try {
				Update(request, response, plates, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			break;
		}

	}

	// This method is called if the user wants to grab data from the DB
	private void CheckGet(HttpServletRequest request, HttpServletResponse response, String plates, String message)
			throws IOException, ServletException, SQLException {
		if (plates == null || plates.equals("")) {
			message = "License plate number can not be empty!";
		}
		if (message != null) {
			forwardError(request, response, message, "warning");
		// If we got no errors query the database
		} else {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement stm = null;
			ResultSet rs = null;

			// First we check with the gov db if the vehicle exists and is isnured
			stm = (PreparedStatement) con
					.prepareStatement("select plates, is_insured" + " from GOV_DB" + " where plates=?;");
			stm.setString(1, plates);
			rs = stm.executeQuery();
			//if it exists we pull its info from out DB
			if (rs != null && rs.next()) {
				if (!rs.getBoolean("is_insured")) {
					request.setAttribute("i_fine", 200);
				}
				stm = (PreparedStatement) con.prepareStatement("select plates, owner, last_check,"
						+ " model, type, cubics" + " from VEHICLES" + " where plates=?;");
				stm.setString(1, plates);
				rs = stm.executeQuery();
				if (rs != null && rs.next()) {
					request.setAttribute("plates", rs.getString("plates"));
					request.setAttribute("owner", rs.getString("owner"));
					request.setAttribute("date", rs.getDate("last_check"));
					request.setAttribute("model", rs.getString("model"));
					request.setAttribute("type", rs.getString("type"));
					request.setAttribute("cubics", rs.getString("cubics"));
					LocalDate currDate = LocalDate.now();
					LocalDate checkDate = rs.getDate("last_check").toLocalDate();
					
					if(currDate.minusYears(checkDate.getYear()).getYear() > 2){
						request.setAttribute("o_fine", calculateFine(Integer.parseInt(rs.getString("cubics")), rs.getString("type")));
					}

					request.getRequestDispatcher("/protected/secretary_panel.jsp").forward(request, response);
				// In case the vehicle is here for the first time we must update our DB from the GOV DB
				} else {
					message = "Vehicle not found in Local DB, please Update";
					forwardError(request, response, message, "warning");
				}
			} else {
				message = "Vehicle not found";
				forwardError(request, response, message, "warning");
			}
			rs.close();
			stm.close();
		}
	}

	// Simple method to calculate overdue fines (Should we keep weight in the db too?)
	private Object calculateFine(int cubics, String type) {
		int fine = 0;
		if(type.equals("passenger") && cubics <= 1800){
			fine = 25;
		}
		if(type.equals("passenger") && cubics > 1800){
			fine = 40;
		}
		if(type.equals("truck") && cubics <= 3){
			fine = 50;
		}
		if(type.equals("truck") && cubics > 3){
			fine = 75;
		}
		return fine;
	}

	// This method is called if we want to update our db from the gov db (employees cannot and should not manualy alter anything)
	private void Update(HttpServletRequest request, HttpServletResponse response, String plates, String message)
			throws IOException, ServletException, SQLException {
		
		// Error checking
		if (plates == null || plates.equals("")) {
			message = "License plate number can not be empty!";
		}
		if (message != null) {
			forwardError(request, response, message, "warning");
		} else {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement stm = null;
			ResultSet rs = null;
			
			//Query the gov db to get the vehicle data we need
			stm = (PreparedStatement) con
					.prepareStatement("select plates, model, owner, type, cubics" + " from GOV_DB" + " where plates=?;");
			stm.setString(1, plates);
			rs = stm.executeQuery();
			
			// If vehicle exists in gov db transfer the data from there to here. Last check is set to current date.
			if(rs != null && rs.next()){
				DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
				Date date = new Date();
				
				stm = (PreparedStatement) con
						.prepareStatement("insert into VEHICLES(plates, owner, last_check, model, type, cubics) "
								+ "values(?,?,?,?,?,?);");
				stm.setString(1, rs.getString("plates"));
				stm.setString(2, rs.getString("owner"));
				stm.setString(3, df.format(date));
				stm.setString(4, rs.getString("model"));
				stm.setString(5, rs.getString("type"));
				stm.setString(6, rs.getString("cubics"));
				stm.executeUpdate();
				
				message = "New vehicle added successfuly!";
				forwardError(request, response, message, "success");
			}else{
				message="Vehicle not found!";
				forwardError(request, response, message, "warning");
			}
			rs.close();
			stm.close();
		}
	}

	// Simple error forwarding method using bootstrap alerts.
	private void forwardError(HttpServletRequest request, HttpServletResponse response, String message, String type)
			throws IOException, ServletException {

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/protected/secretary_panel.jsp");
		PrintWriter out = response.getWriter();
		out.println("<div class=\"container\">");
		out.println("<div class=\"alert alert-"+type+" alert-dismissible\">");
		out.println("<a href=\"#\" class=\"close\" data-dismiss=\"alert\" aria-label=\"close\">&times;</a>");
		out.println("<strong>"+type+"!</strong>" + message + ".");
		out.println("</div>");
		out.println("</div>");

		rd.include(request, response);
	}

}
