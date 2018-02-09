package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas001Service;
import fms.util.ParseUtil;
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
 * 날짜:2015.05.12
 */
@Controller
public class CoBas001Controller {
    @Autowired
    private CoBas001Service coBas001Service;
    @Autowired
    private ParseUtil parseUtil;
    /**페이지조회*/
    @RequestMapping(value="/fms/co/bas/CoBas001.do")
     public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	 List<?> searchBizYyList = coBas001Service.getSearchBizYySelectBox();
    	 String searchBizYy = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchBizYyList), null, "@");
    	 model.addAttribute("searchBizYyList", searchBizYy);
    	 List<?> bankPlusAccountList = coBas001Service.getBankPlusAccountSelectBox();
    	 String searchBankPlusAccountList = parseUtil.listToJson(bankPlusAccountList);
    	 String searchBankPlusAccount = parseUtil.jsonToSelectBox(searchBankPlusAccountList, "bankplusaccount", "@");
    	 model.addAttribute("searchBankPlusAccount", searchBankPlusAccount);
         return "fms/co/bas/CoBas001";
    }
    /**중복체크*/
	@RequestMapping(value="/fms/co/bas/001/duplicateBizCd.do")
    public @ResponseBody
    String duplicateBizCd(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  EgovMap map = parseUtil.jsonToEgovMap(searchKey);
    	  int count = coBas001Service.duplicateBizCd(map);
          return count+"";
    }
     /**사업년도조회*/
	@RequestMapping(value="/fms/co/bas/001/getBusinessYear.do", produces="application/json;charset=UTF-8")
      public @ResponseBody
    String getBusinessYear(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas001Service.getBusinessYear(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
    }
    /**관리지역 검색 셀렉트 박스 생성*/
	@RequestMapping(value="/fms/co/bas/001/getSearchMngZonNm.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getSearchMngZonNm(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> searchMngZonNmList = coBas001Service.getSearchMngZonNmSelectBox(map);
	   	 String searchMngZonNm = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchMngZonNmList), null, "@");
	   	 return searchMngZonNm;
   }
	
	 /**관리지역 검색 셀렉트 박스 생성 name/code*/
		@RequestMapping(value="/fms/co/bas/001/getSearchMngZonCd.do", produces="application/json;charset=UTF-8")
	     public @ResponseBody
        String getSearchMngZonCd(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
		   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
		   	 List<?> searchMngZonNmList = coBas001Service.getSearchMngZonNmSelectBoxCd(map);
		   	 String searchMngZonNm = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchMngZonNmList), null, "@");
		   	 return searchMngZonNm;
	   }
    /**관리지역조회*/
	@RequestMapping(value="/fms/co/bas/001/getManagementArea.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getManagementArea(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas001Service.getManagementArea(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
     }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/001/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas001Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
