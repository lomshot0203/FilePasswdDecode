package fms.co.pop.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:c
*날짜:2015.05.15
 */
@Mapper
public interface CoPop002Mapper {
	/** 계약내역 조회 */
    public List<?> coPop002ctrAll01List(EgovMap map) throws Exception;

	public int coPop002TestCnt();
}
 