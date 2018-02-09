package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.26
 */
public interface CoBas007Service {
	/**중복체크*/
	int duplicateBegYy(EgovMap map) throws Exception;
	/**지원한도 조회*/
    List<?> getSupportLimits(EgovMap map) throws Exception;
    /**지원한도 저장*/
	EgovMap saveSupportLimits(EgovMap map) throws Exception;
	/**저장*/
    List<?> save(EgovMap map) throws Exception;
}
