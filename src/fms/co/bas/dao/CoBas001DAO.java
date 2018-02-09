package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.EgovAbstractDAO;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Class Name : CoBas001DAO.java
 * @Description : TestYy DAO Class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.10
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Repository("coBas001DAO")
public class CoBas001DAO extends EgovAbstractDAO {
    /**
	 * TEST_YY list
	 * @param EgovMap
	 * @return list
	 * @exception Exception
	 */
    public List coBas001YyList(EgovMap map) throws Exception {
        return list("coBas001DAO.coBas001YyList", map);
    }
    
    
    /**
	 * Test_area list
	 * @param EgovMap
	 * @return list
	 * @exception Exception
	 */
    public List coBas001AreaList(EgovMap map) throws Exception {
        return list("coBas001DAO.coBas001AreaList", map);
    }
    
    /**
	 * area update
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001AreaUpdate(EgovMap map) throws Exception {
        update("coBas001DAO.coBas001AreaUpdate", map);
    }
    
    /**
	 * area insert
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001AreaInsert(EgovMap map) throws Exception {
        insert("coBas001DAO.coBas001AreaInsert", map);
    }
    
    /**
	 * area delete
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001AreaDelete(EgovMap map) throws Exception {
        delete("coBas001DAO.coBas001AreaDelete", map);
    }


    /**
	 * Yy update
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001YyUpdate(EgovMap map) throws Exception {
        update("coBas001DAO.coBas001YyUpdate", map);
    }
    
    /**
	 * Yy insert
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001YyInsert(EgovMap map) throws Exception {
        insert("coBas001DAO.coBas001YyInsert", map);
    }
    
    /**
	 * Yy delete
	 * @param EgovMap
	 * @return 
	 * @exception Exception
	 */
    public void coBas001YyDelete(EgovMap map) throws Exception {
        delete("coBas001DAO.coBas001YyDelete", map);
    }
    
    
	/**
	 * TEST_YY을 등록한다.
	 * @param vo - 등록할 정보가 담긴 TestYyVO
	 * @return 등록 결과
	 * @exception Exception
	 *
    public String insertTestYy(TestYyVO vo) throws Exception {
        return (String)insert("testYyDAO.insertTestYy_S", vo);
    }

    /**
	 * TEST_YY을 수정한다.
	 * @param vo - 수정할 정보가 담긴 TestYyVO
	 * @return void형
	 * @exception Exception
	 *
    public void updateTestYy(TestYyVO vo) throws Exception {
        update("testYyDAO.updateTestYy_S", vo);
    }

    /**
	 * TEST_YY을 삭제한다.
	 * @param vo - 삭제할 정보가 담긴 TestYyVO
	 * @return void형 
	 * @exception Exception
	 *
    public void deleteTestYy(TestYyVO vo) throws Exception {
        delete("testYyDAO.deleteTestYy_S", vo);
    }

    /**
	 * TEST_YY을 조회한다.
	 * @param vo - 조회할 정보가 담긴 TestYyVO
	 * @return 조회한 TEST_YY
	 * @exception Exception
	 *
    public TestYyVO selectTestYy(TestYyVO vo) throws Exception {
        return (TestYyVO) select("testYyDAO.selectTestYy_S", vo);
    }

    /**
	 * TEST_YY 목록을 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return TEST_YY 목록
	 * @exception Exception
	 *
    public List<?> selectTestYyList(TestYyDefaultVO searchVO) throws Exception {
        return list("testYyDAO.selectTestYyList_D", searchVO);
    }

    /**
	 * TEST_YY 총 갯수를 조회한다.
	 * @param searchMap - 조회할 정보가 담긴 Map
	 * @return TEST_YY 총 갯수
	 * @exception
	 *
    public int selectTestYyListTotCnt(TestYyDefaultVO searchVO) {
        return (Integer)select("testYyDAO.selectTestYyListTotCnt_S", searchVO);
    }
*/



}
