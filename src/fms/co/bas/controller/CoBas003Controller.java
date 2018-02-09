package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas003Service;
import fms.util.ParseUtil;
import fms.util.com.service.customizeCodeService;
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
 * 작성자:민광진
 * 날짜:2015.05.11
 */
@Controller
public class CoBas003Controller {
	@Autowired
	private CoBas003Service coBas003Service;
	@Autowired
	private customizeCodeService customizeCodeService;
	@Autowired
	private ParseUtil parseUtil;
    /**페이지조회*/     
     @RequestMapping(value="/fms/co/bas/CoBas003.do")
     public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	 List<?> hdyDivList = customizeCodeService.getCodePair("CTR007", null); //휴일코드
    	 String searchHdyDiv = parseUtil.listToJson(hdyDivList);
    	 model.addAttribute("searchHdyDivList", searchHdyDiv);
    	 searchHdyDiv = searchHdyDiv.replace("codeNm", "label").replace("code", "value");
    	 model.addAttribute("selectHdyDivList", searchHdyDiv);
    	 List<?> searchStndYyList = coBas003Service.getSearchStndYySelectBox();
    	 String searchStndYy = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchStndYyList), null, "@");
    	 model.addAttribute("searchStndYyList", searchStndYy);
    	 List<?> insertStndYyList = coBas003Service.getInsertStndYySelectBox();
    	 String insertStndYy = parseUtil.jsonToSelectBox(parseUtil.listToJson(insertStndYyList), null, "@");
    	 model.addAttribute("insertStndYyList", insertStndYy);
         return "fms/co/bas/CoBas003";
     }
     /**휴일내역조회*/     
     @RequestMapping(value="/fms/co/bas/003/getHolidayHistory.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
     String getHolidayHistory(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
    	 List<?> list = coBas003Service.getHolidayHistory(map);
    	 String json = parseUtil.listToJson(list);
    	 return json;
     }
 	/**저장*/
 	@RequestMapping(value="/fms/co/bas/003/save.do", produces="application/json;charset=UTF-8")
 	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
 		List<?> list = coBas003Service.save(map);
 		String json = parseUtil.listToJson(list);
 		return json;
 	}
}
