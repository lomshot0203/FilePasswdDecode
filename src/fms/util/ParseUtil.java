package fms.util;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.*;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 작성자:민광진
 * 날짜:2015.04.27
 * 소개:컨트롤러에서 각종 데이터 파싱을 도우는 콤포넌트
  */
@Component
public class ParseUtil {
	/**List 객체를 Json으로 파싱*/
	public String listToJson(List<?> list){
		Gson gson = new GsonBuilder().setDateFormat("yyyyMMdd").create();
		String json = gson.toJson(list,List.class);
		return json;
	}
	/**Json 형 String 객체를 EgovMap으로 파싱*/
	public EgovMap jsonToEgovMap(String json) throws ParseException{
		Gson gson = new Gson();
		EgovMap egovMap = new EgovMap();
		json = convertUnicode(json);
		@SuppressWarnings("serial")
		Map<String, String> map = gson.fromJson(json, new TypeToken<Map<String,String>>() {}.getType());
		egovMap.putAll(map);
		return egovMap;
	}
	/**Json 형 String 객체를 Map으로 파싱*/
	@SuppressWarnings("serial")
	public Map<String, Object> jsonToMap(String json) throws ParseException{
		Gson gson = new Gson();
		json = convertUnicode(json);
		Map<String, Object> map = new HashMap<String, Object>();
		map = gson.fromJson(json, new TypeToken<Map<String,String>>() {}.getType());
		return map;
	}
	/**Json 형 String 객체를 List<Map> 으로 파싱*/
	public List<?> jsonToListMap(String json) throws ParseException{
		Gson gson = new Gson();
		@SuppressWarnings("unused")
		EgovMap egovMap = new EgovMap();
		json = convertUnicode(json);
		JSONArray jsonArray = jsonToJsonArray(json);
		List<Map<String, String>> list = new ArrayList<Map<String,String>>();
		for(int i=0; i<jsonArray.size(); i++){
			@SuppressWarnings("serial")
			Map<String, String> map = gson.fromJson(jsonArray.get(i).toString(), new TypeToken<Map<String,String>>() {}.getType());
			list.add(map);
		}
		return list;
	}
	/**Map 객체를 Json으로 파싱*/
	public  String mapToJson(Map<?, ?> map) {
		Gson gson = new Gson();
		String json = gson.toJson(map,Map.class);
		return json;
	}
	/**parameterMap 객체를 EgovMap으로 파싱*/
	public EgovMap parameterMapToEgovMap(Map<?, ?> map) {
		EgovMap egovMap = new EgovMap();
		for (@SuppressWarnings("rawtypes") Map.Entry entry : map.entrySet()) {
			String key = (String) entry.getKey();
			String[] value = (String[]) entry.getValue();
			String valueString = "";
			if (value.length > 1) {
				for (int i = 0; i < value.length; i++) {
					valueString += value[i] + " ";
				}
			} else {
				valueString = value[0];
			}
			egovMap.put(key, valueString);
		}
		return  egovMap;
	}
	/**Json 형 String 객체를 JsonArray로 파싱*/
	public JSONArray jsonToJsonArray(String json) throws ParseException {
		JSONParser jsonParser = new JSONParser();
		JSONArray jsonArray = (JSONArray) jsonParser.parse(json);
		return jsonArray;
	}
	/***/
	public String convertUnicode(String code){
		String preCode = code.replace("&quot;", "\"");
		return preCode;
	}
	/**Json 형 String 객체를 HashMap으로 파싱*/
	public HashMap<String, Object> jsonStringToHashMap(String jsonString) throws Exception{
		HashMap<String, Object> map = new HashMap<String, Object>();
		ObjectMapper om = new ObjectMapper();
		Map<String, Object> m = om.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
		map.putAll(m);
		return map;
	}
	/**Json 특정 param값을 구분자@ 로 selectBox용 key value로 파싱
	 * @throws Exception */
	public String jsonToSelectBox(String target, String param, String delimiter) throws Exception {
		if(param!=null){
			List<?> list = jsonToListMap(target);
			List<EgovMap> resultList = new ArrayList<EgovMap>();
			for(int i = 0; i < list.size(); i++){
				@SuppressWarnings("unchecked")
				Map<String, String> map = (Map<String, String>) list.get(i);
				String parameter = map.get(param);
				if(parameter.replace(delimiter, "").length()>0){
					String[] token = parameter.split(delimiter);
					String label = token[0];
					String value = token[1];
					boolean overlap = false;
					for(int j = 0; j < resultList.size(); j++){
						if(resultList.get(j).get("label").equals(label)){
							overlap = true;
						};
					}
					if(overlap==false){
						EgovMap resultMap = new EgovMap();
						resultMap.put("label", label);
						resultMap.put("value", value);
						resultList.add(resultMap);
					}
				}
			}
			String json = listToJson(resultList);
			return json;
		}else{
			List<EgovMap> resultList = new ArrayList<EgovMap>();
			JSONArray jsonArray = jsonToJsonArray(target);
			for(int i=0; i<jsonArray.size(); i++){
				EgovMap egovMap = jsonToEgovMap(jsonArray.get(i).toString());
				for (Object key : egovMap.keySet()){
					 String values = (String) egovMap.get(key);
					 if(values.replace(delimiter, "").length()>0){
						 String[] token = values.split(delimiter);
						 String label = token[0];
						 String value = token[1];
						 EgovMap resultMap = new EgovMap();
						 resultMap.put("label", label);
						 resultMap.put("value", value);
						 resultList.add(resultMap);
					 }
				}
			}
			String json = listToJson(resultList);
			return json;
		}
	}
	/**txtFile을 String으로 파싱*/
	public String localTxtFileToString(MultipartHttpServletRequest request) throws Exception {
		String string = "";
		Iterator<String> itr = request.getFileNames();
		while(itr.hasNext()){
			String uploadfilename = itr.next();
			MultipartFile mfile = request.getFile(uploadfilename);
			String filefullname = mfile.getOriginalFilename();
			String filename = filefullname.substring(0,filefullname.indexOf("."));
			String fileext = filefullname.substring(filefullname.indexOf(".")+1);
			String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
			if(filefullname!=null && !filefullname.equals("")){
				File file = new File(filepath + filename + fileext);
				int cnt = 0;
				while (file.exists()) {
					file = new File(filepath + filename + "("+cnt+")" + fileext);
					cnt++;
				}
				mfile.transferTo(file);
				FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(fileReader);
				StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				string=stringBuffer.toString();
				bufferedReader.close();
				fileReader.close();
				file.delete();
			}
		}
		return string;
    }
	/**excelFile을 List<Map>으로 파싱*/
	public List<Map<Object, Object>> localExcelToListMap2(MultipartHttpServletRequest request,int caption) throws BiffException, IOException{
		List<Map<Object, Object>> list = null;
		Iterator<String> itr = request.getFileNames();
		while(itr.hasNext()){
			String uploadfilename = itr.next();
			MultipartFile mfile = request.getFile(uploadfilename);
			String filefullname = mfile.getOriginalFilename();
			String filename = filefullname.substring(0,filefullname.indexOf("."));
			String fileext = filefullname.substring(filefullname.indexOf(".")+1);
			String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
			if(filefullname!=null && !filefullname.equals("")){
				File file = new File(filepath + filename + fileext);
				int cnt = 0;
				while (file.exists()) {
					file = new File(filepath + filename + "("+cnt+")" + fileext);
					cnt++;
				}
				mfile.transferTo(file);
				Workbook workbook = Workbook.getWorkbook(file);
				Sheet sheet = workbook.getSheet(0);
				int rowcount = sheet.getRows();
				int colcount = sheet.getColumns();
				list = new ArrayList<Map<Object,Object>>();
				List<Object> key = new ArrayList<Object>();
				for(int col = 0 ; col < colcount ; col++){
					if(caption>=0){
						key.add(sheet.getCell(col, caption).getContents());
					}else{
						key.add(col+"");
					}
				}
				for(int row = (caption+1>0?caption+1:0) ; row < rowcount ; row++){
					Map<Object, Object> map = new HashMap<Object, Object>();
					for(int col = 0; col < colcount; col++){
							map.put(key.get(col), sheet.getCell(col, row).getContents());
					}
					list.add(map);
				}
				workbook.close();
				file.delete();
			}
		}
		return list;
	}
	/**excelFile을 List<Map>으로 파싱*/
	public List<Map<Object, Object>> localExcelToListMap(MultipartHttpServletRequest request,int caption) throws BiffException, IOException{
		List<Map<Object, Object>> list = null;
		Iterator<String> itr = request.getFileNames();
		while(itr.hasNext()){
			String uploadfilename = itr.next();
			MultipartFile mfile = request.getFile(uploadfilename);
			String filefullname = mfile.getOriginalFilename();
			String filename = filefullname.substring(0,filefullname.indexOf("."));
			String fileext = filefullname.substring(filefullname.indexOf(".")+1);
			String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
			if(filefullname!=null && !filefullname.equals("")){
				File file = new File(filepath + filename + fileext);
				int cnt = 0;
				while (file.exists()) {
					file = new File(filepath + filename + "("+cnt+")" + fileext);
					cnt++;
				}
				mfile.transferTo(file);
				list = new ArrayList<Map<Object,Object>>();
				List<Object> key = new ArrayList<Object>();
				if("xlsx".equals(fileext)){
					XSSFWorkbook work = new XSSFWorkbook(new FileInputStream(file));
					int sheetcount = work.getNumberOfSheets();
					for( int sheetnum = 0; sheetnum < sheetcount; sheetnum++){
						XSSFSheet sheet = work.getSheetAt(sheetnum);
						int rowcount = sheet.getPhysicalNumberOfRows();
						for( int rownum = 0; rownum < rowcount; rownum++){
							Map<Object, Object> map = new HashMap<Object, Object>();
							XSSFRow row = sheet.getRow(rownum);
					    	int cellcount = row.getPhysicalNumberOfCells();
					    	for(int cellnum =0; cellnum < cellcount; cellnum++){
					    		XSSFCell cell = row.getCell(cellnum);
						          switch (cell.getCellType()) {
						          case XSSFCell.CELL_TYPE_NUMERIC:  
							              if (HSSFDateUtil.isCellDateFormatted(cell))  
							              {  
							                 if(caption<0){
							                	 map.put(cellnum+"", dateToString(cell.getDateCellValue(), "yyyy-MM-dd"));
							                 }else if(caption==rownum){
							                	 key.add(dateToString(cell.getDateCellValue(), "yyyy-MM-dd")+"");
							                 }else if(caption<rownum){
							                	 map.put(key.get(cellnum), dateToString(cell.getDateCellValue(), "yyyy-MM-dd"));
							                 }
							              }  
							              else 
							              {  
							                 if(caption<0){
							                	 map.put(cellnum+"", String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue())));
							                 }else if(caption==rownum){
							                	 key.add(String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue()))+"");
							                 }else if(caption<rownum){
							                	 map.put(key.get(cellnum), String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue())));
							                 }
							              }  
							              break;  
						          case XSSFCell.CELL_TYPE_FORMULA:
						                 if(caption<0){
						                	 map.put(cellnum+"", cell.getCellFormula());
						                 }else if(caption==rownum){
						                	 key.add(cell.getCellFormula()+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getCellFormula());
						                 }
						                 break;
						          case XSSFCell.CELL_TYPE_STRING:
						                 if(caption<0){
						                	 map.put(cellnum+"", cell.getRichStringCellValue().getString());
						                 }else if(caption==rownum){
						                	 key.add(cell.getRichStringCellValue().getString()+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getRichStringCellValue().getString());
						                 }         
						                 break;
						          case XSSFCell.CELL_TYPE_BLANK:
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", String.valueOf(cell.getBooleanCellValue()));
						                 }else if(caption==rownum){
						                	 key.add(String.valueOf(cell.getBooleanCellValue())+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), String.valueOf(cell.getBooleanCellValue()));
						                 }     
						                 break;
						          case XSSFCell.CELL_TYPE_ERROR :
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", String.valueOf(cell.getErrorCellValue()));
						                 }else if(caption==rownum){
						                	 key.add(String.valueOf(cell.getErrorCellValue())+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), String.valueOf(cell.getErrorCellValue()));
						                 }     
						                 break;
						          default:
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", cell.getRichStringCellValue().toString());
						                 }else if(caption==rownum){
						                	 key.add(cell.getRichStringCellValue().toString()+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getRichStringCellValue().toString());
						                 }     
						                 break;
						          }
					    	}
						    if(caption<rownum){
					    		list.add(map);
					    	}
						}
					}
				}else{
					HSSFWorkbook work = new HSSFWorkbook(new FileInputStream(file));
					int sheetcount = work.getNumberOfSheets();
					for( int sheetnum = 0; sheetnum < sheetcount; sheetnum++){
						HSSFSheet sheet = work.getSheetAt(sheetnum);
						int rowcount = sheet.getPhysicalNumberOfRows();
						for( int rownum = 0; rownum < rowcount; rownum++){
							Map<Object, Object> map = new HashMap<Object, Object>();
							HSSFRow row = sheet.getRow(rownum);
						    	int cellcount = row.getPhysicalNumberOfCells();
						    	for(int cellnum =0; cellnum < cellcount; cellnum++){
					    		HSSFCell cell = row.getCell(cellnum);
						          switch (cell.getCellType()) {
						          case HSSFCell.CELL_TYPE_NUMERIC:  
							              if (HSSFDateUtil.isCellDateFormatted(cell))  
							              {  
							                 if(caption<0){
							                	 map.put(cellnum+"", dateToString(cell.getDateCellValue(), "yyyy-MM-dd"));
							                 }else if(caption==rownum){
							                	 key.add(dateToString(cell.getDateCellValue(), "yyyy-MM-dd")+"");
							                 }else if(caption<rownum){
							                	 map.put(key.get(cellnum), dateToString(cell.getDateCellValue(), "yyyy-MM-dd"));
							                 }
							              }  
							              else 
							              {  
							                 if(caption<0){
							                	 map.put(cellnum+"", String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue())));
							                 }else if(caption==rownum){
							                	 key.add(String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue()))+"");
							                 }else if(caption<rownum){
							                	 map.put(key.get(cellnum), String.valueOf(new DecimalFormat("###").format(cell.getNumericCellValue())));
							                 }
							              }  
							              break;  
						          case HSSFCell.CELL_TYPE_FORMULA:
						                 if(caption<0){
						                	 map.put(cellnum+"", cell.getCellFormula());
						                 }else if(caption==rownum){
						                	 key.add(cell.getCellFormula()+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getCellFormula());
						                 }
						                 break;
						          case HSSFCell.CELL_TYPE_STRING:
						                 if(caption<0){
						                	 map.put(cellnum+"", cell.getRichStringCellValue().getString());
						                 }else if(caption==rownum){
						                	 key.add(cell.getRichStringCellValue().getString()+""); 
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getRichStringCellValue().getString());
						                 }         
						                 break;
						          case HSSFCell.CELL_TYPE_BLANK:
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", String.valueOf(cell.getBooleanCellValue()));
						                 }else if(caption==rownum){
						                	 key.add(String.valueOf(cell.getBooleanCellValue())+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), String.valueOf(cell.getBooleanCellValue()));
						                 }     
						                 break;
						          case HSSFCell.CELL_TYPE_ERROR :
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", String.valueOf(cell.getErrorCellValue()));
						                 }else if(caption==rownum){
						                	 key.add(String.valueOf(cell.getErrorCellValue())+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), String.valueOf(cell.getErrorCellValue()));
						                 }     
						                 break;
						          default:
						        	  	 if(caption<0){
						        	  		 map.put(cellnum+"", cell.getRichStringCellValue().toString());
						                 }else if(caption==rownum){
						                	 key.add(cell.getRichStringCellValue().toString()+"");
						                 }else if(caption<rownum){
						                	 map.put(key.get(cellnum), cell.getRichStringCellValue().toString());
						                 }     
						                 break;
						          }
					    	}
						    if(caption<rownum){
					    		list.add(map);
					    	}
						}
					}
				}
				file.delete();
			}
		}
		return list;
	}
	/**List<Map>을 SBgridJsonRef List<Map>으로 파싱*/
	public List<Map<Object, Object>> listToSBRef(List<Map<Object, Object>> list, String mapping, String defaults) throws BiffException, IOException{
		List<Map<Object, Object>> ref = new ArrayList<Map<Object,Object>>();
		String[] mSplit = mapping.split("⌒");
		for(int i=0; i<list.size(); i++){
			Map<Object, Object> map = new HashMap<Object, Object>();
			if(defaults!=null){
				String[] dSplit = defaults.split("⌒");
				for(int j=0; j<dSplit.length; j++){
					map.put(dSplit[j].split("〓")[0], dSplit[j].split("〓")[1]);
				}
			}
			for(int j=0; j<mSplit.length; j++){
				String key = mSplit[j].split("⇔")[1]!=null?mSplit[j].split("⇔")[1]:j+"";
				map.put(mSplit[j].split("⇔")[0], list.get(i).get(key));
			}
			ref.add(map);
		}
		return ref;
	}
	/**Date를 String으로 파싱*/
	public String dateToString(Date date, String format){
		String result ="";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			result = sdf.format(date);
		} catch (Exception e) {
		}
		return result;
	}
	
	
	/**txtFile을 String으로 파싱 euc-kr*/
	public String localTxtFileToStringEucKr(MultipartHttpServletRequest request) throws Exception {
		String string = "";
		Iterator<String> itr = request.getFileNames();
		while(itr.hasNext()){
			String uploadfilename = itr.next();
			MultipartFile mfile = request.getFile(uploadfilename);
			String filefullname = mfile.getOriginalFilename();
			String filename = filefullname.substring(0,filefullname.indexOf("."));
			String fileext = filefullname.substring(filefullname.indexOf(".")+1);
			String filepath = System.getProperty("java.io.tmpdir") + System.getProperty("file.separator");
			if(filefullname!=null && !filefullname.equals("")){
				File file = new File(filepath + filename + fileext);
				int cnt = 0;
				while (file.exists()) {
					file = new File(filepath + filename + "("+cnt+")" + fileext);
					cnt++;
				}
				mfile.transferTo(file);
				//FileReader fileReader = new FileReader(file);
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(file),"euc-kr"));
				
				StringBuffer stringBuffer = new StringBuffer();
				String line;
				while ((line = bufferedReader.readLine()) != null) {
					stringBuffer.append(line);
					stringBuffer.append("\n");
				}
				string=stringBuffer.toString();
				bufferedReader.close();
				file.delete();
			}
		}
		return string;
    }
}
