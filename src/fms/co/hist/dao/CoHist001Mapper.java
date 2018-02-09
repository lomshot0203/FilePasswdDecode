package fms.co.hist.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
 * 
 * @Class Name : CoHist001Mapper.java
 * @Description : 
 * @Modification Information
 * @
 * @   수정일                   수정자                    수정내용
 * @  ----------          --------------        ------------------------
 * @ 2015. 5. 21.         박성용     			           최초 생성
 *
 *  @author 
 *  @version 1.0
 *  @see
 *
 */
@Mapper
public interface CoHist001Mapper {
	    public List<?> listPrivateHistory(EgovMap map) throws Exception;
	    public int inserPrivateHistory(EgovMap map) throws Exception;
	    int selectCountByAuthNUrl(EgovMap map) throws Exception;
}
