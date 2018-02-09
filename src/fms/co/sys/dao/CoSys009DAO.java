package fms.co.sys.dao;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : CoSys009Controller.java
 * @Description : CoSys009 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.20
 * @version 1.0
 * @see
 *  
 */
@Repository("coSys009DAO")
public class CoSys009DAO extends EgovAbstractDAO {
	// 시도, 시군구 검색
	public List coSys009AddrSelectBoxSidoGugunList(EgovMap map) throws Exception {
		return list("coSys009DAO.coSys009AddrSelectBoxSidoGugunList", map);
	}
	// 도로명 검색
	public List coSys009AddrSelectBoxDoroList(EgovMap map) throws Exception {
		return list("coSys009DAO.coSys009AddrSelectBoxDoroList", map);
	}
	// 지번 검색
	public List coSys009AddrSelectBoxBubList(EgovMap map) throws Exception {
		return list("coSys009DAO.coSys009AddrSelectBoxBubList", map);
	}
	
	//읍면 가져온다
	public Map coSys009SelectUpmen(EgovMap map) throws Exception {
		//System.out.println("param : "+map);
		return (Map) selectByPk("coSys009DAO.coSys009SelectUpmen", map);
	}
	
	// 리스트 검색
	public List coSys009AddrList(EgovMap map) throws Exception {
		//System.out.println("param : "+map);
		return list("coSys009DAO.coSys009AddrList", map);
	}
	public void coSys009txtDel(EgovMap map) throws Exception {
		update("coSys009DAO.coSys009txtDel", map);
	}
	public void coSys009txtUp(EgovMap map) throws Exception {
		update("coSys009DAO.coSys009txtUp", map);
	}

}
