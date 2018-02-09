package egovframework.com.sec.security.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


/**
 *
 * @author 공통서비스 개발팀 서준식
 * @since 2011. 8. 29.
 * @version 1.0
 * @see
 *
 * <pre>
 * 개정이력(Modification Information)
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011. 8. 29.    서준식        최초생성
 *
 *  </pre>
 */

public class EgovSpringSecurityLogoutFilter implements Filter {

	@SuppressWarnings("unused")
	private FilterConfig config;

	private static final Logger LOGGER = LoggerFactory.getLogger(EgovSpringSecurityLogoutFilter.class);

	public void destroy() {}


	public void doFilter(ServletRequest request, ServletResponse response,
                         FilterChain chain) throws IOException, ServletException {

		String requestURL = ((HttpServletRequest)request).getRequestURI();
		LOGGER.debug(requestURL);

		((HttpServletRequest)request).getSession().setAttribute("loginVO", null);
		((HttpServletResponse)response).sendRedirect(((HttpServletRequest)request).getContextPath() + "/j_spring_security_logout");

	}

	public void init(FilterConfig filterConfig) throws ServletException {

		this.config = filterConfig;

	}
}