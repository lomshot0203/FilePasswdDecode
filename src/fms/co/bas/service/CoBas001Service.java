package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.11
 */
public interface CoBas001Service {
	/**중복체크*/
	int duplicateBizCd(EgovMap map) throws Exception;
	/**사업년도 조회*/
    List<?> getBusinessYear(EgovMap map) throws Exception;
    /**관리지역 조회*/
	List<?> getManagementArea(EgovMap map) throws Exception;
	/**사업년도 검색 셀렉트 박스 생성*/
	List<?> getSearchBizYySelectBox() throws Exception;
	/**관리지역 검색 셀렉트 박스 생성*/
	List<?> getSearchMngZonNmSelectBox(EgovMap map) throws Exception;
	/**관리지역 검색 셀렉트 박스 생성 name/code*/
	List<?> getSearchMngZonNmSelectBoxCd(EgovMap map) throws Exception;
    /**임대료 입금계좌 입력 은행+계좌 셀렉트 박스 생성*/
    List<?> getBankPlusAccountSelectBox() throws Exception;
    /**저장*/
    List<?> save(EgovMap map) throws Exception;
    /**사업년도 저장*/
    EgovMap saveBusinessYear(EgovMap map) throws Exception;
    /**관리지역 저장*/
    EgovMap saveManagementArea(EgovMap map) throws Exception;
}
