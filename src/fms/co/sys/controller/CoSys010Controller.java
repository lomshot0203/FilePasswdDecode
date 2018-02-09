package fms.co.sys.controller;

import egovframework.com.sec.ram.service.AuthorManageVO;
import egovframework.com.sec.ram.service.EgovAuthorManageService;
import egovframework.com.sec.rgm.service.AuthorGroup;
import egovframework.com.sec.rgm.service.AuthorGroupVO;
import egovframework.com.sec.rgm.service.EgovAuthorGroupService;
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
 * @Class Name : CoSys010Controller.java
 * @Description : CoSys010Controller class
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
public class CoSys010Controller {

    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;
	
	/** egovAuthorGroupService */
	@Autowired
	private EgovAuthorGroupService egovAuthorGroupService;
	
	/** egovAuthorManageService */
	@Autowired
	private EgovAuthorManageService egovAuthorManageService;
	
	@Autowired
	private ParseUtil parseUtil;
	
    /**
     * 페이지 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/sys/CoSys010.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys010";
     }
     
     /**
      * 권한 셀렉트 박스 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
      */     
 	@RequestMapping(value="/fms/co/sys/CoSys010GetAuthorInfoCode.do")
      public void getAuthorInfoCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		/* 변수선언 */
 		ObjectMapper om = new ObjectMapper();
 		
 		/* 결과 리스트 반환*/
 		AuthorManageVO authorManageVO = new AuthorManageVO();
 		authorManageVO.setAuthorManageList(egovAuthorManageService.selectAuthorUsefulList(authorManageVO)); // 사용가능한 권한목록만 조회
 		List<?> list = authorManageVO.getAuthorManageList();

 		/* 검색결과 리턴 */
 		String jsonString = parseUtil.listToJson(list); 
 		ViewUtil.jsonSuccess(response, jsonString);
      }
 	
 	
	/**
	 * 목록을 조회한다. 
     * @param request
     * @param response
     * @return string
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys010SelectAuthorGroupList.do")
	public void selectAuthorGroupList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));
		
		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		AuthorGroupVO authorGroupVO = new AuthorGroupVO();
		authorGroupVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건 
		authorGroupVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색Keyword
		authorGroupVO.setFirstIndex(0);
		authorGroupVO.setRecordCountPerPage(999999999);
		
		/* 결과 리스트 반환*/
		List<?> list = egovAuthorGroupService.selectAuthorGroupList(authorGroupVO);

		/* 검색결과 리턴 */
		String jsonString = parseUtil.listToJson(list);
		ViewUtil.jsonSuccess(response, jsonString);
	}

	/**
	 * 저장한다.
     * @param request
     * @param response
     * @return string
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys010SaveAuthorGroup.do")
	public void saveAuthorGroup(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		AuthorGroup authorGroup = null;
		ObjectMapper om = new ObjectMapper();
		
		/* 파라미터 */
		EgovMap egovMap  = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		JSONArray ja = parseUtil.jsonToJsonArray(egovMap.get("updInfo").toString());
		
		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		for(int i = 0;  i < ja.size(); i +=1){
			map = new HashMap();
			JSONObject jo		= (JSONObject) ja.get(i);
			JSONObject rowData	= (JSONObject) jo.get("data");
			String updStatus	= (String) jo.get("status");
			
			map = parseUtil.jsonStringToHashMap(rowData.toJSONString());
			authorGroup = new AuthorGroup();
			authorGroup.setUniqId((String) map.get("uniqId")); // 유니크 id
			authorGroup.setMberTyCode((String) map.get("mberTyCode")); // 사용자 타입
			authorGroup.setAuthorCode((String) map.get("authorCode")); // 권한코드
			authorGroup.setPerDataYn((String) map.get("perDataYn"));   /* 개인정보 표시 */
			
			// 등록 여부가 N 일경우 신규, Y일경우 수정
			if("u".equals(updStatus)){
				if("N".equals(map.get("regYn")))
					egovAuthorGroupService.insertAuthorGroup(authorGroup);
				else
					egovAuthorGroupService.updateAuthorGroup(authorGroup);
				succesCnt++;
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
