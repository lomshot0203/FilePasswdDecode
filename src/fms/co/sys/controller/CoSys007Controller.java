package fms.co.sys.controller;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.sym.ccm.cca.service.CmmnCode;
import egovframework.com.sym.ccm.cca.service.CmmnCodeVO;
import egovframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
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
 * @Class Name : CoSys007Controller.java
 * @Description : CoSys007Controller class
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
public class CoSys007Controller {

    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;
	
	/** EgovCcmCmmnCodeManageService */
	@Autowired
	private EgovCcmCmmnCodeManageService cmmnCodeManageService;
	
	@Autowired
	private ParseUtil parseUtil;

    /**
     * 페이지 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/sys/CoSys007.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys007";
     }
     
 	
	/**
	 * 목록을 조회한다. 
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys007SelectCmmnCodeList.do")
	public void selectCmmnCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		CmmnCodeVO searchVO = new CmmnCodeVO();
		searchVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건 
		searchVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색Keyword
		searchVO.setFirstIndex(0);
		searchVO.setRecordCountPerPage(999999999);
		
		/* 결과 리스트 반환*/
		List<?> list = cmmnCodeManageService.selectCmmnCodeListFMS(searchVO);

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
	@RequestMapping(value = "/fms/co/sys/CoSys007SaveCmmnCode.do")
	public void saveCmmnCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		CmmnCode cmmnCode = null;
		ObjectMapper om = new ObjectMapper();
		LoginVO user = EgovUserDetailsHelper.isAuthenticated()? (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser():null;//사용자 정보
		
		/* 파라미터 */
		EgovMap egovMap  = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		JSONArray ja = parseUtil.jsonToJsonArray(egovMap.get("updInfo").toString());
		String strReturn = "";
		
		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		for(int i = 0;  i < ja.size(); i +=1){
			map = new HashMap();
			JSONObject jo		= (JSONObject) ja.get(i);
			JSONObject rowData	= (JSONObject) jo.get("data");
			String updStatus	= (String) jo.get("status");
			
			map = parseUtil.jsonStringToHashMap(rowData.toJSONString());
			cmmnCode = new CmmnCode();
			cmmnCode.setCodeId((String) map.get("codeId"));
			cmmnCode.setCodeIdNm((String) map.get("codeIdNm"));
			cmmnCode.setCodeIdDc((String) map.get("codeIdDc"));
			cmmnCode.setClCode("FMS");
			cmmnCode.setUseAt((String) map.get("useAt"));
			cmmnCode.setMappingDc1((String) map.get("mappingDc1"));
			cmmnCode.setMappingDc2((String) map.get("mappingDc2"));
			cmmnCode.setMappingDc3((String) map.get("mappingDc3"));

			if("u".equals(updStatus)){
				cmmnCode.setLastUpdusrId(user.getUniqId());
		    	cmmnCodeManageService.updateCmmnCode(cmmnCode);
				succesCnt++;
			}else if("i".equals(updStatus)){
				if (cmmnCode.getClCode() == null||cmmnCode.getClCode().equals("")) {
					strReturn += ((strReturn.length()>1) ? "\n" : "")+ map.get("codeIdNm")+" : 코드ID값이 없습니다.";
				}else{
					cmmnCode.setFrstRegisterId(user.getUniqId());
			    	cmmnCodeManageService.insertCmmnCode(cmmnCode);
			    	succesCnt++;
				}
			}else if("d".equals(updStatus)){
				cmmnCodeManageService.deleteCmmnCode(cmmnCode);
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
