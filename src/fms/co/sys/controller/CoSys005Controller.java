package fms.co.sys.controller;

import egovframework.com.cmm.ComDefaultVO;
import egovframework.com.sym.mnu.mpm.service.EgovMenuManageService;
import egovframework.com.sym.mnu.mpm.service.MenuManageVO;
import egovframework.com.sym.prm.service.EgovProgrmManageService;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;

/**
 * @Class Name : CoSys005Controller.java
 * @Description : CoSys005Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.30
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class CoSys005Controller {

    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;
	
	/** EgovProgrmManageService */
	@Autowired
	private EgovProgrmManageService progrmManageService;
	
	/** EgovMenuManageService */
	@Autowired
	private EgovMenuManageService menuManageService;
	
	@Autowired
	private ParseUtil parseUtil;
	
    /**
     * 페이지 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/sys/CoSys005.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys005";
     }
     
	/**
	 * 목록을 조회한다. 
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys005SelectMenuList.do")
	public void selectMenuList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		ComDefaultVO searchVO = new ComDefaultVO();
		//searchVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건 
		searchVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색Keyword
		searchVO.setFirstIndex(0);
		searchVO.setRecordCountPerPage(999999999);

		/* 결과 리스트 반환*/
		List<?> list = menuManageService.selectMenuManageList(searchVO);

		/* 검색결과 리턴 */
		String jsonString = parseUtil.listToJson(list);
		ViewUtil.jsonSuccess(response, jsonString);
	}

	/**
	 * 저장한다.
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys005SaveMenu.do")
	public void saveMenu(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		MenuManageVO menuManageVO = null;
		ObjectMapper om = new ObjectMapper();
		ComDefaultVO searchVO = null;
		
		/* 파라미터 */
		EgovMap egovMap  = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		JSONArray ja = parseUtil.jsonToJsonArray(egovMap.get("updInfo").toString());
		
		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		for(int i = 0;  i < ja.size(); i +=1){
			map = new HashMap();
			searchVO = new ComDefaultVO();
			JSONObject jo		= (JSONObject) ja.get(i);
			JSONObject rowData	= (JSONObject) jo.get("data");
			String updStatus	= (String) jo.get("status");
			
			map = parseUtil.jsonStringToHashMap(rowData.toJSONString());
			menuManageVO = new MenuManageVO();
			menuManageVO.setUpperMenuId( Integer.parseInt((String) map.get("upperMenuId")));
			menuManageVO.setMenuNo(Integer.parseInt((String) map.get("menuNo")));
			menuManageVO.setMenuOrdr(Integer.parseInt((String) map.get("menuOrdr")));
			menuManageVO.setProgrmFileNm((String) map.get("progrmFileNm"));
			menuManageVO.setMenuNm((String) map.get("menuNm"));
			menuManageVO.setMenuDc((String) map.get("menuDc"));
			searchVO.setSearchKeyword((String) map.get("progrmFileNm"));
			
			if("u".equals(updStatus)){
				menuManageService.updateMenuManage(menuManageVO);
				succesCnt++;
			}else if("i".equals(updStatus)){
				if(menuManageService.selectMenuNoByPk(menuManageVO) == 0){
					if(progrmManageService.selectProgrmNMTotCnt(searchVO)==0){
						strMsg += ((strMsg.length()>1) ? "\n" : "")+ map.get("menuNm")+" : 해당하는 프로그램이 없습니다.";
					}else{
						menuManageService.insertMenuManage(menuManageVO);
						succesCnt++;
					}
				}else{
					strMsg += ((strMsg.length()>1) ? "\n" : "")+ map.get("menuNm")+" : 동일한 메뉴번호가 존재합니다.";
				}
			}else if("d".equals(updStatus)){
				if (menuManageService.selectUpperMenuNoByPk(menuManageVO) != 0){
					strMsg += ((strMsg.length()>1) ? "\n" : "")+ map.get("menuNm")+" : 해당 메뉴의 하위메뉴를 먼저 삭제 해 주세요.";
				}else{
					String checkedMenuNoForDel = menuManageVO.getMenuNo()+"";
					menuManageService.deleteMenuManageList(checkedMenuNoForDel);
					succesCnt++;
				}
				
			}else{
				strMsg += ((strMsg.length()>1) ? "\n" : "")+ " 상태값이 없습니다.";
			}
		}
		map = new HashMap();
		map.put("successCnt", succesCnt+"");
		map.put("errorMsg", strMsg);
		ViewUtil.jsonSuccess(response, parseUtil.mapToJson(map));
	}
}
