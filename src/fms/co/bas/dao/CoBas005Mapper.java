package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.18
 */
@Mapper
public interface CoBas005Mapper {
		/**이율 조회*/
	    public List<?> getInterestRate(EgovMap map) throws Exception;
	    /**이율 저장*/
	    public void saveInterestRate(EgovMap map) throws Exception;
}
