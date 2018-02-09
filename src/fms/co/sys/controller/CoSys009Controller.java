package fms.co.sys.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.sys.service.CoSys009Service;
import fms.util.CmmUtil;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


/**
 * @Class Name : CoSys009Controller.java
 * @Description : CoSys009 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.20
 * @version 1.0
 * @see
 *  
 */

@Controller
public class CoSys009Controller {

	@Autowired
    private CoSys009Service coSys009Service;
    
    @Autowired
	private ParseUtil parseUtil;
    /**
     * 페이지 조회
     * @param req
     * @param res
     * @param params
     * @return
     * @throws Exception
     */     
     @RequestMapping(value="/fms/co/sys/CoSys009.do")
     public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/sys/CoSys009";
     }
     
     /**
      * 셀렉트 박스 조회
      * @param request
      * @param response
      * @param map
      * @return
      * @throws Exception
      */     
     @SuppressWarnings("rawtypes")
     @RequestMapping(value="/fms/co/sys/CoSys009AddrSelectBoxList.do")
     public void CoBas001BusinessList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	  EgovMap map = new EgovMap();
    	  map = CmmUtil.getRequestJsonToEgovMap(request);
    	  ObjectMapper om = new ObjectMapper();
    	  List list = coSys009Service.coSys009AddrSelectBoxList(map);
          String jsonString = om.writeValueAsString(list); // 셀렉트박스용
          //String jsonString = CmmUtil.getJsonMakeConvertforSBGrid(list); // SB GRID용
          ViewUtil.jsonSuccess(response, jsonString);
     }

     /**
      * 주소 리스트 조회
      * @param request
      * @param response
      * @param map
      * @return
      * @throws Exception
      */     
     @SuppressWarnings("rawtypes")
     @RequestMapping(value="/fms/co/sys/CoSys009AddrList.do")
     public void CoSys009AddrList(HttpServletRequest request, HttpServletResponse response) throws Exception {
  		/* 파라미터 */
  		EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

  		/* 결과 리스트 반환*/
  		List<?> list = coSys009Service.coSys009AddrList(map);

  		/* 검색결과 리턴 */
  		String jsonString = parseUtil.listToJson(list);
  		ViewUtil.jsonSuccess(response, jsonString);
     }
     
     /**텍스트 업로드*/
 	@RequestMapping(value="/fms/co/sys/txtUp.do")
     public @ResponseBody
    String txtUpLoad(MultipartHttpServletRequest request, HttpServletResponse response) throws Exception {
 		System.out.println("txtUpLoad...");
 		String txt = parseUtil.localTxtFileToStringEucKr(request);
 		int cnt = coSys009Service.coSys009txtUp(txt);
		return cnt+"";
 		
     }
 	
 	
}
