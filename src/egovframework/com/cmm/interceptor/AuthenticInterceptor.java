package egovframework.com.cmm.interceptor;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.cts.FmsConstant;
import fms.co.hist.dao.CoHist001Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.ModelAndViewDefiningException;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 인증여부 체크 인터셉터
 * @author 공통서비스 개발팀 서준식
 * @since 2011.07.01
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *
 *   수정일      수정자          수정내용
 *  -------    --------    ---------------------------
 *  2011.07.01  서준식          최초 생성
 *  2011.09.07  서준식          인증이 필요없는 URL을 패스하는 로직 추가
 *  </pre>
 */


public class AuthenticInterceptor extends HandlerInterceptorAdapter {
	
	@Autowired
	private CoHist001Mapper coHist001DAO;
	
	Logger logger = LoggerFactory.getLogger(AuthenticInterceptor.class) ;

	/**
	 * 세션에 계정정보(LoginVO)가 있는지 여부로 인증 여부를 체크한다.
	 * 계정정보(LoginVO)가 없다면, 로그인 페이지로 이동한다.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

		boolean isPermittedURL = false;

		LoginVO loginVO = (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser();
		if(loginVO != null ){//메인은 공통
			EgovMap map = new EgovMap();
			map.put("url", request.getRequestURI());
			map.put("uniqId", loginVO.getUniqId());
			//TODO: 권한코드(로그인한 사용자의author_code 가져올것) 
			
			int exsitAuth = coHist001DAO.selectCountByAuthNUrl(map);
			if(exsitAuth > 0  ){
				logger.info("url 접근권한 있음!!!");
				isPermittedURL = true;	
			}else{
				logger.info("url 접근권한 없음!!!");
				request.getSession().invalidate();
				ModelAndView modelAndView = new ModelAndView("redirect:"+FmsConstant.URL_MAIN);
//				ModelAndView modelAndView = new ModelAndView("redirect:/uat/uia/egovLoginUsr.do");
				throw new ModelAndViewDefiningException(modelAndView);
				//isPermittedURL = false;
			}
			
		} else{
				ModelAndView modelAndView = new ModelAndView("redirect:/uat/uia/egovLoginUsr.do");
				throw new ModelAndViewDefiningException(modelAndView);
		}
		return isPermittedURL;
	}

}
