package fms.util.com.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
public class SBexcelUtilController{

	@RequestMapping(value={"/saveSBexcel"}, method={RequestMethod.POST})
	public void excelExDownload(HttpServletRequest req, HttpServletResponse res) throws Exception{
		//boolean bIsDebugMode = false;							//디버그모드 설정 세팅
		//String strRequestEncodingType = "UTF-8";				//request 인코딩 설정 세팅
		//boolean bIsUseSXSSF = true;								//POI Library 버전이 3.8-beta3 이상이면 true로 설정할 것		
	}
	private String getBrowser(HttpServletRequest request){
		String header = request.getHeader("User-Agent");
		if(header.contains("MSIE")){
			return "MSIE";
		}else if(header.contains("Chrome")){
			return "Chrome";
		}else if(header.contains("Opeara")){
			return "Opera";
		}else if(header.contains("Firefox")){
			return "Firefox";
		}else if(header.contains("Safari")){
			return "Safari";
		}else{
			return "MSIE";
		}
	}
}
