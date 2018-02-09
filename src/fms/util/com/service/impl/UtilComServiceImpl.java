package fms.util.com.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.sys.service.impl.CoSys009ServiceImpl;
import fms.util.com.dao.UtilComDAO;
import fms.util.com.service.UtilComService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Class Name : UtilComServiceImpl
 * @Description : UtilCom Service class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.21
 * @version 1.0
 * @see
 *  
 */
@Service("utilComService")
public class UtilComServiceImpl extends EgovAbstractServiceImpl implements UtilComService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CoSys009ServiceImpl.class);
	
    @Resource(name="utilComDAO")
    private UtilComDAO utilComDAO;
	
    
    /**
     * 리스트 조회
     * @param EgovMap
     * @return List
     * @throws Exception
     */     
	public List utilComGetCodeList(EgovMap map) throws Exception {
		return utilComDAO.utilComGetCodeList(map);
	}
}
