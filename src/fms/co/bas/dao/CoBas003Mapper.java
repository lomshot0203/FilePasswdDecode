package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.11
 */
@Mapper
public interface CoBas003Mapper {
		/** 휴일내역조회*/
	    public List<?> getHolidayHistory(EgovMap map) throws Exception;
		/** 기준년 검색 셀렉트박스 생성*/
	    public List<?> getSearchStndYySelectBox() throws Exception;
		/** 기준년 입력 셀렉트박스 생성*/
	    public List<?> getInsertStndYySelectBox() throws Exception;
	    /**휴일내역 저장*/
	    public void saveHolidayHistory(EgovMap map) throws Exception;
}
