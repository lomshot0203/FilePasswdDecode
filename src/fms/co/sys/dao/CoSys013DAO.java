package fms.co.sys.dao;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Class Name : CoSys013Controller.java
 * @Description : CoSys013 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.07.13
 * @version 1.0
 * @see
 *  
 */
@Repository("coSys013DAO")
public class CoSys013DAO extends EgovAbstractDAO {
	public List listCoSys013(EgovMap map) throws Exception {
		return list("coSys013DAO.listCoSys013", map);
	}
	public void saveCoSys013(EgovMap map) throws Exception {
		insert("coSys013DAO.saveCoSys013", map);
	}
	public void updateCoSys013(EgovMap map) throws Exception {
		update("coSys013DAO.updateCoSys013", map);
	}
	public void deleteCoSys013(EgovMap map) throws Exception {
		delete("coSys013DAO.deleteCoSys013", map);
	}
	public void deleteCoSys013Tmp(EgovMap map) throws Exception {
	    delete("coSys013DAO.deleteCoSys013Tmp", map);
	}
}
