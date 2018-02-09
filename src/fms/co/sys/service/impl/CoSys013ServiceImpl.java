package fms.co.sys.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.sys.dao.CoSys013DAO;
import fms.co.sys.service.CoSys013Service;
import fms.util.ParseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * @Class Name : CoSys013Controller.java
 * @Description : CoSys013 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.07.13
 * @version 1.0
 * @see
 *  
 */
@Service("coSys013Service")
public class CoSys013ServiceImpl extends EgovAbstractServiceImpl implements CoSys013Service {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoSys013ServiceImpl.class);
	
	@Autowired
	private ParseUtil parseUtil;
	
    @Resource(name="coSys013DAO")
    private CoSys013DAO coSys013DAO;
    /**
     * 첨부파일 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public List listCoSys013(EgovMap map) throws Exception {
		return coSys013DAO.listCoSys013(map);
	}

    /**
     * 첨부파일 저장
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public Map saveCoSys013(EgovMap map) throws Exception{
     	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	EgovMap pMap = map; //파라미터맵을 계속 활용해야 되기 때문에 파라미터 맵을 담아두기 위한 map
    	EgovMap rtnMap= new EgovMap();
		String userId = user.getName();
    	int successCnt = 0;
    	//updInfo : JSON.stringify({"ctrNo" : PAGE_DATA.CTR_NO, "fileId" : fileId, "div" : 10})
    	String json =  (String) pMap.get("updInfo");
    	map =  parseUtil.jsonToEgovMap(json);
    	map.put("regEr", userId);
    	coSys013DAO.saveCoSys013(map); successCnt++;
    	rtnMap.put("successCnt", successCnt);
    	return rtnMap;
	}
    /**
     * 첨부파일 저장
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public Map updateCoSys013(EgovMap map) throws Exception{
	 	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		EgovMap pMap = map; //파라미터맵을 계속 활용해야 되기 때문에 파라미터 맵을 담아두기 위한 map
		EgovMap rtnMap= new EgovMap();
		String userId = user.getName();
		int successCnt = 0;
		//거주이력 처리(InhabitHistory)
		String json =  (String) pMap.get("updInfo");
		loop1 : if(json!=null&&json!=""){
	    	JSONArray jsonArray = parseUtil.jsonToJsonArray(json);
	    	for(int i = 0; i < jsonArray.size(); i++){
	    		JSONObject jsonObject = (JSONObject) jsonArray.get(i);
	    		String status = (String) jsonObject.get("status");
	    		String data = jsonObject.get("data").toString();
	    		map =  parseUtil.jsonToEgovMap(data);
	    		map.put("regEr", userId);
	    		map.put("modEr", userId);
	    		if("i".equals(status)){
	    		}else if ("u".equals(status)) {
	    			coSys013DAO.updateCoSys013(map); successCnt++;
				}else if ("d".equals(status)) {
					coSys013DAO.deleteCoSys013(map);
					coSys013DAO.deleteCoSys013Tmp(map); successCnt++;
				}else{
	    		}
	    	}
		}
		rtnMap.put("successCnt", successCnt);
		return rtnMap;
	}
	
	
}