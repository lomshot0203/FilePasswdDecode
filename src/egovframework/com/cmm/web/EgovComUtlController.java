package egovframework.com.cmm.web;

import egovframework.com.cmm.CommonMenuVO;
import egovframework.com.cmm.LoginVO;
import egovframework.com.sym.mnu.mpm.service.EgovMenuManageService;
import egovframework.com.sym.mnu.mpm.service.MenuManageVO;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.fdl.security.userdetails.util.EgovUserDetailsHelper;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @Class Name : EgovComUtlController.java
 * @Description : 공통유틸리티성 작업을 위한 Controller
 * @Modification Information
 * @
 * @  수정일         수정자                   수정내용
 * @ -------    --------    ---------------------------
 * @ 2009.03.02    조재영          최초 생성
 * @ 2011.10.07    이기하          .action -> .do로 변경하면서 동일 매핑이 되어 삭제처리
 *
 *  @author 공통서비스 개발팀 조재영
 *  @since 2009.03.02
 *  @version 1.0
 *  @see
 *
 */
@Controller
public class EgovComUtlController {

    //@Resource(name = "egovUserManageService")
    //private EgovUserManageService egovUserManageService;

    /** EgovPropertyService */
    @Resource(name = "propertiesService")
    protected EgovPropertyService propertiesService;
	
    /** EgovMenuManageService */
    @Resource(name = "meunManageService")
    private EgovMenuManageService menuManageService;

    /**
	 * JSP 호출작업만 처리하는 공통 함수
	 */
	@RequestMapping(value="/EgovPageLink.do")
	public String moveToPage(@RequestParam("link") String linkPage){
		String link = linkPage;
		// service 사용하여 리턴할 결과값 처리하는 부분은 생략하고 단순 페이지 링크만 처리함
		if (linkPage==null || linkPage.equals("")){
			link="egovframework/com/cmm/egovError";
		}
		return link;
	}

    /**
	 * validato rule dynamic Javascript
	 */
	@RequestMapping("/validator.do")
	public String validate(){
		return "egovframework/com/cmm/validator";
	}
	
	
	@RequestMapping(value="/common/selectLeftList.do", produces="application/json;charset=UTF-8")//method=RequestMethod.POST
	@ResponseBody
    public String selectLeftList(HttpServletRequest request, HttpServletResponse response, @RequestBody String json) throws Exception {
		LoginVO user = EgovUserDetailsHelper.isAuthenticated()? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser():null;
        MenuManageVO menuManageVO = new MenuManageVO(); 
		String rtn = "";
    	HashMap<String, Object> map = new ObjectMapper().readValue(json, HashMap.class) ;
    	if(EgovUserDetailsHelper.isAuthenticated() && user!=null){
            menuManageVO.setTmpId(user.getId());
            menuManageVO.setTmpPassword(user.getPassword());
            menuManageVO.setTmpUserSe(user.getUserSe());
            menuManageVO.setTmpName(user.getName());
            menuManageVO.setTmpEmail(user.getEmail());
            menuManageVO.setTmpOrgnztId(user.getOrgnztId());
            menuManageVO.setTmpUniqId(user.getUniqId());
            menuManageVO.setMenuId(Integer.parseInt((String) map.get("menuId")));
        }
    	List<CommonMenuVO> dataList=menuManageService.selectMainMenuLeftV2(menuManageVO);
    	rtn = getJsonMakeConvertforSBGrid(dataList);
    	return rtn;
    }
    
    /**
     * JSON String으로 변환
     * @param response
     * @param data
     * @throws Exception
     */
    public String getJsonMakeConvertforSBGrid(Object dataList) throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        String value=mapper.writeValueAsString(dataList);
        return value;
    }
}