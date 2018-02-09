package fms.util.com.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
 * @Class Name : UtilComService
 * @Description : UtilCom Service interface
 * @Modification Information
 *
 * @author c
 * @since 2015.04.21
 * @version 1.0
 * @see
 *  
 */
public interface UtilComService {
    /**
     * 리스트 조회
     * @param req
     * @return List
     * @throws Exception
     */     
	List utilComGetCodeList(EgovMap map) throws Exception;
	
}
