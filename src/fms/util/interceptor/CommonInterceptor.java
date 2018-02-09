package fms.util.interceptor;

import egovframework.com.cmm.LoginVO;
import egovframework.com.sym.mnu.mpm.service.EgovMenuManageService;
import egovframework.com.sym.mnu.mpm.service.MenuManageVO;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

public class CommonInterceptor extends HandlerInterceptorAdapter implements ApplicationContextAware {

    /** EgovMenuManageService */
    @Resource(name = "meunManageService")
    private EgovMenuManageService menuManageService;
    
    private ApplicationContext applicationContext;
    
	@Override
	public void setApplicationContext(ApplicationContext arg0) throws BeansException {
		this.applicationContext=applicationContext;
	}

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception{
        String curUrl = request.getRequestURI();
        MenuManageVO menuManageVO = new MenuManageVO();
        LoginVO user = EgovUserDetailsHelper.isAuthenticated()? (LoginVO)EgovUserDetailsHelper.getAuthenticatedUser():null;

            if(EgovUserDetailsHelper.isAuthenticated() && user!=null && curUrl.indexOf("fms/fmsMainView.do") >= 0){
                menuManageVO.setTmpId(user.getId());
                menuManageVO.setTmpPassword(user.getPassword());
                menuManageVO.setTmpUserSe(user.getUserSe());
                menuManageVO.setTmpName(user.getName());
                menuManageVO.setTmpEmail(user.getEmail());
                menuManageVO.setTmpOrgnztId(user.getOrgnztId());
                menuManageVO.setTmpUniqId(user.getUniqId());
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
