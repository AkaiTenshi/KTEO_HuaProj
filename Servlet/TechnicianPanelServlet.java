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

import com.mysql.cj.jdbc.PreparedStatement;


/**
 * Servlet implementation class TechnicianPanelServlet
 */
@WebServlet("/TechnicianPanelServlet")
public class TechnicianPanelServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public TechnicianPanelServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Get plates and desired action
		String plates = request.getParameter("plates");
		String action = request.getParameter("action");
		String message = null;

		// run appropriate method based on action choosen
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
	private void CheckGet(HttpServletRequest request, HttpServletResponse response, String plates, String message)
			throws IOException, ServletException, SQLException {
		if (plates == null || plates.equals("")) {
			message = "License plate number can not be empty!";
		}
		if (message != null) {
			forwardError(request, response, message, "warning");
		} else {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement stm = null;
			ResultSet rs = null;

			stm = (PreparedStatement) con
					.prepareStatement("select *" + " from VEHICLES_CHECK" + " where plates=?;");
			stm.setString(1, plates);
			rs = stm.executeQuery();
			
			if(rs != null && rs.next()){
				request.setAttribute("plates", plates);
				request.setAttribute("visual", rs.getString("visual"));
				request.setAttribute("stability", rs.getString("stability"));
				request.setAttribute("brakes", rs.getString("brakes"));
				request.setAttribute("suspension", rs.getString("suspension"));
				request.setAttribute("gas", rs.getString("gas"));
				request.setAttribute("lights", rs.getString("lights"));
				
				request.getRequestDispatcher("/protected/technician_panel.jsp").forward(request, response);
			} else {
				message = "No data found. This must be the first check. Fill the form and update";
				forwardError(request, response, message, "warning");
			}
			rs.close();
			stm.close();
		}
	}

	// Update the technical comments database
	private void Update(HttpServletRequest request, HttpServletResponse response, String plates, String message)
			throws IOException, ServletException, SQLException {
		if (plates == null || plates.equals("")) {
			message = "License plate number can not be empty!";
		}
		if (message != null) {
			forwardError(request, response, message, "warning");
		} else {
			Connection con = (Connection) getServletContext().getAttribute("DBConnection");
			PreparedStatement stm = null;
			ResultSet rs = null;

			// Query the db
			stm = (PreparedStatement) con
					.prepareStatement("select plates" + " from VEHICLES_CHECK" + " where plates=?;");
			stm.setString(1, plates);
			rs = stm.executeQuery();
			
			// If the vehicle exists in this db (is not his first time) update.
			if(rs != null && rs.next()){
				//execute update
				stm = (PreparedStatement) con
						.prepareStatement("update VEHICLES_CHECK "
								+ "set visual=?, stability=?, brakes=?, suspension=?, "
								+ "gas=?, lights=? "
								+ "where plates=?");
				stm.setString(1, request.getParameter("visual"));
				stm.setString(2, request.getParameter("stability"));
				stm.setString(3, request.getParameter("brakes"));
				stm.setString(4, request.getParameter("suspension"));
				stm.setString(5, request.getParameter("gas"));
				stm.setString(6, request.getParameter("lights"));
				stm.setString(7, plates);
				stm.executeUpdate();
				
				message="Vehicle updated successfuly!";
				forwardError(request, response, message, "success");
			// If it does not exist (first time check), insert it to the database
			}else{
				//execute insert
				stm = (PreparedStatement) con
						.prepareStatement("insert into VEHICLES_CHECK(visual, stability, brakes, suspension, "
								+ "gas, lights, plates)"
								+ "values(?,?,?,?,?,?,?)");
				stm.setString(1, request.getParameter("visual"));
				stm.setString(2, request.getParameter("stability"));
				stm.setString(3, request.getParameter("brakes"));
				stm.setString(4, request.getParameter("suspension"));
				stm.setString(5, request.getParameter("gas"));
				stm.setString(6, request.getParameter("lights"));
				stm.setString(7, plates);
				stm.executeUpdate();
				
				message="Vehicle data inserted successfuly!";
				forwardError(request, response, message, "success");
			}
			rs.close();
			stm.close();
		}
	}

	private void forwardError(HttpServletRequest request, HttpServletResponse response, String message, String type)
			throws IOException, ServletException {

		RequestDispatcher rd = getServletContext().getRequestDispatcher("/protected/technician_panel.jsp");
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
