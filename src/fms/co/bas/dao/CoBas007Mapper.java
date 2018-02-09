package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.26
 */
@Mapper
public interface CoBas007Mapper {
		/**중복체크*/
		public int duplicateBegYy(EgovMap map) throws Exception;
		/**지원한도 조회*/
	    public List<?> getSupportLimits(EgovMap map) throws Exception;
	    /**지원한도 저장*/
	    public void saveSupportLimits(EgovMap map) throws Exception;
}
