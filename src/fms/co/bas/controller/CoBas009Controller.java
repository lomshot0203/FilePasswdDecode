package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas009Service;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
*작성자:최세원
*날짜:2017.02.06
 */
@Controller
public class CoBas009Controller {
	@Autowired
	private CoBas009Service coBas009Service;
	@Autowired
	private ParseUtil parseUtil;
	 /**페이지조회*/
	 @RequestMapping(value="/fms/co/bas/CoBas009.do")
	 public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

	     return "fms/co/bas/CoBas009";
	 }

    /**지원한도 조회*/
	@RequestMapping(value="/fms/co/bas/CoBas009/selectList.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getSupportLimits(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas009Service.selectCoBas009(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }

	/**탭3 저장*/
	@RequestMapping(value="/fms/co/bas/CoBas009/save.do")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		EgovMap rtnMap = new EgovMap();
		rtnMap = (EgovMap) coBas009Service.save(map);
		rtnMap.put("errorMsg", "");
		ViewUtil.jsonSuccess(response, parseUtil.mapToJson(rtnMap));
	}

}
