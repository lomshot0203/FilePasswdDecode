package fms.co.sys.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.sys.service.CoSys013Service;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Class Name : CoSys013Controller.java
 * @Description : CoSys013Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.07.13
 * @version 1.0
 * @see
 *  
 *  Copyright (C)  All right reserved.
 */

@Controller
public class CoSys013Controller {
    @Autowired
    protected CoSys013Service coSys013Service;
	@Autowired
	private ParseUtil parseUtil;

	
    /**페이지조회*/
	 @RequestMapping(value="/fms/co/sys/CoSys013.do")
	 public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
	     return "fms/co/sys/CoSys013";
	 }
	 
 	/**
 	* 첨부파일 조회
 	* @param req
 	* @param res
 	* @param 
 	* @return
 	* @throws Exception
 	*/     
 	@SuppressWarnings("rawtypes")
 	@RequestMapping(value="/fms/co/sys/listCoSys013.do")
 	public void listCoSys013(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		  EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));
 		  EgovMap rtnMap = new EgovMap();
 		  List<?> list = coSys013Service.listCoSys013(map);
 		  rtnMap.put("dataList", list);
 		  ViewUtil.jsonSuccess(response, parseUtil.mapToJson(rtnMap));
 	}
 	
 	
 	
 	/**첨부파일 저장*/
 	@RequestMapping(value="/fms/co/sys/saveListCoSys013Attachfile.do")
 	public void saveListCoSys013Attachfile(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
 		EgovMap rtnMap = new EgovMap();
 		rtnMap = (EgovMap) coSys013Service.saveCoSys013(map);
 		//List<?> list = coSys013Service.listCoSys013(rtnMap);
 		//rtnMap.put("dataList", list);
 		rtnMap.put("errorMsg", "");
 		ViewUtil.jsonSuccess(response, parseUtil.mapToJson(rtnMap));
 	}
 	
 	/**수정*/
 	@RequestMapping(value="/fms/co/sys/updateCoSys013.do")
 	public void saveCoSys013(HttpServletRequest request, HttpServletResponse response) throws Exception {
 		EgovMap map = parseUtil.parameterMapToEgovMap(request.getParameterMap());
 		EgovMap rtnMap = new EgovMap();
 		rtnMap = (EgovMap) coSys013Service.updateCoSys013(map);
 		rtnMap.put("errorMsg", "");
 		ViewUtil.jsonSuccess(response, parseUtil.mapToJson(rtnMap));
 	}
}
