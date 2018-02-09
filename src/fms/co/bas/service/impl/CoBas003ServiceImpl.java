package fms.co.bas.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.dao.CoBas003Mapper;
import fms.co.bas.service.CoBas003Service;
import fms.util.ParseUtil;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.30
 */
@Service
public class CoBas003ServiceImpl extends EgovAbstractServiceImpl implements CoBas003Service {
	@Autowired
	private CoBas003Mapper coBas003DAO;
	@Autowired
	private ParseUtil parseUtil;
	/**휴일내역조회*/
    public List<?> getHolidayHistory(EgovMap map) throws Exception {
        return coBas003DAO.getHolidayHistory(map);
    }
	/**기준년 검색 셀렉트박스 생성*/
    public List<?> getSearchStndYySelectBox() throws Exception {
        return coBas003DAO.getSearchStndYySelectBox();
    }
	/**기준년 입력 셀렉트박스 생성*/
    public List<?> getInsertStndYySelectBox() throws Exception {
        return coBas003DAO.getInsertStndYySelectBox();
    }
    /**휴일내역 저장*/
    public EgovMap saveHolidayHistory(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("holidayHistoryUpdate");
    	if(json!=null&&json!=""){
        	JSONArray jsonArray = parseUtil.jsonToJsonArray(json);
        	for(int i = 0; i < jsonArray.size(); i++){
        		JSONObject jsonObject = (JSONObject) jsonArray.get(i);
        		String status = (String) jsonObject.get("status");
        		String data = jsonObject.get("data").toString();
        		map =  parseUtil.jsonToEgovMap(data);
        		map.put("regEr", userId);
        		map.put("modEr", userId);
        		if("i".equals(status)){
        			map.put("crudType", "I");
        		}else if ("u".equals(status)) {
        			map.put("crudType", "U");
    			}else if ("d".equals(status)) {
    				map.put("crudType", "D");
    			}
        		coBas003DAO.saveHolidayHistory(map); dataAccessCount++;
        		resultMap.put("title", "holidayHistoryGrid");
        		resultMap.put("errCd", map.get("errCd"));
        		resultMap.put("errMsg", map.get("errMsg"));
        		resultMap.put("row", jsonObject.get("rownum"));
        		resultMap.put("dataAccessCount", dataAccessCount);
        		if(map.get("errCd").equals("E")){
        			try {
						throw new SQLException();
					} catch (SQLException e) {
						TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
						return resultMap;
					}
        		}
        	}
        	return resultMap;
    	}else{
    		return null;
    	}
    }
    /**저장*/
    public List<?> save(EgovMap map) throws Exception {
    	List<EgovMap> list = new ArrayList<EgovMap>();
    	list.add(saveHolidayHistory(map));
    	return list;
    }
}
