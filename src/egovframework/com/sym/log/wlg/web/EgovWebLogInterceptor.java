package egovframework.com.sym.log.wlg.web;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.sym.log.wlg.service.EgovWebLogService;
import egovframework.com.sym.log.wlg.service.WebLog;
import egovframework.com.sym.mnu.mpm.service.EgovMenuManageService;
import egovframework.com.sym.mnu.mpm.service.MenuManageVO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Class Name : EgovWebLogInterceptor.java
 * @Description : 웹로그 생성을 위한 인터셉터 클래스
 * @Modification Information
 *
 *    수정일        수정자         수정내용
 *    -------      -------     -------------------
 *    2009. 3. 9.   이삼섭         최초생성
 *    2011. 7. 1.   이기하         패키지 분리(sym.log -> sym.log.wlg)
 *
 * @author 공통 서비스 개발팀 이삼섭
 * @since 2009. 3. 9.
 * @version
 * @see
 *
 */
public class EgovWebLogInterceptor extends HandlerInterceptorAdapter {

    /** EgovMenuManageService */
    @Resource(name = "meunManageService")
    private EgovMenuManageService menuManageService;

	@Resource(name="EgovWebLogService")
	private EgovWebLogService webLogService;

	/**
	 * 웹 로그정보를 생성한다.
	 *
	 * @param HttpServletRequest request, HttpServletResponse response, Object handler
	 * @return
	 * @throws Exception
	 */
	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modeAndView) throws Exception {

		WebLog webLog = new WebLog();
		String reqURL = request.getRequestURI();
		String uniqId = "";

    	/* Authenticated  */
        Boolean isAuthenticated = EgovUserDetailsHelper.isAuthenticated();
    	if(isAuthenticated.booleanValue()) {
			LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
			uniqId = user.getUniqId();
    	}

		webLog.setUrl(reqURL);
		webLog.setRqesterId(uniqId);
		webLog.setRqesterIp(request.getRemoteAddr());

		webLogService.logInsertWebLog(webLog);

		/* 메뉴바 */
        ModelAndView mav=new ModelAndView();
        EgovMap map = new EgovMap();
        MenuManageVO menuManageVO = new MenuManageVO();
        LoginVO user = EgovUserDetailsHelper.isAuthenticated()? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser():null;

            if(EgovUserDetailsHelper.isAuthenticated() && user!=null){
                menuManageVO.setTmpId(user.getId());
                menuManageVO.setTmpPassword(user.getPassword());
                menuManageVO.setTmpUserSe(user.getUserSe());
                menuManageVO.setTmpName(user.getName());
                menuManageVO.setTmpEmail(user.getEmail());
                menuManageVO.setTmpOrgnztId(user.getOrgnztId());
                menuManageVO.setTmpUniqId(user.getUniqId());

                String timeCheck = menuManageService.selectLastTime(menuManageVO);

                request.setAttribute("TIME_CHECK", timeCheck);


                request.setAttribute("TOP_MENU_LIST",  getTopMenuList(menuManageVO));
                request.setAttribute("LEFT_MENU_LIST", getLeftMenuList(menuManageVO));
            }
	}

    /**
     * 상단메뉴가지고 오기
     * @param request
     * @param response
     * @param userData
     * @return
     * @throws Exception
     */
    public List<?> getTopMenuList(MenuManageVO vo) throws Exception {
        List<?> menuList =  menuManageService.selectMainMenuHead(vo);
        return menuList;
    }

    /**
     * 좌측메뉴 가지고오기
     * @param request
     * @param response
     * @param userData
     * @return
     * @throws Exception
     */
    public List<?> getLeftMenuList(MenuManageVO vo) throws Exception{
        List<?> menuList=null;

        return menuList;
    }
}
