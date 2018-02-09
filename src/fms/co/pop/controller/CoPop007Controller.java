package fms.co.pop.controller;

import fms.co.pop.service.CoPop001Service;
import fms.util.ParseUtil;
import fms.util.com.service.customizeCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 작성자:민광진
 * 날짜:2015.05.18
 */
@Controller
public class CoPop007Controller {
	@Autowired
	private CoPop001Service coPop001Service;
	@Autowired
	private ParseUtil parseUtil;
	@Autowired
	private customizeCodeService customizeCodeService;
	
	
    /**페이지조회*/
	 @RequestMapping(value="/fms/co/pop/CoPop007.do")
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
	   	
       if(!request.getParameter("searchText").equals("") || request.getParameter("searchText") != null){
            model.addAttribute("searchText", request.getParameter("searchText"));
        }
	     return "fms/co/pop/CoPop007";
	 }
}
