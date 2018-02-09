/**
 * 개요
 * - 보고서통계에 대한 DAO 클래스를 정의한다.
 * 
 * 상세내용
 * - 보고서통계에 대한 등록, 조회 기능을 제공한다.
 * - 보고서통계의 조회기능은 목록조회, 상세조회로 구분된다.
 * @author lee.m.j
 * @version 1.0
 * @created 03-8-2009 오후 2:09:15
 */

package egovframework.com.sts.rst.service.impl;

import egovframework.com.cmm.service.impl.EgovComAbstractDAO;
import egovframework.com.sts.rst.service.ReprtStats;
import egovframework.com.sts.rst.service.ReprtStatsVO;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository("reprtStatsDAO")
public class ReprtStatsDAO extends EgovComAbstractDAO {

	/**
	 * 보고서 통계정보의 대상목록을 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return List - 보고서통계 목록
	 */
	@SuppressWarnings("unchecked")
	public List<ReprtStatsVO> selectReprtStatsList(ReprtStatsVO reprtStatsVO) throws Exception {
		return (List<ReprtStatsVO>) list("reprtStatsDAO.selectReprtStatsList", reprtStatsVO);
	}

    /**
	 * 보고서통계목록 페이징 총 갯수를 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return int
	 */
    public int selectReprtStatsListTotCnt(ReprtStatsVO reprtStatsVO) throws Exception {
        return (Integer)select("reprtStatsDAO.selectReprtStatsListTotCnt", reprtStatsVO);
    }
    
    /**
	 * 보고서통계목록 총 갯수를 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return int
	 */
    public int selectReprtStatsListBarTotCnt(ReprtStatsVO reprtStatsVO) throws Exception {
        return (Integer)select("reprtStatsDAO.selectReprtStatsListBarTotCnt", reprtStatsVO);
    }    
    
	/**
	 * 보고서 통계정보의 상세정보를 조회한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return ReprtStatsVO - 보고서통계 VO
	 */
    @SuppressWarnings("unchecked")
	public List<ReprtStatsVO> selectReprtStats(ReprtStatsVO reprtStatsVO) throws Exception {
		return (List<ReprtStatsVO>) list("reprtStatsDAO.selectReprtStats", reprtStatsVO);
	}

	/**
	 * 보고서 통계정보를 생성한 뒤 저장한다.
	 * @param reprtStats - 보고서통계 model
	 */
	public void insertReprtStats(ReprtStats reprtStats) throws Exception {
		insert("reprtStatsDAO.insertReprtStats", reprtStats);
	}

	/**
	 * 등록일자별 통계정보를 그래프로 표현한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return List - 보고서통계 목록
	 */
	@SuppressWarnings("unchecked")
	public List<ReprtStatsVO> selectReprtStatsBarList(ReprtStatsVO reprtStatsVO) throws Exception {
		return (List<ReprtStatsVO>) list("reprtStatsDAO.selectReprtStatsBarList", reprtStatsVO);
	}	

	/**
	 * 보고서유형별 통계정보를 그래프로 표현한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return List - 보고서통계 목록
	 */
	@SuppressWarnings("unchecked")
	public List<ReprtStatsVO> selectReprtStatsByReprtTyList(ReprtStatsVO reprtStatsVO) throws Exception {
		return (List<ReprtStatsVO>) list("reprtStatsDAO.selectReprtStatsByReprtTyList", reprtStatsVO);
	}	
	
	/**
	 * 진행상태별 통계정보를 그래프로 표현한다.
	 * @param reprtStatsVO - 보고서통계 VO
	 * @return List - 보고서통계 목록
	 */
	@SuppressWarnings("unchecked")
	public List<ReprtStatsVO> selectReprtStatsByReprtSttusList(ReprtStatsVO reprtStatsVO) throws Exception {
		return (List<ReprtStatsVO>) list("reprtStatsDAO.selectReprtStatsByReprtSttusList", reprtStatsVO);
	}		
}
