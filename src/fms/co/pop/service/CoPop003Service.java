package fms.co.pop.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;


/**
 * @Class Name : CoPop003Service.java
 * @Description : CoPop003Service interface
 * @Modification Information
 *
 * @author c
 * @since 2015.04.27
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
public interface CoPop003Service {

    /**
	 * 목록을 조회한다.
	 * @param EgovMap
	 * @return List
	 * @exception Exception
	 */
    List<?> coPop003AddrList(EgovMap map) throws Exception;
}
