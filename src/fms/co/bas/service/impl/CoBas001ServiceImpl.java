package fms.co.bas.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.dao.CoBas001Mapper;
import fms.co.bas.service.CoBas001Service;
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
*날짜:2015.05.11
 */
@Service
public class CoBas001ServiceImpl extends EgovAbstractServiceImpl implements CoBas001Service {
    @Autowired
    private CoBas001Mapper coBas001DAO;
	@Autowired
	private ParseUtil parseUtil;
	/**중복체크*/
	public int duplicateBizCd(EgovMap map) throws Exception {
		return coBas001DAO.duplicateBizCd(map);
	}
    /**사업년도 조회*/
    public List<?> getBusinessYear(EgovMap map) throws Exception {
    	return coBas001DAO.getBusinessYear(map);
    }
    /**사업년도 검색 셀렉트박스 생성*/
    public List<?> getSearchBizYySelectBox() throws Exception {
    	return coBas001DAO.getSearchBizYySelectBox();
    }
    /**관리지역 검색 셀렉트박스 생성*/
    public List<?> getSearchMngZonNmSelectBox(EgovMap map) throws Exception {
    	return coBas001DAO.getSearchMngZonNmSelectBox(map);
    }

    /**관리지역 검색 셀렉트박스 생성 name/code*/
    public List<?> getSearchMngZonNmSelectBoxCd(EgovMap map) throws Exception {
    	return coBas001DAO.getSearchMngZonNmSelectBoxCd(map);
    }

    /**관리지역 조회*/
    public List<?> getManagementArea(EgovMap map) throws Exception {
        return coBas001DAO.getManagementArea(map);
    }
    /**임대료 입금계좌 입력 은행+계좌 셀렉트 박스 생성*/
    public List<?> getBankPlusAccountSelectBox() throws Exception {
    	return coBas001DAO.getBankPlusAccountSelectBox();
    }
    /**사업년도 저장*/
    public EgovMap saveBusinessYear(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("businessYearUpdate");
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
        		coBas001DAO.saveBusinessYear(map); dataAccessCount++;
        		//사업년도 신규 저장시 관리지역을 전부 저장한다.
        		if("i".equals(status)){
        			coBas001DAO.saveAllManagementArea(map);
        		}
        		resultMap.put("title", "businessYearGrid");
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
    /**관리지역 저장*/
    public EgovMap saveManagementArea(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("managementAreaUpdate");
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
        		coBas001DAO.saveManagementArea(map); dataAccessCount++;
        		resultMap.put("title", "managementAreaGrid");
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
    	list.add(saveBusinessYear(map));
    	list.add(saveManagementArea(map));
    	return list;
    }
}
