package fms.util;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

/**
 * SBGrid Servlet에서 공통적으로 사용할 기능을 구현한 추상클래스
 * 
 * @author 정원채
 * @since SoftBowl SbGrid 0.0.1
 */
public abstract class SoftBowlServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	public static final boolean ISCONTENTTYPE = true;
	
	public static final int CANT_MAKES_REQUEST_DATA = 0;
	public static final int CANT_MAKES_RESPONSE = 1;
	
	/**
	 * 요청중 strKey 의 키값을 갖는 파라미터를 구한다.
	 * @param Request
	 * @param strKey
	 * @return
	 */
	public String getParameter (HttpServletRequest Request, String strKey){
		if (null == Request.getCharacterEncoding()){
			try{
				Request.setCharacterEncoding("UTF-8");
			}catch (UnsupportedEncodingException e){
				e.printStackTrace();
			}
		}
		return Request.getParameter(strKey);
	}
	
	/**
	 * 요청 DATA를 STRING으로 구한다.
	 * @param Request
	 * @param Response
	 * @return StringBuffer sbRequest
	 */
	public String getStringData (HttpServletRequest Request, HttpServletResponse Response){
		StringBuffer sbRequest = new StringBuffer();
		try{
			byte[] bt = new byte[1024];
			for(int i; (i = Request.getInputStream().read(bt)) != -1;){
				sbRequest.append(new String(bt, 0, i, "UTF-8"));
			}
		}
		catch (IOException e){
			try{
				Response.sendError(CANT_MAKES_REQUEST_DATA, e.getMessage());
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}
		return sbRequest.toString();
	}
	/**
	 * 요청 Data를 Object로 구한다.
	 * @param Request
	 * @param Response
	 * @return
	 */
	public Object getObjectData (HttpServletRequest Request, HttpServletResponse Response){
		
		ObjectInputStream objInputStream = null;
		Object objValue = null;
		try{
			objInputStream = new ObjectInputStream(new BufferedInputStream(Request.getInputStream()));
			try{
				objValue = objInputStream.readObject();
			}
			catch (ClassNotFoundException e){
				e.printStackTrace();
				try{
					Response.sendError(CANT_MAKES_REQUEST_DATA, e.getMessage());
				}
				catch (IOException ex){
					ex.printStackTrace();
				}
			}
		}
		catch (IOException e){
			e.printStackTrace();
			try{
				Response.sendError(CANT_MAKES_REQUEST_DATA, e.getMessage());
			}
			catch (IOException ex){
				ex.printStackTrace();
			}
		}
		finally{
			if (null != objInputStream){
				try{
					objInputStream.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}
		return objValue;
	}
	
	/**
	 * 해당 Context 의 Root Path 를 구한다.
	 */
	public String getRootPath (){
		return this.getServletContext().getRealPath("/").replace(File.separator, "/");
	}
	
	/**
	 * HttpServletResponse 에 String 결과를 전송한다.
	 * @param objResponse
	 * @param strResponse
	 */
	public void sendResult (HttpServletResponse Response, String strRes){
		if (ISCONTENTTYPE){
			Response.setCharacterEncoding("utf-8");
		}
		Response.setContentType("text/xml");
		PrintWriter objWriter = null;
		try{
			objWriter = Response.getWriter();
			objWriter.print(strRes);
		}catch (IOException e){
			e.printStackTrace();
			try{
				Response.sendError(CANT_MAKES_RESPONSE, e.getMessage());
			}catch (IOException ex){
				ex.printStackTrace();
			}
		}finally{
			if (null != objWriter){
				objWriter.flush();
				objWriter.close();
			}
		}
	}
	
	/**
	 * HttpServletResponse 에 String 결과를 전송한다.
	 * @param response
	 * @param strRes
	 */
	public void sendResult (HttpServletResponse response, String strRes, String strContentsType){
		if(ISCONTENTTYPE){
			response.setCharacterEncoding("utf-8");
		}
		response.setContentType(strContentsType);
		PrintWriter objWriter = null;
		try{
			objWriter = response.getWriter();
			objWriter.print(strRes);
		} 
		catch (IOException e){
			e.printStackTrace();
			try{
				response.sendError(CANT_MAKES_RESPONSE, e.getMessage());
			} 
			catch (IOException ex){
				ex.printStackTrace();
			}
		}
		finally{
			if (null != objWriter){
				objWriter.flush();
				objWriter.close();
			}
		}
	}
	
	/**
	 * HttpServletResponse 에 Object 결과를 전송한다.
	 * @param response
	 * @param objResponseData
	 */
	public void sendResult (HttpServletResponse response, Object objResponseData){
		ObjectOutputStream objOutputStream = null;
		try{
			objOutputStream = new ObjectOutputStream(new BufferedOutputStream(response.getOutputStream()));
			if (null != objResponseData){
				objOutputStream.writeObject(objResponseData);
			}
		}catch (Exception e){
			e.printStackTrace();
		}finally{
			if (null != objOutputStream){
				try{
					objOutputStream.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * HttpServletResponse 에 byte arry 결과를 전송한다.
	 * @param response
	 * @param strResponse
	 */
	public void sendResult (HttpServletResponse response, byte[] arResponseData){
		response.setContentType("application/octet-stream; charset=utf-8");
		BufferedOutputStream objOutputStream = null;
		try{
			objOutputStream = new BufferedOutputStream(response.getOutputStream());
			objOutputStream.write(arResponseData);
		}catch (IOException e){
			e.printStackTrace();
		}finally{
			if (null != objOutputStream){
				try{
					objOutputStream.close();
				}
				catch (IOException e){
					e.printStackTrace();
				}
			}
		}
	}
	
	protected void redirect(HttpServletResponse response, String strURL) throws IOException{
		String urlWithSessionID = response.encodeRedirectURL(strURL);
		response.sendRedirect(urlWithSessionID);
	}
	
	protected void forward(HttpServletRequest request, HttpServletResponse objResponse, String strURL) throws ServletException, IOException {
		RequestDispatcher dispatcher = request.getRequestDispatcher(strURL);
		dispatcher.forward(request, objResponse);
	}

}
