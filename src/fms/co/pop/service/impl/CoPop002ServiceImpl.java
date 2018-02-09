package fms.co.pop.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.pop.dao.CoPop002Mapper;
import fms.co.pop.service.CoPop002Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*작성자:c
*날짜:2015.05.15
 */
@Service
public class CoPop002ServiceImpl extends EgovAbstractServiceImpl implements CoPop002Service {
	
	private static final Logger log = LoggerFactory.getLogger(CoPop002ServiceImpl.class);
	
	@Autowired
	private CoPop002Mapper coPop002DAO;
	
	/** 계약내역 조회 */
	public List<?> coPop002ctrAll01List(EgovMap map) throws Exception {
		int aa = 0;
		aa =coPop002DAO.coPop002TestCnt();
		return coPop002DAO.coPop002ctrAll01List(map);
	}
}
