package fms.util.com.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.util.ParseUtil;
import fms.util.com.dao.customizeCodeMapper;
import fms.util.com.service.customizeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.30
 */
@Service
public class customizeCodeServiceImpl extends EgovAbstractServiceImpl implements customizeCodeService {
	@Autowired
	private customizeCodeMapper customizeCodeDAO;
	@Autowired
	private ParseUtil parseUtil;

	public List<?> getCodePair(String target, List<String> code) throws Exception {
		EgovMap egovMap = new EgovMap();
		egovMap.put("codeId", target);
		egovMap.put("code", code);
		return customizeCodeDAO.getCodePair(egovMap);
	}
}
