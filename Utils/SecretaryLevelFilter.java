package Utils;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import Beans.User;

@WebFilter(
filterName = "SecretaryLevelFilter",
urlPatterns = { "/protected/secretary_panel.jsp" }
)
public class SecretaryLevelFilter implements Filter {

    public SecretaryLevelFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	// Check if the user has clearance
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
			throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		System.out.println("Requested Resource: " + uri);

		HttpSession session = req.getSession(false);
		User user = (User) session.getAttribute("User");

		// if he doesnt redirect to the proper panel.
		if(!user.getLevel().equals("secr")){
			System.out.println("Unauthorized access request:: " + uri);
			res.sendRedirect(req.getContextPath() + "/protected/technician_panel.jsp");
		} else {
			System.out.println("Authorized access request:: " + uri);
			chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("SecretaryLevelFilter initialized");
	}

}
