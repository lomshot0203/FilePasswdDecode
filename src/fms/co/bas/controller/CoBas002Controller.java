package fms.co.bas.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas002Service;
import fms.util.ParseUtil;
import fms.util.com.service.customizeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

/**
 * 작성자:민광진
 * 날짜:2015.05.04
 */
@Controller
public class CoBas002Controller {
	@Autowired
	private CoBas002Service coBas002Service;
	@Autowired
	private customizeCodeService customizeCodeService;
	@Autowired
	private ParseUtil parseUtil;
	/**페이지조회*/ 
     @RequestMapping(value="/fms/co/bas/CoBas002.do")
     public String list(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	 List<String> code = new  ArrayList<String>();
    	 code.add("02"); /*임대료 가상 모계좌*/ code.add("05"); /*전세지원금 입금 가상모계좌*/
    	 List<?> divList = customizeCodeService.getCodePair("CTR011",code);
    	 String searchDiv = parseUtil.listToJson(divList);
    	 model.addAttribute("searchDivList", searchDiv);
    	 List<?> bankPlusAccountList = coBas002Service.getBankPlusAccountSelectBox();
    	 String searchBankPlusAccountList = parseUtil.listToJson(bankPlusAccountList);
    	 String searchBankPlusAccount = parseUtil.jsonToSelectBox(searchBankPlusAccountList, "bankplusaccount", "@");
    	 model.addAttribute("searchBankPlusAccount", searchBankPlusAccount);
         return "fms/co/bas/CoBas002";
     }
     /**모계좌조회*/
 	@RequestMapping(value="/fms/co/bas/002/getMasterAccountList.do", produces="application/json;charset=UTF-8")
     public @ResponseBody
    String getMasterAccountList(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		EgovMap map = parseUtil.jsonToEgovMap(searchKey);
     	List<?> list = coBas002Service.getMasterAccountList(map);
        String json = parseUtil.listToJson(list);
        return json;
     }
    /**텍스트 업로드*/
	@RequestMapping(value="/fms/co/bas/002/txtUp.do")
    public @ResponseBody
    String txtUpLoad(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
		String string = parseUtil.localTxtFileToString(request);
		return string;
    }
    /**텍스트를 그리드로 매핑*/
	@RequestMapping(value="/fms/co/bas/002/txtToGrid.do", produces="application/json;charset=UTF-8")
    public @ResponseBody
    String txtToGrid(@RequestParam("txtData") String txt, @RequestParam("gridRef") String ref, HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap egovMap = parseUtil.jsonToEgovMap(ref);
		String vtaacnSeq = (String) egovMap.get("vtaacnSeq"); //가상모계좌 순번
		String pntacnCd = (String) egovMap.get("pntacnCd"); //가상모계좌 코드
		String bancd = coBas002Service.getBanCd(pntacnCd); //은행코드
		String div = (String) egovMap.get("div"); //구분
   	 	List<String> code = new  ArrayList<String>();
   	 	code.add(bancd); //은행코드
   	 	List<?> bancdList = customizeCodeService.getCodePair("CTR012",code); //은행코드
   	 	String codeNm = parseUtil.jsonToEgovMap((bancdList.get(0).toString())).get("codeNm").toString();
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		StringTokenizer stringTokenizer = new StringTokenizer(txt, "\n");
		while (stringTokenizer.hasMoreTokens()) {
			Map<String, String> map = new HashMap<String, String>();
			String account = stringTokenizer.nextToken(); 
			map.put("pntacnCd", pntacnCd);
			map.put("banCd", bancd); //가상계좌 상세 은행 코드
			map.put("acnDiv", div);
			map.put("banAcnNo", account);
			map.put("bankplusaccount", codeNm+" "+account);
			if(vtaacnSeq!="undefined")map.put("vtaacnSeq", vtaacnSeq);
			list.add(map);
		};
		String json = parseUtil.listToJson(list); 
		return json;
    }
 	/**가상계좌조회*/
 	@RequestMapping(value="/fms/co/bas/002/getVirtualAccountList.do", produces="application/json;charset=UTF-8")
    public @ResponseBody
    String getVirtualAccountList(@RequestParam("searchKey") String searchKey, HttpServletRequest request, HttpServletResponse response) throws Exception {
 		EgovMap map = parseUtil.jsonToEgovMap(searchKey);
 		List<?> list = coBas002Service.getVirtualAccountList(map);
        String json = parseUtil.listToJson(list);
        return json;
    }
	/**저장*/
	@RequestMapping(value="/fms/co/bas/002/save.do", produces="application/json;charset=UTF-8")
	public @ResponseBody
    String save(HttpServletRequest request, HttpServletResponse response) throws Exception {
		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
		List<?> list = coBas002Service.save(map);
		String json = parseUtil.listToJson(list);
		return json;
	}
}
