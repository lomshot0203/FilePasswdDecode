package fms.util.com.dao;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Class Name : UtilComDao
 * @Description : UtilCom DAO class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.21
 * @version 1.0
 * @see
 *  
 */
@Repository("utilComDAO")
public class UtilComDAO extends EgovAbstractDAO {
    /**
     * 리스트 조회
     * @param EgovMap
     * @return List
     * @throws Exception
     */     
	public List utilComGetCodeList(EgovMap map) throws Exception {
		return list("utilComDAO.utilComGetCodeList", map);
	}
}
