package fms.co.bas.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.com.cmm.util.EgovUserDetailsHelper;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.dao.CoBas004Mapper;
import fms.co.bas.service.CoBas004Service;
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
*날짜:2015.05.13
 */
@Service
public class CoBas004ServiceImpl extends EgovAbstractServiceImpl implements CoBas004Service {
    @Autowired
    private CoBas004Mapper coBas004DAO;
	@Autowired
	private ParseUtil parseUtil;
	/**주민번호 복호화*/
	public String getDecRRN(String encRRN) throws Exception {
    	return coBas004DAO.getDecRRN(encRRN);
    }
    /**고객정보 조회*/
    public List<?> getCustomerInformation(EgovMap map) throws Exception {
    	return coBas004DAO.getCustomerInformation(map);
    }
    /**고객상세 조회*/
    public List<?> getMoreInformation(EgovMap map) throws Exception {
        return coBas004DAO.getMoreInformation(map);
    }
    /**고객변경 조회*/
    public List<?> getChangeInformation(EgovMap map) throws Exception {
        return coBas004DAO.getChangeInformation(map);
    }
    /**고객정보 저장*/
    public EgovMap saveCustomerInformation(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("customerInformationUpdate");
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
    				coBas004DAO.deleteBanAcnInformation(map); //고객 계좌정보 삭제(프로시저 미포함)
    				coBas004DAO.deleteCgpInformation(map); //담당자 정보 삭제(프로시저 미포함)
    			}
        		coBas004DAO.saveCustomerInformation(map); dataAccessCount++;
        		resultMap.put("title", "customerInformationGrid");
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
    /**고객상세 저장*/
    public EgovMap saveMoreInformation(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("moreInformationUpdate");
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
        		coBas004DAO.saveMoreInformation(map);
        		dataAccessCount++;

        		resultMap.put("title", "moreInformationGrid");
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
    /**고객변경 저장*/
    public EgovMap saveChangeInformation(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("changeInformationUpdate");
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
        		coBas004DAO.saveChangeInformation(map); dataAccessCount++;
        		resultMap.put("title", "changeInformationGrid");
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
    	list.add(saveCustomerInformation(map));
    	list.add(saveMoreInformation(map));
    	list.add(saveChangeInformation(map));
    	list.add(saveBanAcnInformation(map));
    	list.add(saveCgpInformation(map));
    	return list;
    }

    /**계약번호를 이용하여 계약구분을 조회*/
	@Override
	public String getCtrGb(String ctrGb) throws Exception {
		return coBas004DAO.getCtrGb(ctrGb);
	}

	/**고객 계좌정보 조회*/
	public List<?> getBanAcnInformation(EgovMap map) throws Exception {
		return coBas004DAO.getBanAcnInformation(map);
	}

	/**고객 계좌정보 저장*/
    public EgovMap saveBanAcnInformation(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("banAcnInformationUpdate");
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
        			coBas004DAO.saveBanAcnInformation(map); dataAccessCount++;
        		}else if ("u".equals(status)) {
        			coBas004DAO.updateBanAcnInformation(map); dataAccessCount++;;
    			}else if ("d".equals(status)) {
    				coBas004DAO.deleteBanAcnInformation(map); dataAccessCount++;
    				resultMap.put("oRetCode", map.get("oRetCode")); //처리결과코드(S=정상,E=에러)
    				resultMap.put("oRetMsg", map.get("oRetMsg")); //처리결과MSG
    				if("E".equals(map.get("oRetCode"))){
    					resultMap=null; //에러시 더이상 돌지 못하도록 초기화.
    					return null;
    				}
    			}
        		resultMap.put("title", "banAcnInformationGrid");
        		resultMap.put("errCd", map.get("errCd"));
        		resultMap.put("errMsg", map.get("errMsg"));
        		resultMap.put("row", jsonObject.get("rownum"));
        		resultMap.put("dataAccessCount", dataAccessCount);
        	}
        	return resultMap;
    	}else{
    		return null;
    	}
    }

    /**담당자 정보 조회*/
	public List<?> getCgpInformation(EgovMap map) throws Exception {
		return coBas004DAO.getCgpInformation(map);
	}

	/**담당자 정보 저장*/
    public EgovMap saveCgpInformation(EgovMap map) throws Exception {
    	EgovMap resultMap = new EgovMap();
    	LoginVO user = (LoginVO) EgovUserDetailsHelper.getAuthenticatedUser();
		String userId = user.getName();
    	int dataAccessCount = 0;
    	String json =  (String) map.get("cgpInformationUpdate");
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
        			coBas004DAO.saveCgpInformation(map); dataAccessCount++;
        		}else if ("u".equals(status)) {
        			coBas004DAO.updateCgpInformation(map); dataAccessCount++;;
    			}else if ("d".equals(status)) {
    				coBas004DAO.deleteCgpInformation(map); dataAccessCount++;
    				resultMap.put("oRetCode", map.get("oRetCode")); //처리결과코드(S=정상,E=에러)
    				resultMap.put("oRetMsg", map.get("oRetMsg")); //처리결과MSG
    				if("E".equals(map.get("oRetCode"))){
    					resultMap=null; //에러시 더이상 돌지 못하도록 초기화.
    					return null;
    				}
    			}
        		resultMap.put("title", "banAcnInformationGrid");
        		resultMap.put("errCd", map.get("errCd"));
        		resultMap.put("errMsg", map.get("errMsg"));
        		resultMap.put("row", jsonObject.get("rownum"));
        		resultMap.put("dataAccessCount", dataAccessCount);
        	}
        	return resultMap;
    	}else{
    		return null;
    	}
    }

    /**구분(개인/단체)로 주민번호 복호화*/
	@Override
	public String getDecRsdtNo(EgovMap map) throws Exception {
		return coBas004DAO.getDecRsdtNo(map);
	}






}
