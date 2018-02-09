package fms.util.com.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.util.CmmUtil;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import fms.util.com.service.UtilComService;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Class Name : UtilComController
 * @Description : UtilCom Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.21
 * @version 1.0
 * @see
 *  
 */

@Controller
public class UtilComController {

	@Autowired
    private UtilComService utilComService;
	
    @Autowired
	private ParseUtil parseUtil;

    /**
     * 리스트 조회
     * @param req
     * @param res
     * @return jsonString
     * @throws Exception
     */     
	@RequestMapping(value="/fms/util/com/getCodeList.do")
     public void UtilComGetCodeList(HttpServletRequest request, HttpServletResponse response) throws Exception {
   	  EgovMap map = new EgovMap();
   	  map = CmmUtil.getRequestJsonToEgovMap(request);
   	  ObjectMapper om = new ObjectMapper();
   	
   	  List list = utilComService.utilComGetCodeList(map);
   	  String jsonString = parseUtil.listToJson(list);
   	  //String jsonString = om.writeValueAsString(list); // sb 그리드외
      //String jsonString = CmmUtil.getJsonMakeConvertforSBGrid(list); // sb 그리드용
      ViewUtil.jsonSuccess(response, jsonString);
     }
}
