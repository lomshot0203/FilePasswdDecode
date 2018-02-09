package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas004Service;
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
 * 날짜:2015.05.13
 */
@Controller
public class CoBas004Controller {
	@Autowired
	private CoBas004Service coBas004Service;
	@Autowired
	private ParseUtil parseUtil;
	@Autowired
	private customizeCodeService customizeCodeService;
    /**페이지조회*/
	@RequestMapping(value="/fms/co/bas/CoBas004.do")
	public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		List<?> commDivList = customizeCodeService.getCodePair("CTR008", null); //개인단체
		String commDivCode = parseUtil.listToJson(commDivList);
		model.addAttribute("commDivCode", commDivCode);
		List<?> ntrList = customizeCodeService.getCodePair("COM014", null); //성별
	   	String ntrCode = parseUtil.listToJson(ntrList);
	   	model.addAttribute("ntrCode", ntrCode);

	   	List<?> chgDivCdList = customizeCodeService.getCodePair("CTR009", null); //변경사유
	   	String chgDivCdCode = parseUtil.listToJson(chgDivCdList);
	   	model.addAttribute("chgDivCdCode", chgDivCdCode);

	   	return "fms/co/bas/CoBas004";
	 }
	 /**주민번호 복호화*/
	@RequestMapping(value="/fms/co/bas/004/getDecRRN.do", produces="text/plain;charset=UTF-8")
      public @ResponseBody
    String getDecRRN(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 String encRRN = request.getParameter("encRRN");
	   	 String decRRN = coBas004Service.getDecRRN(encRRN);
	   	 return decRRN;
    }
     /**고객정보조회*/
	@RequestMapping(value="/fms/co/bas/004/getCustomerInformation.do", produces="application/json;charset=UTF-8")
      public @ResponseBody
    String getCustomerInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas004Service.getCustomerInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
    }
    /**고객상세조회*/
	@RequestMapping(value="/fms/co/bas/004/getMoreInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getMoreInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas004Service.getMoreInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
    /**고객변경조회*/
	@RequestMapping(value="/fms/co/bas/004/getChangeInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getChangeInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas004Service.getChangeInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/004/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas004Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}

	/**계약번호를 이용하여 계약구분을 조회*/
	@RequestMapping(value="/fms/co/bas/004/getCtrGb.do", produces="text/plain;charset=UTF-8")
      public @ResponseBody
    String getCtrGb(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 String ctrGb = request.getParameter("ctrGb");
	   	 String decRRN = coBas004Service.getCtrGb(ctrGb);
	   	 return decRRN;
    }

	/**고객 계좌정보 조회*/
	@RequestMapping(value="/fms/co/bas/004/getBanAcnInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getBanAcnInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas004Service.getBanAcnInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }

	/**담당자 정보 조회*/
	@RequestMapping(value="/fms/co/bas/004/getCgpInformation.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getCgpInformation(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = parseUtil.jsonToEgovMap(searchKey);
	   	 List<?> list = coBas004Service.getCgpInformation(map);
	   	 String json = parseUtil.listToJson(list);
	   	 return json;
   }

	/**구분(개인/단체)로 주민번호 복호화*/
	@RequestMapping(value="/fms/co/bas/004/getDecRsdtNo.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getDecRsdtNo(HttpServletRequest request, HttpServletResponse response) throws Exception {
	   	 EgovMap map = new EgovMap();
	   	 map.put("crtpyRsdtNo", request.getParameter("crtpyRsdtNo"));
	   	 map.put("commDiv", request.getParameter("commDiv"));
	   	 String rtnValue = coBas004Service.getDecRsdtNo(map);
	   	 return rtnValue;
   }

}
