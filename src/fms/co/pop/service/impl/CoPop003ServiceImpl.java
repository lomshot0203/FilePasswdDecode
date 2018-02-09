package fms.co.pop.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.pop.dao.CoPop003Mapper;
import fms.co.pop.service.CoPop003Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


/**
 * @Class Name : CoPop003ServiceImpl.java
 * @Description : CoPop003ServiceImpl
 * @Modification Information
 *
 * @author c
 * @since 2015.04.27
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Service
public class CoPop003ServiceImpl extends EgovAbstractServiceImpl implements
        CoPop003Service {
	
	private static final Logger log = LoggerFactory.getLogger(CoPop003ServiceImpl.class);
	
    @Autowired
    private CoPop003Mapper coPop003DAO;
    
	public List<?> coPop003AddrList(EgovMap map) throws Exception {
		return coPop003DAO.coPop003AddrList(map);
	}

}
