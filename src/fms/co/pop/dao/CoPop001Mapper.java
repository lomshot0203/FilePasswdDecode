package fms.co.pop.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.18
 */
@Mapper
public interface CoPop001Mapper {
		/**주민번호 복호화*/
	    public String getDecRRN(String encRRN) throws Exception;
		/**고객정보 조회*/
	    public List<?> getCustomerInformation(EgovMap map) throws Exception;
		/**고객상세 조회*/
	    public List<?> getMoreInformation(EgovMap map) throws Exception;
		/**고객변경 조회*/
	    public List<?> getChangeInformation(EgovMap map) throws Exception;
	    /**고객정보 저장*/
	    public void saveCustomerInformation(EgovMap map) throws Exception;
	    /**고객상세 저장*/
	    public void saveMoreInformation(EgovMap map) throws Exception;
	    /**고객변경 저장*/
	    public void saveChangeInformation(EgovMap map) throws Exception;
}
