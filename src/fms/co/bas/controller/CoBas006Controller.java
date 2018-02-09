package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas006Service;
import fms.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 작성자:민광진
 * 날짜:2015.04.28
 */
@Controller
public class CoBas006Controller {
	@Autowired
	private CoBas006Service coBas006Service;
	@Autowired
	private ParseUtil parseUtil;
	/**페이지조회*/
    @RequestMapping(value="/fms/co/bas/CoBas006.do")
    public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	return "fms/co/bas/CoBas006";
    }
    /**중복체크*/
	@RequestMapping(value="/fms/co/bas/006/duplicateAcnCd.do")
    public @ResponseBody
    String duplicateAcnCd(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  EgovMap map = parseUtil.jsonToEgovMap(searchKey);
    	  int count = coBas006Service.duplicateAcnCd(map);
          return count+"";
    }
    /**계좌조회*/
	@RequestMapping(value="/fms/co/bas/006/getAccountList.do", produces="application/json;charset=UTF-8")
    public @ResponseBody
    String getAccountList(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
    	  EgovMap map = parseUtil.jsonToEgovMap(searchKey);
    	  List<?> list = coBas006Service.getAccountList(map);
          String json = parseUtil.listToJson(list);
          return json;
    }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/006/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas006Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
