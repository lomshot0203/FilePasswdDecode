package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas005Service;
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
 * 날짜:2015.05.18
 */
@Controller
public class CoBas005Controller {
	@Autowired
	private CoBas005Service coBas005Service;
	@Autowired
	private ParseUtil parseUtil;
	 /**페이지조회*/
	 @RequestMapping(value="/fms/co/bas/CoBas005.do")
	 public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
	     return "fms/co/bas/CoBas005";
	 }
    /**이율 조회*/
	@RequestMapping(value="/fms/co/bas/005/getInterestRate.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getCustomerInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas005Service.getInterestRate(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/005/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas005Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
