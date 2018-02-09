package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;
import java.util.Map;

/**
*작성자:최세원
*날짜:2017.02.06
 */
public interface CoBas009Service {

	/**지원한도 조회*/
    List<?> selectCoBas009(EgovMap map) throws Exception;

	/**저장*/
    Map save(EgovMap map) throws Exception;
}
