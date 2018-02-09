package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.11
 */
@Mapper
public interface CoBas001Mapper {
		/**중복체크*/
    	public int duplicateBizCd(EgovMap map) throws Exception;
		/**사업년도 조회*/
	    public List<?> getBusinessYear(EgovMap map) throws Exception;
		/**관리지역 조회*/
	    public List<?> getManagementArea(EgovMap map) throws Exception;
	    /**사업년도 검색 셀렉트 박스 생성*/
	    public List<?> getSearchBizYySelectBox() throws Exception;
	    /**관리지역 검색 셀렉트 박스 생성*/
	    public List<?> getSearchMngZonNmSelectBox(EgovMap map) throws Exception;
	    /**관리지역 검색 셀렉트 박스 생성 name/code*/
	    public List<?> getSearchMngZonNmSelectBoxCd(EgovMap map) throws Exception;
	    /**임대료 입금계좌 입력 은행+계좌 셀렉트 박스 생성*/
	    public List<?> getBankPlusAccountSelectBox() throws Exception;
	    /**사업년도 저장*/
	    public void saveBusinessYear(EgovMap map) throws Exception;
	    /**관리지역 저장*/
	    public void saveManagementArea(EgovMap map) throws Exception;
	    /**관리지역 저장(사업년도 신규 저장시)*/
	    public void saveAllManagementArea(EgovMap map) throws Exception;
}
