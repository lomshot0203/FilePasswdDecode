package fms.co.bas.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.dao.CoBas008Mapper;
import fms.co.bas.service.CoBas008Service;
import fms.util.ParseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
*작성자:최세원
*날짜:2017.02.06
 */
@Service
public class CoBas008ServiceImpl extends EgovAbstractServiceImpl implements CoBas008Service {
    @Autowired
    private CoBas008Mapper coBas008DAO;
	@Autowired
	private ParseUtil parseUtil;

	/**기준년 검색 셀렉트박스 생성*/
    public List<?> getSearchStndYySelectBox() throws Exception {
        return coBas008DAO.getSearchStndYySelectBox();
    }

    /**조회*/
    public List<?> selectCoBas008(EgovMap map) throws Exception {
    	return coBas008DAO.selectCoBas008(map);
    }

    /**저장*/
    public Map save(EgovMap map) throws Exception {
     	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
    	EgovMap pMap = map; //파라미터맵을 계속 활용해야 되기 때문에 파라미터 맵을 담아두기 위한 map
    	EgovMap rtnMap= new EgovMap();
		String userId = user.getName();
    	int successCnt = 0;
    	//거주이력 처리(InhabitHistory)
    	String json =  (String) map.get("supportLimitsUpdate");
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
        			coBas008DAO.insertCoBas008(map); successCnt++;
        		}else if ("u".equals(status)) {
        			coBas008DAO.updateCoBas008(map); successCnt++;
    			}else if ("d".equals(status)) {
    				coBas008DAO.deleteCoBas008(map); successCnt++;
    			}
        	}
    	}
    	rtnMap.put("successCnt", successCnt);
    	return rtnMap;
    }
}
