package fms.util;

import egovframework.rte.psl.dataaccess.util.EgovMap;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


@SuppressWarnings("rawtypes")
public class CmmUtil {
/*
    public static List<Map<String, Object>> jsonList(String str) throws Exception {

        List<Map<String, Object>> list = null;

        Map<String, Object> map = null;
        String keyName = "";
        Iterator<?> itr = null;

        JSONArray array = (JSONArray) JSONValue.parse(str);

        list = new ArrayList<Map<String, Object>>();

        for (int i=0; i < array.size(); i++) {

            itr = ((JSONObject) array.get(i)).keySet().iterator();
            map = new HashMap<String, Object>();

            while(itr.hasNext()){
                keyName = (String) itr.next();

                map.put(keyName, ((JSONObject)array.get(i)).get(keyName));

            }
            list.add(map);
        }

        return list;
    }
*/

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
     * JSON String으로 변환
     * @param response
     * @param data
     * @throws Exception
     */
    public static String getJsonMakeConvertforSBGrid(Object dataList) throws Exception{
        ObjectMapper mapper=new ObjectMapper();
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        jsonMap.put("strGrid1", dataList);
        //jsonMap.put("combo_yn", arg1)
        String value=mapper.writeValueAsString(jsonMap);
        return value;
        //return getJsonMakeConvertforSBGrid(dataList,gridKey,false);
    }

    public static Object convertMapToObject(Map map, Object objClass) throws Exception {
        String keyAttribute = null;
        String setMethodString = "set";
        String methodString = null;
        if(map == null){
            return null;
        }

        Iterator itr = map.keySet().iterator();
        while(itr.hasNext()){
            keyAttribute = (String) itr.next();
            methodString = setMethodString+keyAttribute.substring(0,1).toUpperCase()+keyAttribute.substring(1);
            try {
                Method[] methods = objClass.getClass().getMethods();
                for(int i=0;i<=methods.length-1;i++){
                    if(methodString.equals(methods[i].getName())){
                        methods[i].invoke(objClass, map.get(keyAttribute));
                    }
                }
            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return objClass;
    }


    public static List<?> convertMapToList(List list, Object objClass) throws Exception {

        List result = new ArrayList();
        Map map = null;

        if(list == null){
            return null;
        }
        for(Object f_map : list){
            map = new HashMap();
            map = (Map) f_map;
            result.add(convertMapToObject(map, objClass.getClass().newInstance()));
        }

        return result;
    }

    /**
     * 스트링이 null일경우 공백으로 리턴해준다.
     * @param str
     * @return
     * @throws Exception
     */
    public static String checkNull(String str) throws Exception{
        return checkNull(str, "");
    }

    /**
     * 스트링 치환
     * @param str
     * @return
     * @throws Exception
     */
    public static String checkNull(Object str) throws Exception{
        if (str == null || "".equals(str) || str.equals("null") || str.toString().length() == 0){
            return "";
        }

        return checkNull(str.toString(), "");
    }

    /**
     * object형이 null이거나 할때리턴
     * @param str
     * @param converted
     * @return
     * @throws Exception
     */
    public static String checkNull(Object str, String converted) throws Exception{
        if (str == null || "".equals(str) || str.equals("null") || str.toString().length() == 0){
            return converted;
        }
        else{
            return str.toString();
        }
    }

    /**
     * 문자열이 공백값이 있는지 확인
     * @param string
     * @return
     */
    public static boolean isEmpty(String string){
        return string == null || string.trim().length() == 0;
    }


    /**
     * 문자열을 숫자로 바꾸어줍니다. null이거나 공백일시 0으로 바꾸어줍니다.
     *
     * @param src
     * @return
     */
    public static int zeroConvert(String src) throws Exception{
        if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
            return 0;
        }else{
            return Integer.parseInt(src.trim());
        }
    }

    /**
     * 문자열을 숫자(long)로 바꾸어줍니다. null이거나 공백일시 0으로 바꾸어줍니다.
     *
     * @param src
     * @return
     */
    public static long zeroConvertLong(String src) throws Exception{
        if (src == null || src.equals("null") || "".equals(src) || " ".equals(src)) {
            return 0;
        }else{
            return Long.parseLong(src.trim());
        }
    }

    /**
     * 공백을 제거합니다.
     * @param str
     * @return
     * @throws Exception
     */
    public static String removeWhitespace(String str) throws Exception{
        if (isEmpty(str)){
            return str;
        }

        int sz = str.length();
        char chs[] = new char[sz];
        int count = 0;

        for (int i = 0; i < sz; i++){
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }

        if (count == sz){
            return str;
        }else{
            return new String(chs, 0, count);
        }
    }

    /**
     * 스트링 치환
     *
     * @param source
     * @param subject
     * @param object
     * @return
     */
    public static String replace(String source, String subject, String object) throws Exception{
        StringBuffer rtnStr = new StringBuffer();
        String preStr = "";
        String nextStr = source;

        for (String srcStr = source; srcStr.indexOf(subject) >= 0; rtnStr.append(preStr).append(object)){
            preStr = srcStr.substring(0, srcStr.indexOf(subject));
            nextStr = srcStr.substring(srcStr.indexOf(subject) + subject.length(), srcStr.length());
            srcStr = nextStr;
        }

        rtnStr.append(nextStr);

        return rtnStr.toString();
    }
    /**
     * 문자열에서 특정문자 삭제
     * @param str
     * @param remove
     * @return
     * @throws Exception
     */
    public static String remove(String str, char remove)throws Exception {
        if(isEmpty(str) || str.indexOf(remove) == -1){
            return str;
        }
        char chars[] = str.toCharArray();
        int pos = 0;
        for(int i = 0; i < chars.length; i++){
            if(chars[i] != remove){
                chars[pos++] = chars[i];
            }
        }
        return new String(chars, 0, pos);
    }

    /**
     * 문자열을 숫자로 바꾸어줍니다. null이거나 공백일시 0으로 바꾸어줍니다.
     *
     * @param src
     * @return
     */
    public static int zeroConvert(Object src) throws Exception{
        if (src == null || src.equals("null")){
            return 0;
        }
        else{
            return Integer.parseInt(((String) src).trim());
        }
    }

    /**
     * 숫자변환가능한 문자열을  LONG으로 변환한다.
     * 값이 없으면 0이다.
     * @param src
     * @return
     * @throws Exception
     */
    public static Long zeroParsLong(Object src) throws Exception{
        if (src == null || src.equals("null")){
            return (long) 0;
        }else{
            return Long.parseLong(((String) src).trim());
        }
    }


    // OBJECT -> MAP 변환
    @SuppressWarnings("finally")
    public static Map ConverObjectToMap(Object obj){

         Map resultMap = new HashMap();

        try {
            //Field[] fields = obj.getClass().getFields(); //private field는 나오지 않음.
            Field[] fields = obj.getClass().getDeclaredFields();

            for(int i=0; i<=fields.length-1;i++){
                fields[i].setAccessible(true);
                resultMap.put(fields[i].getName(), fields[i].get(obj));
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } finally{
          return resultMap;
        }
    } // ConverObjectToMap


	/* 공통 - request 값을 EgovMap에 넣어줌 */
	public static EgovMap getRequestJsonToEgovMap(HttpServletRequest request){
		EgovMap egovMap = new EgovMap();
		HashMap map = new HashMap();

		map = (HashMap)request.getParameterMap();
		Set key = map.keySet();
		for (Iterator iterator = key.iterator(); iterator.hasNext();) {
			String keyName = (String) iterator.next();
			egovMap.put(keyName, request.getParameter(keyName));
		}
		return  egovMap;
	}


	/**
	 * &quot; 변환
	 * @param String
	 * @return String
	 * @throws Exception
	 */
	public static String convertUnicode(String src){
		String conString = src.replace("&quot;", "\"");
		return conString;
	}


	public static EgovMap getParameterJsonToEgovMap(String param){
	EgovMap map = new EgovMap();
  	  ObjectMapper om = new ObjectMapper();
  	  String str = CmmUtil.convertUnicode(param);
  	  Map<String, Object> m = null;
	try {
		m = om.readValue(str, new TypeReference<Map<String, Object>>(){});
	} catch (Exception e) {
		e.printStackTrace();
	}
  	  map.putAll(m);
  	  /*
String str = CmmUtil.convertUnicode(request.getParameter("searchKey"));
    	  Map<String, Object> m = om.readValue(str, new TypeReference<Map<String, Object>>(){});
    	  map = (EgovMap) m;
*/
  	  return map;
	}

	public static EgovMap getJsonStringToEgovMap(String jsonString){
	EgovMap map = new EgovMap();
	ObjectMapper om = new ObjectMapper();
	Map<String, Object> m = null;
	try {
		m = om.readValue(jsonString, new TypeReference<Map<String, Object>>(){});
	} catch (Exception e) {
		e.printStackTrace();
	}
	map.putAll(m);
	return map;
	}



	/**
	 * 엑셀 업로드
	 * @param req
	 * @param res
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value={"/fms/util/cmmutil/exupload.do"}, method={RequestMethod.POST})
	public void excekUpload(HttpServletRequest req, HttpServletResponse res) throws Exception{
		ExcelUtil excelUtil = new ExcelUtil();
		excelUtil.cmExcelUpload(req, res);
	}
}
