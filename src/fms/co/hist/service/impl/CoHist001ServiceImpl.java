package fms.co.hist.service.impl;

import egovframework.com.cmm.LoginVO;
import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.cts.FmsConstant;
import fms.co.hist.dao.CoHist001Mapper;
import fms.co.hist.service.CoHist001Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * 
 * @Class Name : CoHist001ServiceImpl.java
 * @Description : 
 * @Modification Information
 * @
 * @   수정일                   수정자                    수정내용
 * @  ----------          --------------        ------------------------
 * @ 2015. 5. 21.          박성용 			           최초 생성
 *
 *  @author 
 *  @version 1.0
 *  @see
 *
 */
@Service
public class CoHist001ServiceImpl extends EgovAbstractServiceImpl implements CoHist001Service {
	
	@Autowired
	private CoHist001Mapper coHist001DAO; 
	/**
     * 개인정보 조회 이력 조회
     * @param map
     * @return
     * @throws Exception
     */
    public List<?> listPrivateHistory(EgovMap map) throws Exception{
    	return coHist001DAO.listPrivateHistory(map); 
    }
    
    /**
     * 개인정보 조회 이력 등록
     * @param map
     * @return
     * @throws Exception
     */
 	public int inserPrivateHistory(EgovMap searchKey, HttpServletRequest request, String type) throws Exception{
 		
 		//등록 정보 세팅
 		EgovMap eMap = new EgovMap();
 		HttpSession session =  request.getSession();
 		LoginVO loginVo = (LoginVO)session.getAttribute("loginVO");
 		eMap.put(FmsConstant.ID, loginVo.getId());
 		eMap.put(FmsConstant.IP, request.getRemoteAddr());
 		eMap.put(FmsConstant.INQUERY_TYPE, type);
 		eMap.put(FmsConstant.INQUERY_URL, request.getRequestURI());
 		eMap.put(FmsConstant.INQUERY_CONDITION, searchKey.toString().replaceAll(FmsConstant.CODE_QUOT, ""));
 		
    	return coHist001DAO. inserPrivateHistory(eMap);
    }
}
