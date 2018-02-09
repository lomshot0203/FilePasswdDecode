package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas008Service;
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
public class CoBas008Controller {
	@Autowired
	private CoBas008Service coBas008Service;
	@Autowired
	private ParseUtil parseUtil;
	 /**페이지조회*/
	 @RequestMapping(value="/fms/co/bas/CoBas008.do")
	 public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {

		 List<?> searchStndYyList = coBas008Service.getSearchStndYySelectBox();
    	 String searchStndYy = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchStndYyList), null, "@");
    	 model.addAttribute("searchStndYyList", searchStndYy);

	     return "fms/co/bas/CoBas008";
	 }

    /**지원한도 조회*/
	@RequestMapping(value="/fms/co/bas/CoBas008/selectList.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getSupportLimits(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas008Service.selectCoBas008(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }

	/**탭3 저장*/
	@RequestMapping(value="/fms/co/bas/CoBas008/save.do")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		EgovMap rtnMap = new EgovMap();
		rtnMap = (EgovMap) coBas008Service.save(map);
		rtnMap.put("errorMsg", "");
		ViewUtil.jsonSuccess(response, parseUtil.mapToJson(rtnMap));
	}

}
