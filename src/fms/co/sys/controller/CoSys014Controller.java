package fms.co.sys.controller;

import egovframework.com.cmm.LoginVO;
import egovframework.com.uss.umt.service.EgovUserManageService;
import egovframework.com.uss.umt.service.UserManageVO;
import egovframework.com.utl.sim.service.EgovFileScrty;
import egovframework.rte.fdl.property.EgovPropertyService;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.List;

/**
 * @Class Name : CoSys014Controller.java
 * @Description :  Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.21
 * @version 1.0
 * @see
 *
 *  Copyright (C)  All right reserved.
 */

@Controller
public class CoSys014Controller {
    /** EgovPropertyService */
    @Autowired
    protected EgovPropertyService propertiesService;

	/** userManageService */
	@Autowired
	private EgovUserManageService userManageService;

	/** parseUtil */
	@Autowired
	private ParseUtil parseUtil;

    /**
     * 페이지 조회
     * @param request
     * @param response
     * @return string
     * @throws Exception
     */
     @RequestMapping(value="/fms/co/sys/CoSys014.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys014";
     }

	/**
	 * 목록 조회
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys014SelectUserList.do")
	public void selectUserList(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 파라미터 */
		 EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

		 /* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		 UserManageVO userManageVO = new UserManageVO();
		 HttpSession session =  request.getSession();
		 LoginVO loginVo = (LoginVO)session.getAttribute("loginVO");

		 userManageVO.setSbscrbSttus((String) map.get("sbscrbSttus")); // 검색조건-회원상태     (0, A, D, P)
		 userManageVO.setSearchCondition((String) map.get("searchCondition")); // 검색조건
		 userManageVO.setSearchKeyword((String) map.get("searchKeyword")); // 검색값
		 userManageVO.setFirstIndex(0);
		 userManageVO.setRecordCountPerPage(999999999);
		 userManageVO.setEmplyrId(loginVo.getId());



		/* 결과 리스트 반환*/
		List<?> list = userManageService.selectUserList2(userManageVO);

		/* 검색결과 리턴 */
		String jsonString = parseUtil.listToJson(list);
		ViewUtil.jsonSuccess(response, jsonString);
	}

	/**
	 * 사용자목록을 저장한다
     * @param request
     * @param response
	 * @return jsonString
	 * @throws Exception
	 */
	@RequestMapping(value = "/fms/co/sys/CoSys014SaveUser.do")
	public void saveUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
		/* 변수선언 */
		int succesCnt=0;
		String strMsg = "";
		HashMap map;
		UserManageVO userManageVO = null;
		ObjectMapper om = new ObjectMapper();
		JSONParser jp	= new JSONParser();

		/* 파라미터 */
		EgovMap egovMap  = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		JSONArray ja	= (JSONArray) jp.parse(egovMap.get("updInfo").toString());

		/* 전자정부 서비스 이용을 위해 VO에 map 값 입력 */
		for(int i = 0;  i < ja.size(); i +=1){
			map = new HashMap();
			JSONObject jo		= (JSONObject) ja.get(i);
			JSONObject rowData	= (JSONObject) jo.get("data");
			String updStatus	= (String) jo.get("status");

			map = parseUtil.jsonStringToHashMap(rowData.toJSONString());
			userManageVO = new UserManageVO();
			userManageVO.setUniqId((String) map.get("uniqId")); // 회원 고유번호
			userManageVO.setEmplyrId((String) map.get("emplyrId")); // 아이디
			userManageVO.setUserTy((String) map.get("userTy"));//사용자 타입, 삭제시 사용 : 업무사용자 USR03
			userManageVO.setEmplyrNm((String) map.get("emplyrNm"));// 이름
			userManageVO.setAreaNo((String) map.get("areaNo"));//전화번호1
			userManageVO.setHomemiddleTelno((String) map.get("homemiddleTelno"));//전화번호2 homemiddleTelno로 사용 - 테이블 컬럼명은 houseMiddleTelno 임
			userManageVO.setHomeendTelno((String) map.get("homeendTelno")); // homeendTelno 로 사용 - 테이블 컬럼명은 houseEndTelno 임
			userManageVO.setPassword((String) map.get("password")); // 비밀번호
			userManageVO.setEmplyrSttusCode("P"); // 가입상태
			userManageVO.setEmailAdres((String) map.get("emailAdres")); // 이메일
			//고정값
			userManageVO.setPasswordHint("P01"); // 비밀번호 힌트
			userManageVO.setPasswordCnsr("경기도시공사"); // 비밀번호 힌트 답
			userManageVO.setZip("100775"); // 우편번호
			userManageVO.setHomeadres("서울 중구 무교동 한국정보화진흥원"); // 주소
			userManageVO.setOrgnztId(null); // 조직
			userManageVO.setGroupId(null); // 그룹

			if("u".equals(updStatus)){
				if(! "".equals(userManageVO.getPassword())){
					//패스워드 암호화
					String pass = EgovFileScrty.encryptPassword(userManageVO.getPassword(), userManageVO.getEmplyrId());
					userManageVO.setPassword(pass);
					userManageService.updatePassword(userManageVO);
				}

				//업무사용자 수정시 히스토리 정보를 등록한다.
				//무결성 오류로 제외 userManageService.insertUserHistory(userManageVO);
				userManageService.updateUser(userManageVO);
				succesCnt++;
			}else if("i".equals(updStatus)){
				//입력한 사용자아이디의 중복여부를 체크하여 사용가능여부를 확인
				int usedCnt = userManageService.checkIdDplct((String) map.get("emplyrId"));
				if(usedCnt>0){
					strMsg += ((strMsg.length()>1) ? "\n" : "")+ (String) map.get("emplyrId") + " 아이디는 이미 사용중 입니다.";
				}else{
					userManageService.insertUser(userManageVO);
					succesCnt++;
				}
			}else if("d".equals(updStatus)){
				userManageService.deleteUser(userManageVO.getUserTy()+":"+userManageVO.getUniqId());
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
