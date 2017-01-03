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

@WebFilter(
filterName = "AuthenticationFilter",
urlPatterns = { "/protected/*" }
)
public class AuthenticationFilter implements Filter {

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	//Filter to check if user is authenticated
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		
		
		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;
		
		String uri = req.getRequestURI();
		System.out.println("Requested Resource: " + uri);

		// get the session if it exists
		HttpSession session = req.getSession(false);
		
		// If sessions is not null we go down the chain. Else redirect to login page
		if(session == null){
			System.out.println("Unauthorized access request:: " + uri);
			res.sendRedirect(req.getContextPath() + "/login.html");
		} else {
			System.out.println("Authorized access request:: " + uri);
			chain.doFilter(request, response);
		}
	}

	@Override
	public void init(FilterConfig fConfig) throws ServletException {
		System.out.println("AuthorizationFilter initialized");
	}

}
