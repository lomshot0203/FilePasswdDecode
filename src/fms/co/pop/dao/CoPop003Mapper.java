package fms.co.pop.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
 * @Class Name : CoPop003Mapper.java
 * @Description : CoPop003Mapper
 * @Modification Information
 *
 * @author c
 * @since 2015.04.27
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Mapper
public interface CoPop003Mapper {

	/**
	 * 주소 목록 조회
	 * @param map
	 * @return list
	 */
    public List<?> coPop003AddrList(EgovMap map) throws Exception;
    
    
}
