package fms.util.com.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.04
 */
@Mapper
public interface customizeCodeMapper {
		/** 코드조회*/
		public List<?> getCodePair(EgovMap map) throws Exception;
}
