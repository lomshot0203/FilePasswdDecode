package fms.co.sys.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
 * @Class Name : CoSys009Controller.java
 * @Description : CoSys009 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.20
 * @version 1.0
 * @see
 *  
 */
public interface CoSys009Service {
    /**
     * 셀렉트 박스 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	List coSys009AddrSelectBoxList(EgovMap map) throws Exception;

    /**
     * 셀렉트 박스 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	List coSys009AddrList(EgovMap map) throws Exception;
	
	//coSys009txtUp
	int coSys009txtUp(String txt) throws Exception;
	
}

