package fms.co.pop.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.pop.service.CoPop001Service;
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
 * 날짜:2015.05.18
 */
@Controller
public class CoPop001Controller {
	@Autowired
	private CoPop001Service coPop001Service;
	@Autowired
	private ParseUtil parseUtil;
	@Autowired
	private customizeCodeService customizeCodeService;

    /**페이지조회*/
	 @RequestMapping(value="/fms/co/pop/CoPop001.do")
	 public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		 List<?> commDivList = customizeCodeService.getCodePair("CTR008", null); //개인단체코드 리스트
	   	String commDivCode = parseUtil.listToJson(commDivList);
	   	model.addAttribute("commDivCode", commDivCode);
	   	List<?> ntrList = customizeCodeService.getCodePair("COM014", null); //성별
	   	String ntrCode = parseUtil.listToJson(ntrList);
	   	model.addAttribute("ntrCode", ntrCode);
	   	List<?> chgDivCdList = customizeCodeService.getCodePair("CTR009", null); //변경사유
	   	String chgDivCdCode = parseUtil.listToJson(chgDivCdList);
	   	model.addAttribute("chgDivCdCode", chgDivCdCode);

       if(!request.getParameter("searchText").equals("") || request.getParameter("searchText") != null){
            model.addAttribute("searchText", request.getParameter("searchText"));
        }
       //고객번호 셋팅
       model.addAttribute("cusNo",request.getParameter("cusNo"));
       //단체구분 셋팅
       model.addAttribute("commDiv",request.getParameter("commDiv"));
       //초기 탭 번호 셋팅
       model.addAttribute("activTabNo",request.getParameter("activTabNo"));

	     return "fms/co/pop/CoPop001";
	 }
	 /**주민번호 복호화*/
	@RequestMapping(value="/fms/co/pop/001/getDecRRN.do", produces="text/plain;charset=UTF-8")
      public @ResponseBody
    String getDecRRN(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 String encRRN = request.getParameter("encRRN");
	   	 String decRRN = coPop001Service.getDecRRN(encRRN);
	   	 return decRRN;
    }
     /**고객정보조회*/
	@RequestMapping(value="/fms/co/pop/001/getCustomerInformation.do", produces="application/json;charset=UTF-8")
      public @ResponseBody
    String getCustomerInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coPop001Service.getCustomerInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
    }
    /**고객상세조회*/
	@RequestMapping(value="/fms/co/pop/001/getMoreInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getMoreInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coPop001Service.getMoreInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
    /**고객변경조회*/
	@RequestMapping(value="/fms/co/pop/001/getChangeInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getChangeInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coPop001Service.getChangeInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
	/**저장*/
	@RequestMapping(value="/fms/co/pop/001/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coPop001Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
