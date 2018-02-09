package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:최세원
*날짜:2015.05.26
 */
@Mapper
public interface CoBas008Mapper {

	/** 기준년 검색 셀렉트박스 생성*/
    public List<?> getSearchStndYySelectBox() throws Exception;

	/**월평균소득 조회*/
    public List<?> selectCoBas008(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void insertCoBas008(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void updateCoBas008(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void deleteCoBas008(EgovMap map) throws Exception;
}
