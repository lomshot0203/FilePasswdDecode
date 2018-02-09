package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.18
 */
public interface CoBas005Service {
	/**이율 조회*/
    List<?> getInterestRate(EgovMap map) throws Exception;
    /**이율 저장*/
	EgovMap saveInterestRate(EgovMap map) throws Exception;
	/**저장*/
    List<?> save(EgovMap map) throws Exception;
}
