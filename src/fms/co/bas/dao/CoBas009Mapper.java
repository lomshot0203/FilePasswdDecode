package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:최세원
*날짜:2015.05.26
 */
@Mapper
public interface CoBas009Mapper {

	/**월평균소득 조회*/
    public List<?> selectCoBas009(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void insertCoBas009(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void updateCoBas009(EgovMap map) throws Exception;

    /**월평균소득 저장*/
    public void deleteCoBas009(EgovMap map) throws Exception;
}
