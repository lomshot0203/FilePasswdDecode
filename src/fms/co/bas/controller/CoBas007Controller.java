package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas007Service;
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
 * 날짜:2015.05.26
 */
@Controller
public class CoBas007Controller {
	@Autowired
	private CoBas007Service coBas007Service;
	@Autowired
	private ParseUtil parseUtil;
	 /**페이지조회*/
	 @RequestMapping(value="/fms/co/bas/CoBas007.do")
	 public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
	     return "fms/co/bas/CoBas007";
	 }
    /**중복체크*/
	@RequestMapping(value="/fms/co/bas/007/duplicateBegYy.do")
    public @ResponseBody
    String duplicateBegYy(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  EgovMap map = parseUtil.jsonToEgovMap(searchKey);
    	  int count = coBas007Service.duplicateBegYy(map);
          return count+"";
    }
    /**지원한도 조회*/
	@RequestMapping(value="/fms/co/bas/007/getSupportLimits.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getSupportLimits(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas007Service.getSupportLimits(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/007/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas007Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
