package fms.co.sys.controller;

import egovframework.com.sec.ram.service.AuthorManage;
import egovframework.com.sec.ram.service.AuthorManageVO;
import egovframework.com.sec.ram.service.EgovAuthorManageService;
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
 * @Class Name : CoSys002Controller.java
 * @Description : CoSys002Controller class
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
public class CoSys002Controller {

    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;
	
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
     @RequestMapping(value="/fms/co/sys/CoSys002.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys002";
     }
     
	/**
	 * 목록을 조회한다. 
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys002SelectAuthorList.do")
	public void selectAuthorList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		AuthorManageVO authorManageVO = new AuthorManageVO();
		authorManageVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건 
		authorManageVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색Keyword
		authorManageVO.setFirstIndex(0);
		authorManageVO.setRecordCountPerPage(999999999);

		/* 결과 리스트 반환*/
		List<?> list = egovAuthorManageService.selectAuthorList(authorManageVO);

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
	@RequestMapping(value = "/fms/co/sys/CoSys002SaveAuthor.do")
	public void saveAuthor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		AuthorManage authorManage = null;
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
			authorManage = new AuthorManage();
			authorManage.setAuthorCode((String) map.get("authorCode")); // 권한코드
			authorManage.setAuthorNm((String) map.get("authorNm")); // 권한명
			authorManage.setAuthorDc((String) map.get("authorDc")); // 권한설명
			if("u".equals(updStatus)){
				egovAuthorManageService.updateAuthor(authorManage);
				succesCnt++;
			}else if("i".equals(updStatus)){
				egovAuthorManageService.insertAuthor(authorManage);
				succesCnt++;
			}else if("d".equals(updStatus)){
				egovAuthorManageService.deleteAuthor(authorManage);
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
