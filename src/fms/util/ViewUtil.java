package fms.util;

import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

/**
 * 화면에 생성할 view 
 * @author ypstore
 *
 */


public class ViewUtil {

	
	public static void ajaxSuccess(HttpServletResponse response, String mimeType, String data) throws Exception{
		
		response.setContentType(mimeType);
		PrintWriter writer=response.getWriter();
		writer.print(data);
		writer.flush();
		writer.close();
	}
	
	/**
	 * JSON으로 데이터 리턴
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	public static void jsonSuccess(HttpServletResponse response, String data) throws Exception{
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer=response.getWriter();
		writer.print(data);
		writer.flush();
		writer.close();
	}
	
	/**
	 * JSON으로 데이터 리턴
	 * 리턴형식 {result : 'SAVE'}
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	public static void jsonResult(HttpServletResponse response, String data) throws Exception{
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer=response.getWriter();
		writer.print("{ \"result\" : \""+data+"\"}");
		writer.flush();
		writer.close();
	}
	
	/**
	 * JSON으로 데이터 리턴(int 형데이터)
	 * 리턴형식 {result : 1}
	 * @param response
	 * @param data
	 * @throws Exception
	 */
	public static void jsonResult(HttpServletResponse response, int data) throws Exception{
		
		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		PrintWriter writer=response.getWriter();
		writer.print("{ \"result\" : \""+data+"\"}");
		writer.flush();
		writer.close();
	}
	
}
