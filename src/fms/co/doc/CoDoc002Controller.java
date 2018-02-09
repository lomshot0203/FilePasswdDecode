package fms.co.doc;

import fms.co.sys.service.CoSys013Service;
import fms.util.ParseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 작성자:c
 * 날짜:2015.07.15
 */
@Controller
public class CoDoc002Controller {
	@Autowired
    protected CoSys013Service coSys013Service;
	
	@Autowired
	private ParseUtil parseUtil;
	
	/**페이지조회*/
	@RequestMapping(value="/fms/co/doc/CoDoc002.do")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		return "fms/co/doc/CoDoc002";
	}
	 

}
