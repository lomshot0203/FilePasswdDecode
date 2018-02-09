package fms.co.pop.controller;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.pop.service.CoPop003Service;
import fms.util.ParseUtil;
import fms.util.ViewUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Class Name : CoPop003Controller.java
 * @Description : CoPop003Controller class
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
public class CoPop003Controller {

	@Autowired
	private CoPop003Service coPop003Service;
    @Autowired
    private ParseUtil parseUtil;

    private static final Logger log = LoggerFactory.getLogger(CoPop003Controller.class);


    /**
     * 페이지 조회
     * @param req
     * @param res
     * @param params
     * @return
     * @throws Exception
     */
     @RequestMapping(value="/fms/co/pop/CoPop003.do")
     public String setPage(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/pop/CoPop003";
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
	@RequestMapping(value="/fms/co/pop/CoPop003AddrList.do")
      public void CoBas001AddrList(HttpServletRequest request, HttpServletResponse response) throws Exception {
    	/* 파라미터 */
    	EgovMap map = parseUtil.jsonToEgovMap(request.getParameter("searchKey"));

    	/* 결과 리스트 반환*/
		List list = coPop003Service.coPop003AddrList(map);
		String gson = parseUtil.listToJson(list);
		ViewUtil.jsonSuccess(response, gson);
      }

    /**
     * 주소검색 API 페이지 조회
     * @param req
     * @param res
     * @param params
     * @return
     * @throws Exception
     */
     @RequestMapping(value="/fms/co/pop/CoPop009.do")
     public String setPageCoPop009(HttpServletRequest request, HttpServletResponse response) throws Exception {
         return "fms/co/pop/CoPop009";
     }
}
