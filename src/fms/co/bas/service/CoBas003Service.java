package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.11
 */
public interface CoBas003Service {
	/**휴일내역조회*/
    List<?> getHolidayHistory(EgovMap map) throws Exception;
	/**기준년 검색 셀렉트박스 생성*/
    List<?> getSearchStndYySelectBox() throws Exception; 
    /**기준년 입력 셀렉트박스 생성*/
    List<?> getInsertStndYySelectBox() throws Exception; 
    
    /**저장*/
    List<?> save(EgovMap map) throws Exception;
    /**휴일내역 저장*/
    EgovMap saveHolidayHistory(EgovMap map) throws Exception;
}
