package fms.co.pop.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:c
*날짜:2015.05.15
 */
public interface CoPop002Service {
	/** 계약내역 조회 */
    List<?> coPop002ctrAll01List(EgovMap map) throws Exception;
}
