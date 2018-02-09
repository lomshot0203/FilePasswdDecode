package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.28
 */
@Mapper
public interface CoBas006Mapper {
		/**중복체크*/
	    public int duplicateAcnCd(EgovMap map) throws Exception;
		/**계좌조회*/
	    public List<?> getAccountList(EgovMap map) throws Exception;
	    /**계좌저장*/
	    public void saveAccountList(EgovMap map) throws Exception;
}
