package fms.co.sys.controller;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.service.CmmnDetailCode;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.com.sym.ccm.cca.service.CmmnCodeVO;
import egovframework.com.sym.ccm.cca.service.EgovCcmCmmnCodeManageService;
import egovframework.com.sym.ccm.cde.service.CmmnDetailCodeVO;
import egovframework.com.sym.ccm.cde.service.EgovCcmCmmnDetailCodeManageService;
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
 * @Class Name : CoSys008Controller.java
 * @Description : CoSys008Controller class
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
public class CoSys008Controller {

    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;
	
    /** EgovCcmCmmnCodeManageService */
	@Autowired
    private EgovCcmCmmnCodeManageService cmmnCodeManageService;
    
	/** EgovCcmCmmnDetailCodeManageService */
	@Autowired
	private EgovCcmCmmnDetailCodeManageService cmmnDetailCodeManageService;
	
	@Autowired
	private ParseUtil parseUtil;

    /**
     * 페이지 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/sys/CoSys008.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys008";
     }
     
     /**
      * 셀렉트 박스 조회 - 공통코드 리스트
     * @param request
     * @param response
     * @return string
     * @throws Exception
      */     
 	@RequestMapping(value="/fms/co/sys/CoSys008GetCmmnCodeList.do")
      public void getCmmnCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		/* 결과 리스트 반환*/
		CmmnCodeVO searchCodeVO;
		searchCodeVO = new CmmnCodeVO();
		searchCodeVO.setFirstIndex(0);
		searchCodeVO.setRecordCountPerPage(999999999);
		//searchCodeVO.setSearchCondition("clCode");
		//searchCodeVO.setSearchKeyword("FMS");
		List<?> list = cmmnCodeManageService.selectCmmnCodeListFMS(searchCodeVO);

 		/* 검색결과 리턴 */
 		String jsonString = parseUtil.listToJson(list); 
 		ViewUtil.jsonSuccess(response, jsonString);
      }
 	
	/**
	 * 목록을 조회한다. 
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys008SelectCmmnDetailCodeList.do")
	public void selectCmmnCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		CmmnDetailCodeVO searchVO = new CmmnDetailCodeVO();
		searchVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건 
		searchVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색Keyword
		searchVO.setFirstIndex(0);
		searchVO.setRecordCountPerPage(999999999);
		
		/* 결과 리스트 반환*/
		List<?> list = cmmnDetailCodeManageService.selectCmmnDetailCodeListFMS(searchVO);

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
	@RequestMapping(value = "/fms/co/sys/CoSys008SaveCmmnDetailCode.do")
	public void saveCmmnCode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		CmmnDetailCode cmmnDetailCode = null;
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
			cmmnDetailCode = new CmmnDetailCode();
			cmmnDetailCode.setCodeId((String) map.get("codeId"));
			cmmnDetailCode.setCode((String) map.get("code"));
			cmmnDetailCode.setCodeNm((String) map.get("codeNm"));
			cmmnDetailCode.setCodeDc((String) map.get("codeDc"));
			cmmnDetailCode.setUseAt((String) map.get("useAt"));
			//cmmnDetailCode.setCodeIdDc((String) map.get("codeIdDc"));
			//cmmnDetailCode.setClCode((String) map.get("clCode"));
			cmmnDetailCode.setUseAt((String) map.get("useAt"));
			cmmnDetailCode.setMapping1((String) map.get("mapping1"));
			cmmnDetailCode.setMapping2((String) map.get("mapping2"));
			cmmnDetailCode.setMapping3((String) map.get("mapping3"));
			cmmnDetailCode.setSort((String) map.get("sort"));

			if("u".equals(updStatus)){
				cmmnDetailCode.setLastUpdusrId(user.getUniqId());
		    	cmmnDetailCodeManageService.updateCmmnDetailCode(cmmnDetailCode);
				succesCnt++;
			}else if("i".equals(updStatus)){
				CmmnDetailCode vo = cmmnDetailCodeManageService.selectCmmnDetailCodeDetail(cmmnDetailCode);
		    	if(vo != null){
		    		strMsg += ((strMsg.length()>1) ? "\n" : "")+ " 이미 등록된 코드가 존재합니다.";
		    	}else{
		    		cmmnDetailCode.setFrstRegisterId(user.getUniqId());
			    	cmmnDetailCodeManageService.insertCmmnDetailCode(cmmnDetailCode);
			    	succesCnt++;
		    	}
			}else if("d".equals(updStatus)){
			    cmmnDetailCodeManageService.deleteCmmnDetailCode(cmmnDetailCode);
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
