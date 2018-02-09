package fms.co.hist.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 
 * @Class Name : CoHist001Service.java
 * @Description : 
 * @Modification Information
 * @
 * @   수정일                   수정자                    수정내용
 * @  ----------          --------------        ------------------------
 * @ 2015. 5. 21.          박성용                			           최초 생성
 *
 *  @author 
 *  @version 1.0
 *  @see
 *
 */
public interface CoHist001Service {

	List<?> listPrivateHistory(EgovMap map) throws Exception;
    int inserPrivateHistory(EgovMap map, HttpServletRequest request, String type) throws Exception;
    
}
