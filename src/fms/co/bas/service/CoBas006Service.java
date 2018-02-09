package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.28
 */
public interface CoBas006Service {
	/**중복체크*/
    int duplicateAcnCd(EgovMap map) throws Exception;
	/**계좌조회*/
    List<?> getAccountList(EgovMap map) throws Exception;
    /**계좌 저장*/
	EgovMap saveAccountList(EgovMap map) throws Exception;
	/**저장*/
    List<?> save(EgovMap map) throws Exception;
}
