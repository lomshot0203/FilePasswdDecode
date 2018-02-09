package fms.co.pop.controller;

import fms.util.ParseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * @Class Name : CoPop004Controller.java
 * @Description : CoPop004Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.27
 * @version 1.0
 * @see
 *
 *  Copyright (C)  All right reserved.
 */
@Controller
public class CoPop008Controller {

//	@Autowired
//	private CoPop004Service coPop003Service;
    @Autowired
    private ParseUtil parseUtil;

    private static final Logger log = LoggerFactory.getLogger(CoPop008Controller.class);


    /**
     * 페이지 조회
     * @param req
     * @param res
     * @param params
     * @return
     * @throws Exception
     */
     @RequestMapping(value="/fms/co/pop/CoPop008.do")
     public String setPage(HttpServletRequest request, HttpServletResponse response, Model model) throws Exception {
    	 String fileId = request.getParameter("fileId");
    	 Map map = request.getParameterMap();
    	 model.addAttribute("fileId", fileId);
         return "fms/co/pop/CoPop008";
     }



}
