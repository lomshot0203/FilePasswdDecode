package fms.util.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
*작성자:민광진
*날짜:2015.05.13
 */
@Controller
public class DateUtilController {

	@RequestMapping(value="/getSysdate.do")
     public @ResponseBody String getSysdate(@RequestParam("format") String format, HttpServletRequest request, HttpServletResponse response) throws Exception {
		  Date date = new Date();	  
		  SimpleDateFormat dateFormat = new SimpleDateFormat(format);
		  String sysdate = dateFormat.format(date);

		  return sysdate;
     }
	
/**
 * 날짜 더하고 빼기
 *작성자:kjn
 *날짜:2015.08.06
*/	
	@RequestMapping(value="/getAddDate.do")
    public @ResponseBody String getAddDate(@RequestParam("format") String format, @RequestParam("gigan") String  gigan, HttpServletRequest request, HttpServletResponse response) throws Exception {
		 
		Calendar addDate = Calendar.getInstance(); 
		 int i = Integer.parseInt(gigan);
		 addDate.add(Calendar.DATE,  i); 		 
		 SimpleDateFormat dateFormat = new  SimpleDateFormat(format);
		 String date =  dateFormat.format(addDate.getTime());
		 
		  return date;
    }	
}
