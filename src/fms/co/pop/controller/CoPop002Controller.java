package fms.co.pop.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.bas.service.CoBas001Service;
import fms.co.pop.service.CoPop002Service;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Class Name : CoPop002Controller.java
 * @Description : CoPop002Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.05.15
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */
@Controller
public class CoPop002Controller {
	
	@Autowired
    private CoBas001Service coBas001Service;
	
	@Autowired
    private CoPop002Service coPop002Service;
	
    @Autowired
    private ParseUtil parseUtil;
    
    private static final Logger log = LoggerFactory.getLogger(CoPop002Controller.class);
  
    
    /**
     * 페이지 조회
     * @param req
     * @param res
     * @param params
     * @return
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/pop/CoPop002.do")
     public String setPage(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
		//사업년도
		List<?> searchBizYyList = coBas001Service.getSearchBizYySelectBox();
		String searchBizYy = parseUtil.jsonToSelectBox(parseUtil.listToJson(searchBizYyList), null, "@");
		model.addAttribute("searchBizYyList", searchBizYy);
		if(!request.getParameter("searchText").equals("") || request.getParameter("searchText") != null){
		    model.addAttribute("searchText", request.getParameter("searchText"));
		}
         return "fms/co/pop/CoPop002";
     }
     

     /**
      * 리스트조회
      * @param req
      * @param res
      * @param 
      * @return
      * @throws Exception
      */     
    @SuppressWarnings("rawtypes")
	@RequestMapping(value="/fms/co/pop/CoPop002ctrAll01List.do")
      public void coPop002ctrAll01List(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	  EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));
    	  List<?> list = coPop002Service.coPop002ctrAll01List(map);
    	  String gson = parseUtil.listToJson(list);
          ViewUtil.jsonSuccess(response, gson);
      }
}
