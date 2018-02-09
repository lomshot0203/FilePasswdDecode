package fms.util;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.*;
import org.apache.poi.xssf.usermodel.extensions.XSSFCellBorder.BorderSide;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.*;
import java.awt.*;
import java.io.*;
import java.net.URLDecoder;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

/*import org.springframework.web.multipart.MultipartRequest;*/

public class ExcelUtil extends SoftBowlServlet {
	
	private static final long serialVersionUID = 1L;
	private String m_strFileName = ""; 
	private boolean dataChkFlg = false;
	
	private Node m_xnDatagrid = null;
	private JSONObject obj2 = null;
	private JSONArray  arr  = null;
	
	
	
	public void cmExcelDownload(HttpServletRequest Request, HttpServletResponse Response, String GridData) throws ServletException, IOException {
		
		String strContentType = Request.getContentType();
		
		boolean bIsFormData = -1 != strContentType.indexOf("multipart/form-data");

		/*Enumeration<?> enumer =  Request.getParameterNames();
		
		while(enumer.hasMoreElements()){
			String param = (String)enumer.nextElement();
			//System.out.println("param : " + param + " value " + Request.getParameter(param));
		}*/
		
		if (bIsFormData) {
			getRequestData(Request, GridData);
		}else {
			getRequestPostData(Request);
		}
		if(null != m_xnDatagrid ){
			boolean bxls = false;
			if ((m_strFileName.endsWith(".xls") || m_strFileName.endsWith(".cell"))){
				bxls = true;
			}
			//System.out.println("bxls : " + bxls);
			if(bxls){
				HSSFWorkbook objWorkbook = null;
				objWorkbook = makeHSSF();
				
				// MULTIPART_ FORM-DATA 
				if (bIsFormData){
					try{
						if (!m_strFileName.endsWith(".xls") && !m_strFileName.endsWith(".cell")){
							m_strFileName += ".xls";
						}
						m_strFileName = java.net.URLEncoder.encode(m_strFileName, "UTF-8");
						Response.setCharacterEncoding("UTF-8");
						Response.setHeader("Content-Disposition", "attachment;filename="+m_strFileName);
						Response.setContentType("application/vnd.ms-excel");
						objWorkbook.write(Response.getOutputStream());
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}else{
					
					HttpSession objSession = Request.getSession();
					if (!m_strFileName.endsWith(".xls") && !m_strFileName.endsWith(".cell")){
						m_strFileName += ".xls";
					}
					String strFilePath = (getRootPath() + "/kr/softbowl/upload/" + objSession.getId() + "/" +  m_strFileName);
					File objFile = new File(strFilePath);
					create(objFile);
					
					FileOutputStream fileExcelStream = new FileOutputStream(strFilePath);
					objWorkbook.write(fileExcelStream);
					fileExcelStream.close();
					sendResult(Response, "kr/softbowl/upload/" + objSession.getId() + "/" +  m_strFileName, "text/xml; charset=utf-8");
				}
			}else{
				Workbook objWorkbook = null;
				objWorkbook = makeXSSF();
				if (bIsFormData){
					try{
						if (!m_strFileName.endsWith(".xlsx")){
							m_strFileName += ".xlsx";
						}
						m_strFileName = java.net.URLEncoder.encode(m_strFileName, "UTF-8");
						Response.setCharacterEncoding("UTF-8");
						Response.setHeader("Content-Disposition", "attachment;filename="+m_strFileName);
						Response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
						objWorkbook.write(Response.getOutputStream());
					}
					catch (IOException e){
						e.printStackTrace();
					}
				}else{
					HttpSession objSession = Request.getSession();
					if (!m_strFileName.endsWith(".xlsx")){
						m_strFileName += ".xlsx";
					}
					String strFilePath = (getRootPath() + "/kr/softbowl/upload/" + objSession.getId() + "/" +  m_strFileName);
					File objFile = new File(strFilePath);
					create(objFile);
					FileOutputStream fileExcelStream = new FileOutputStream(strFilePath);
					objWorkbook.write(fileExcelStream);
					fileExcelStream.close();
					sendResult(Response, "kr/softbowl/upload/" + objSession.getId() + "/" +  m_strFileName, "text/xml; charset=utf-8");
				}
			}
		}
	}
	
	// 머지를 위한 포지션 생성
	class XYPositionforMerge{
		public int firstRow;
		public int firstColumn;
		public int lastRow;
		public int lastColumn;
		public String dataText;
		
		public XYPositionforMerge(String dataText, int firstRow, int firstColumn,int lastRow, int lastColumn) {
			this.dataText = dataText;
			this.firstRow = firstRow;
			this.firstColumn = firstColumn;
			this.lastRow = lastRow;
			this.lastColumn = lastColumn;
		}
		
	}
	
	//getRequestPostData
	private void getRequestPostData (HttpServletRequest objRequest){
		try {
			objRequest.setCharacterEncoding("UTF-8");
			String strName = objRequest.getParameter("Name");
			String strData = objRequest.getParameter("Data");
			if (null != strName && null != strData){
				m_strFileName = strName.trim();
				m_xnDatagrid = loadXmlDocument(strData.trim());
			}
		} 
		catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	//getRequestData 
	@SuppressWarnings("unchecked")
	private void getRequestData (HttpServletRequest Request, String GridData){
		FileItemFactory objFactory  = new DiskFileItemFactory();
		ServletFileUpload objUpload = new ServletFileUpload(objFactory);
		objUpload.setHeaderEncoding("UTF-8");
		try {
			List<FileItem> fileItems = (List<FileItem>)objUpload.parseRequest(Request);
			Iterator<FileItem> it = fileItems.iterator();
			
			if(fileItems.size() > 0){
				while (it.hasNext()){
					FileItem objItem = it.next();
					if (objItem.isFormField()){
						String strInstanceName  = objItem.getFieldName();
						String strInstanceValue = objItem.getString("UTF-8");
						if ("Name".equals(strInstanceName)){
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								m_strFileName = strInstanceValue.trim();
							}
						}else if ("Data1".equals(strInstanceName)){
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								//strInstanceValue = strInstanceValue.trim();
								m_xnDatagrid = loadXmlDocument(strInstanceValue);
							}
						}else if ("Type".equals(strInstanceName)){
							//System.out.println("####  strInstanceValue   : " + strInstanceValue);
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								if("json".equals(strInstanceValue)){
									dataChkFlg = true;
								}else if("xml".equals(strInstanceValue)){
									dataChkFlg = false;
								}
							}
						}
					}
				}
			}else{
				Enumeration<?> params = Request.getParameterNames();
				while (params.hasMoreElements()){
					String objItem = (String)params.nextElement();
					if (objItem != null){
						String strInstanceName  = objItem;
						String strInstanceValue = Request.getParameter(objItem);
						
						if ("Name".equals(strInstanceName)){
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								m_strFileName = strInstanceValue.trim();
							}
						}else if ("Data1".equals(strInstanceName)){
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								//strInstanceValue = strInstanceValue.trim();
								m_xnDatagrid = loadXmlDocument(strInstanceValue);
							}
						}else if ("Type".equals(strInstanceName)){
							if (null != strInstanceValue && !isEmpty(strInstanceValue)){
								if("json".equals(strInstanceValue)){
									dataChkFlg = true;
								}else if("xml".equals(strInstanceValue)){
									dataChkFlg = false;
								}
							}
							
						}
					}
				}
			}
			
			// JSON TYPE으로 처리
			if(dataChkFlg){
				try {
					Object obj = JSONValue.parseWithException(GridData);
					arr = (JSONArray)obj;
					for(int i=0; i<arr.size(); i++){
						obj2 = (JSONObject)arr.get(i);
						Iterator<?> iter = obj2.keySet().iterator();
						while(iter.hasNext()){
							iter.next();
						}
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
				
			}else{
				m_xnDatagrid = loadXmlDocument(GridData);
			}
		}catch (FileUploadException e){
			e.printStackTrace();
		}catch (UnsupportedEncodingException e){
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("deprecation")
	private HSSFWorkbook makeHSSF (){
		HSSFWorkbook objWorkbook = new HSSFWorkbook();
		HSSFSheet   objSheet   = objWorkbook.createSheet();
		//Ahn Test 20150206
		//org.apache.poi.ss.usermodel.Sheet   objSheet   = objWorkbook.createSheet();
		HSSFPalette objPalette = objWorkbook.getCustomPalette();
		
		
		
		ArrayList<HSSFCellStyle> alColStyle = new ArrayList<HSSFCellStyle>();
		
		short sRowHeight = toShort(getAttributeXml(m_xnDatagrid, "rowHeight"));
		short sDataHeight = toShort(getAttributeXml(m_xnDatagrid, "dataHeight"));
		
		// column
		NodeList xlCol = selectNodes(m_xnDatagrid, "col");
		int nColCount = xlCol.getLength(); 
		
		String[] arRef = new String[nColCount];
		for (int i=0; i<nColCount; i++){
			HSSFCellStyle objStyle = objWorkbook.createCellStyle();
			Node xnCol = xlCol.item(i);
			
			// ref
			arRef[i] = getAttributeXml(xnCol, "ref");
			HSSFFont objFont = objWorkbook.createFont();
			
			// fontFamily
			String strFontFamily = getAttributeXml(xnCol, "fontFamily");
			if (null != strFontFamily && !isEmpty(strFontFamily)){
				objFont.setFontName(strFontFamily);
			}else{
				objFont.setFontName("gulim");
			}
			
			
			// fontStyle
			String strFontStyle = getAttributeXml(xnCol, "fontStyle");
			if (null != strFontStyle && "italic".equals(strFontStyle)){
				objFont.setItalic(true);
			}
			
			// fontWeight
			String strFontWeight = getAttributeXml(xnCol, "fontWeight");
			if (null != strFontWeight){
				if ("bold".equals(strFontWeight) || "bolder".equals(strFontWeight)){
					objFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
				}
			}
			
			// color
			String strColor = getAttributeXml(xnCol, "color");
			if (null != strColor && !isEmpty(strColor)){
				Color objColor = getColorFromHexString(strColor);
				if (null != objColor){
					HSSFColor objHSSFColor = objPalette.findColor((byte)objColor.getRed(), (byte)objColor.getGreen(), (byte)objColor.getBlue());
					objFont.setColor(objHSSFColor.getIndex());
				}
			}
			
			// textDecoration
			String strTextDecoration = getAttributeXml(xnCol, "textDecoration");
			if (null != strTextDecoration && !isEmpty(strTextDecoration)){
				if ("underline".equals(strTextDecoration)){
					objFont.setUnderline(HSSFFont.U_SINGLE);
				}
				else if ("line-through".equals(strTextDecoration)){
					objFont.setStrikeout(true);
				}
			}
			objStyle.setFont(objFont);
			
			// width
			int nWidth = toInt(getAttributeXml(xnCol, "width"));
			objSheet.setColumnWidth((short) i, (short)((nWidth) / ((double)1/32)));

			// align
			String strAlign = getAttributeXml(xnCol, "text-align");
			if ("left".equals(strAlign)){
				objStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
			}else if ("center".equals(strAlign)){
				objStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
			}else if ("right".equals(strAlign)){
				objStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
			}else if ("justify".equals(strAlign)){
				objStyle.setAlignment(HSSFCellStyle.ALIGN_JUSTIFY);
			}else{
				objStyle.setAlignment(HSSFCellStyle.ALIGN_GENERAL);
			}
			
			// vertical-align
			String strVAlign = getAttributeXml(xnCol, "text-align");
			if ("top".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
			}
			else if ("middle".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
			}
			else if ("bottom".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_BOTTOM);
			}
			else
			{
				objStyle.setAlignment(HSSFCellStyle.VERTICAL_CENTER);
			}
			
			alColStyle.add(objStyle);
		}
		
		int nRowCount = 0;
		
		// caption 만들기
		String strCaption = getAttributeXml(m_xnDatagrid, "caption");
		
		String[] arCaptionRow = strCaption.split("\\|");
		for (int i=0; i<arCaptionRow.length; i++){
			String strCaptionRow = arCaptionRow[i];
			String[] arCaptionCol = strCaptionRow.split("\\^");
			
			//Ahn Test 20150206
			//org.apache.poi.ss.usermodel.Row objRow = objSheet.createRow(nRowCount++);
			HSSFRow objRow = objSheet.createRow(nRowCount++);
			
			
			objRow.setHeightInPoints(sRowHeight * 72 / 96);
			for (int j=0; j<arCaptionCol.length && j<nColCount; j++)
			{
				// Ahn Test 20150206
				//org.apache.poi.ss.usermodel.Cell objCell = objRow.createCell((short) j);
				HSSFCell objCell = objRow.createCell((short) j);
				
				
				objCell.setCellValue(arCaptionCol[j]);
				
				HSSFCellStyle objStyle = alColStyle.get(j);
				objCell.setCellStyle(objStyle);
			}
		}
		
		// data 만들기
		if(dataChkFlg){
			if(arr != null){
				for(int i=0; i<arr.size(); i++){
					//행추가
					// Ahn Test 20150206
					//org.apache.poi.ss.usermodel.Row objRow = objSheet.createRow(nRowCount++);
					HSSFRow objRow = objSheet.createRow(nRowCount++);
					
					
					objRow.setHeightInPoints(sDataHeight * 72 / 96);
					
					//JSON객체에서 DATAGRID 각 행에 대한 내용을 파싱해서 MAP 으로 담음
					obj2 = (JSONObject)arr.get(i);
					Iterator<?> iter = obj2.keySet().iterator();
					short j = 0; // 밑 루프에 사용될 INDEX
					while(iter.hasNext()){
						String key = (String)iter.next();
						//HSSFCell objCell = objRow.createCell(j++);
						org.apache.poi.ss.usermodel.Cell objCell = objRow.createCell(j++);
						//System.out.println("key : " + key +"     value " + (String)obj2.get(key));
						objCell.setCellValue(new HSSFRichTextString((String) obj2.get(arRef[j-1])));
						objCell.setCellStyle(alColStyle.get(j-1));
						
					}
				}
			}
		}else{
			Node xnData = selectSingleNode(m_xnDatagrid, "data");
			if (null != xnData){
				NodeList xlData = xnData.getChildNodes();
				for (int i=0; i<xlData.getLength(); i++)
				{
					Node xnRow = xlData.item(i);
										
					// Ahn 20150206
					//HSSFRow objRow = objSheet.createRow(nRowCount++);
					org.apache.poi.ss.usermodel.Row objRow = objSheet.createRow(nRowCount++);
					
					
					objRow.setHeightInPoints(sDataHeight * 72 / 96);
					for (int j=0; j<nColCount; j++)
					{
						Node xnCell = selectSingleNode(xnRow, arRef[j]);
						
						// Ahn 20150206
						org.apache.poi.ss.usermodel.Cell objCell = objRow.createCell(j++);
						//HSSFCell objCell = objRow.createCell((short) j);
						objCell.setCellValue(new HSSFRichTextString(getTextValue(xnCell)));
						objCell.setCellStyle(alColStyle.get(j));
					}
				}
			}
		}
		return objWorkbook;
	}
	
	private Workbook makeXSSF (){
		
		// Workbook and Sheet
		XSSFWorkbook objWorkbook = new XSSFWorkbook();
		XSSFSheet objSheet = objWorkbook.createSheet();
		XSSFCellStyle objFixedcellStyle = objWorkbook.createCellStyle();
		ArrayList<XSSFCellStyle> alColStyle = new ArrayList<XSSFCellStyle>();
		
		short sRowHeight = toShort(getAttributeXml(m_xnDatagrid, "rowHeight"));
		short sDataHeight = toShort(getAttributeXml(m_xnDatagrid, "dataHeight"));
		
		// fixed cell
		{
			Node xnFiexedCell = selectSingleNode(m_xnDatagrid, "fixedcell");

			XSSFFont objFont = objWorkbook.createFont();
			
			// fontFamily
			String strFontFamily = getAttributeXml(xnFiexedCell, "fontFamily");
			if (null != strFontFamily && !isEmpty(strFontFamily)){
				objFont.setFontName(strFontFamily);
			}else{
				objFont.setFontName("gulim");
			}
			
			// fontSize
			short nFontSize = toShort(getAttributeXml(xnFiexedCell, "fontSize").replace("pt", ""));
			if (0 != nFontSize){
				objFont.setFontHeightInPoints(nFontSize);
			}else{
				objFont.setFontHeightInPoints((short)10);
			}
			
			// fontStyle
			String strFontStyle = getAttributeXml(xnFiexedCell, "fontStyle");
			if (null != strFontStyle && "italic".equals(strFontStyle)){
				objFont.setItalic(true);
			}
			
			// fontWeight
			String strFontWeight = getAttributeXml(xnFiexedCell, "fontWeight");
			if (null != strFontWeight){
				if ("bold".equals(strFontWeight) || "bolder".equals(strFontWeight)){
					objFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				}
			}
			
			// color
			String strColor = getAttributeXml(xnFiexedCell, "color");
			if (null != strColor && !isEmpty(strColor)){
				Color objColor = getColorFromHexString(strColor);
				if (null != objColor){
					XSSFColor objXSSFColor = new XSSFColor(objColor);
					objFont.setColor(objXSSFColor);
				}
			}
			
			// textDecoration
			String strTextDecoration = getAttributeXml(xnFiexedCell, "textDecoration");
			if (null != strTextDecoration && !isEmpty(strTextDecoration)){
				if ("underline".equals(strTextDecoration)){
					objFont.setUnderline(XSSFFont.U_SINGLE);
				}
				else if ("line-through".equals(strTextDecoration)){
					objFont.setStrikeout(true);
				}
			}
			objFixedcellStyle.setFont(objFont);
			
			// align
			String strAlign = getAttributeXml(xnFiexedCell, "textAlign");
			if ("left".equals(strAlign)){
				objFixedcellStyle.setAlignment(XSSFCellStyle.ALIGN_LEFT);
			}
			else if ("center".equals(strAlign)){
				objFixedcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			}
			else if ("right".equals(strAlign)){
				objFixedcellStyle.setAlignment(XSSFCellStyle.ALIGN_RIGHT);
			}
			else if ("justify".equals(strAlign)){
				objFixedcellStyle.setAlignment(XSSFCellStyle.ALIGN_JUSTIFY);
			}
			else{
				objFixedcellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
			}
			
			// vertical-align
			String strVAlign = getAttributeXml(xnFiexedCell, "verticalAlign");
			if ("top".equals(strVAlign)){
				objFixedcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			}else if ("middle".equals(strVAlign)){
				objFixedcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			}else if ("bottom".equals(strVAlign)){
				objFixedcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
			}else{
				objFixedcellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			}
			
			/*// background-color
			String strBackgroundColor = getAttributeXml(xnFiexedCell, "backgroundColor");
			if (null != strBackgroundColor && !isEmpty(strBackgroundColor)){
				Color objColor = getColorFromHexString(strBackgroundColor);
				if (null != objColor){
					XSSFColor objXSSFColor = new XSSFColor(objColor);
					objFixedcellStyle.setFillForegroundColor(objXSSFColor);
					objFixedcellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				}else{
					Color objColor2 = getColorFromHexString("#EAEFF3");
					XSSFColor objXSSFColor2 = new XSSFColor(objColor2);
					//objFixedcellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
					objFixedcellStyle.setFillForegroundColor(objXSSFColor2);
					objFixedcellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
				}
			}	*/
			
			Color objColor2 = getColorFromHexString("#EAEFF3");
			XSSFColor objXSSFColor2 = new XSSFColor(objColor2);
			//objFixedcellStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
			objFixedcellStyle.setFillForegroundColor(objXSSFColor2);
			objFixedcellStyle.setFillPattern(CellStyle.SOLID_FOREGROUND);
			
					
			// border-color
			Color objBorderColor = getColorFromHexString(getAttributeXml(xnFiexedCell, "borderColor")); 
			
			// border-left-color
			Color objBorderLeftColor = getColorFromHexString(getAttributeXml(xnFiexedCell, "borderLeftColor"));
			if (null == objBorderColor && null == objBorderLeftColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.LEFT, new XSSFColor(new Color(255, 255, 255)));
			}
			else if (null != objBorderColor && null == objBorderLeftColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.LEFT, new XSSFColor(objBorderColor));
			}
			else if (null != objBorderLeftColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.LEFT, new XSSFColor(objBorderLeftColor));
			}
			
			
			// border-top-color
			Color objBorderTopColor = getColorFromHexString(getAttributeXml(xnFiexedCell, "borderTopColor"));
			if (null == objBorderColor && null == objBorderTopColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.TOP, new XSSFColor(new Color(255, 255, 255)));
			}
			else if (null != objBorderColor && null == objBorderTopColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.TOP, new XSSFColor(objBorderColor));
			}
			else if (null != objBorderTopColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.TOP, new XSSFColor(objBorderTopColor));
			}
			
			// border-right-color
			Color objBorderRightColor = getColorFromHexString(getAttributeXml(xnFiexedCell, "borderRightColor"));
			if (null == objBorderColor && null == objBorderRightColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.RIGHT, new XSSFColor(new Color(111, 111, 95)));
			}
			else if (null != objBorderColor && null == objBorderRightColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.RIGHT, new XSSFColor(objBorderColor));
			}
			else if (null != objBorderRightColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.RIGHT, new XSSFColor(objBorderRightColor));
			}
			
			// border-bottom-color
			Color objBorderBottomColor = getColorFromHexString(getAttributeXml(xnFiexedCell, "borderBottomColor"));
			if (null == objBorderColor && null == objBorderBottomColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.BOTTOM, new XSSFColor(new Color(111, 111, 95)));
			}
			else if (null != objBorderColor && null == objBorderBottomColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.BOTTOM, new XSSFColor(objBorderColor));
			}
			else if (null != objBorderBottomColor)
			{
				objFixedcellStyle.setBorderColor(BorderSide.BOTTOM, new XSSFColor(objBorderBottomColor));
			}
			
			// border-width
			{
				objFixedcellStyle.setBorderLeft(BorderStyle.THIN);
				objFixedcellStyle.setBorderTop(BorderStyle.THIN);
				objFixedcellStyle.setBorderRight(BorderStyle.THIN);
				objFixedcellStyle.setBorderBottom(BorderStyle.THIN);
			}
		}
		
		// column
		NodeList xlCol = selectNodes(m_xnDatagrid, "col");
		int nColCount = xlCol.getLength(); 
		
		String[] arRef = new String[nColCount];
		
		for (int i=0; i<nColCount; i++){
			XSSFCellStyle objStyle = objWorkbook.createCellStyle();
			
			Node xnCol = xlCol.item(i);
			
			// ref
			arRef[i] = getAttributeXml(xnCol, "ref");
			
			XSSFFont objFont = objWorkbook.createFont();
			
			// fontFamily
			String strFontFamily = getAttributeXml(xnCol, "fontFamily");
			if (null != strFontFamily && !isEmpty(strFontFamily))
			{
				objFont.setFontName(strFontFamily);
			}
			else
			{
				objFont.setFontName("gulim");
			}
			
			// fontSize
			short nFontSize = toShort(getAttributeXml(xnCol, "fontSize").replace("pt", ""));
			if (0 != nFontSize)
			{
				objFont.setFontHeightInPoints(nFontSize);
			}
			else
			{
				objFont.setFontHeightInPoints((short)10);
			}
			
			// fontStyle
			String strFontStyle = getAttributeXml(xnCol, "fontStyle");
			if (null != strFontStyle && "italic".equals(strFontStyle))
			{
				objFont.setItalic(true);
			}
			
			// fontWeight
			String strFontWeight = getAttributeXml(xnCol, "fontWeight");
			if (null != strFontWeight)
			{
				if ("bold".equals(strFontWeight) || "bolder".equals(strFontWeight))
				{
					objFont.setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
				}
			}
			
			// color
			String strColor = getAttributeXml(xnCol, "color");
			if (null != strColor && !isEmpty(strColor))
			{
				Color objColor = getColorFromHexString(strColor);
				if (null != objColor)
				{
					XSSFColor objXSSFColor = new XSSFColor(objColor);
					objFont.setColor(objXSSFColor);
				}
			}
			
			// textDecoration
			String strTextDecoration = getAttributeXml(xnCol, "textDecoration");
			if (null != strTextDecoration && !isEmpty(strTextDecoration))
			{
				if ("underline".equals(strTextDecoration))
				{
					objFont.setUnderline(XSSFFont.U_SINGLE);
				}
				else if ("line-through".equals(strTextDecoration))
				{
					objFont.setStrikeout(true);
				}
			}
			objStyle.setFont(objFont);
			
			// width
			int nWidth = toInt(getAttributeXml(xnCol, "width"));
			objSheet.setColumnWidth(i, (short)((nWidth) / ((double)1/32)));

			
			// vertical-align
			String strVAlign = getAttributeXml(xnCol, "verticalAlign");
			if ("top".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_TOP);
			}
			else if ("middle".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			}
			else if ("bottom".equals(strVAlign))
			{
				objStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_BOTTOM);
			}
			else
			{
				objStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
			}
			
			alColStyle.add(objStyle);
		}
		
		int nRowCount = 0;
		
		// CAPTION 생성
		/*
		String strCaption = XmlLib.getAttribute(m_xnDatagrid, "caption");
		
		String[] arCaptionRow = strCaption.split("\\|");
		*/
		
		// CAPTION 생성
		String strCaption = getAttributeXml(m_xnDatagrid, "caption");
		
		String[] arCaptionRow = strCaption.split("\\|");
		
		for (int i=0; i<arCaptionRow.length; i++){
			
			String strCaptionRow = arCaptionRow[i];
			String[] arCaptionCol = strCaptionRow.split("\\^");
			
			XSSFRow objRow = objSheet.createRow(nRowCount++);
			objRow.setHeightInPoints(sRowHeight * 72 / 96);
			for (int j=0; j<arCaptionCol.length && j<nColCount; j++){
				XSSFCell objCell = objRow.createCell(j);
				
				objCell.setCellValue(arCaptionCol[j]);
				objCell.setCellStyle(objFixedcellStyle);
			}
		}
		
		
		try {
			
			int nCaptionRowLegnth = arCaptionRow.length;
			int nCaptionColLength = arCaptionRow[0].split("\\^").length;
			int tempRowCnt = 0;
			int tempColCnt = 0;
			int tempColLimit = 0;
			
			Map<String, Object> tempMap = new HashMap<String, Object>();
					
			if(nCaptionRowLegnth > 1){
				for(int i = 0; i < nCaptionRowLegnth; i+=1){
					
					tempColLimit = 0;
					for(int j = 0; j < nCaptionColLength; j+=1){
						String strTempInfo = (String) tempMap.get(Integer.toString(j));
						if(strTempInfo != null){
							if(strTempInfo.indexOf("|") > 0){
								String[] arrTempInfo = strTempInfo.split("|");
								String[] arrTempInfo2 = arrTempInfo[0].split("\\?");
							}else{
								String[] arrTempInfo2 = strTempInfo.split("\\?");
								if( i <= Integer.parseInt(arrTempInfo2[1]) ){
									continue;
								}
							}
						}
						for(int k = i; k < nCaptionRowLegnth-1; k+=1){
							if( arCaptionRow[k].split("\\^")[j].equals(arCaptionRow[k+1].split("\\^")[j])){
								tempRowCnt++;
							}else{
								// key 값 j  에 RowSpan 값
								break;
							}
							if(tempRowCnt > 0){
								// rowspan 적용할 행의 바로 옆에 열의 값이 같으면
								if(j < (nCaptionColLength -1)){
									if(arCaptionRow[k+1].split("\\^")[j].equals(arCaptionRow[k+1].split("\\^")[j+1])){
										for(int _k = j; _k < nCaptionColLength-1; _k+=1){
											if(arCaptionRow[k+1].split("\\^")[_k].equals(arCaptionRow[k+1].split("\\^")[_k+1])){
												tempColCnt++;
											}else{
												break;
											}
										}
									}else{
//										objSheet.addMergedRegion(new CellRangeAddress(i, tempRowCnt, j, j));
									}
								}
							}
						}
						
						if(tempColCnt > 0){
							objSheet.addMergedRegion(new CellRangeAddress(i, (i+tempRowCnt), j, j+tempColCnt));
							
							for(int _c = 0; _c <= tempColCnt; _c+=1){
								tempMap.put(Integer.toString(j+_c), Integer.toString(j+_c)+'?'+Integer.toString(i + tempRowCnt));
							}
							
							tempColCnt = 0;	
							tempRowCnt = 0;
							continue;
						}
						
						
						tempMap.put(Integer.toString(j) , Integer.toString(j)+'?'+Integer.toString(i + tempRowCnt));
						// RowSpan 카운트가 증가되었다면 0으로 설정하고 Continue
						if(tempRowCnt > 0){
							objSheet.addMergedRegion(new CellRangeAddress(i, (i+ tempRowCnt), j, j));
							tempRowCnt = 0;
							continue;
						}
						
						
						String _strTempInfo = (String)tempMap.get(Integer.toString(j));
						// 컬럼 + colCount 값이 인덱스 J값( 현재 열) 보다 크면 SKIP
						if(j < tempColLimit){
							continue;
						}
						
						if(0 < _strTempInfo.indexOf("|")){
							String[] _arrTempInfo = _strTempInfo.split("|");
							String[] _arrTempInfo2 = _arrTempInfo[1].split("\\?");
							
							if(j < Integer.parseInt(_arrTempInfo2[0]) + Integer.parseInt( _arrTempInfo2[1])){
								continue;
							}
						}
						
						for(int k = j; k < nCaptionColLength-1; k+=1){

							String[] tempCol1 = arCaptionRow[i].split("\\^");
							if(tempCol1[k].equals(tempCol1[k+1])){
								tempColCnt++;
							}else{
								break;
							}
						}
						
						tempMap.put(Integer.toString(j), tempMap.get(Integer.toString(j))+"|"+Integer.toString(j)+'?'+Integer.toString(tempColCnt));
						if(tempColCnt > 0){
							objSheet.addMergedRegion(new CellRangeAddress(i, i, j, j + tempColCnt));
						}
						tempColLimit = j + tempColCnt;
						tempColCnt = 0;
					}
				}
			}else{
				for(int i = 0; i < nCaptionRowLegnth; i+=1){
					tempColLimit = 0;
					for(int j = 0; j < nCaptionColLength; j+=1){
						if(j < tempColLimit){
							continue;
						}
						for(int k = j; k < nCaptionColLength-1; k+=1){
							String[] tempCol1 = arCaptionRow[i].split("\\^");
							if(tempCol1[k].equals(tempCol1[k+1])){
								tempColCnt++;
							}else{
								break;
							}
						}
						if(tempColCnt > 0){
							objSheet.addMergedRegion(new CellRangeAddress(i, i, j, j + tempColCnt));
						}
						tempColLimit = j + tempColCnt;
						tempColCnt = 0;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		// ** Ahn Position 을 위한 좌료 List 객체
		//**********************************************************************************
		/*
		List<XYPositionforMerge> MergeXYPosition = new ArrayList<XYPositionforMerge>();	
				
		int firstRowPos = 0;
		int lastRowPos = 0;
		
		for (int i=0; i<arCaptionRow.length; i++){
						
			String strCaptionRow = arCaptionRow[i];
			String[] arCaptionCol = strCaptionRow.split("\\^");
			
			// 두번째이상의 Row 를 비교하기 위한 배열 생성
			String[] arCaptionColTemp = null;			
			if (i > 0){				
				arCaptionColTemp = arCaptionRow[i-1].split("\\^");
			}		
			
			XSSFRow objRow = objSheet.createRow(nRowCount++);
			objRow.setHeightInPoints(sRowHeight * 72 / 96);
			
			int changeColIndex = -1;			
			
			int firstColumnPos = 0;			
			int lastColumnPos = 0;
			boolean isChanged = false;
			
			for (int j=0; j<arCaptionCol.length && j<nColCount; j++){
				
				XSSFCell objCell = objRow.createCell(j);
				objCell.setCellStyle(objFixedcellStyle);
				objCell.setCellValue(arCaptionCol[j]);
				
				// 처음 데이터는 아니고, 컬럼명과 같다면
				// first Row, last Row, first Column, last Column
				if (j > 0 && arCaptionCol[j].equals(arCaptionCol[j-1])) {
					
					System.out.println(arCaptionCol[j]);
					if (changeColIndex == -1 ) changeColIndex = j-1;	
										
					// 제일 마지막 컬럼이 아니면
					if (j != nColCount -1) continue;					
					
				} 
				
				firstRowPos = i;
				lastRowPos = i;
				
				if (changeColIndex >= 0){
					isChanged = true;					
					firstColumnPos = changeColIndex;
					// 제일 마지막의 머지일때는 j 값을 변경한다.
					if (j == nColCount -1)	lastColumnPos = j;
					else lastColumnPos = j-1;					
															
					changeColIndex = -1;					
				} else {
					// 일반적으로 컴럼과 열간 둘다 머지가 되는 경우는 극히 없다.
					if (i > 0){							
						if (arCaptionColTemp[j].equals(arCaptionCol[j]) ){	
							firstRowPos = i-1;
							firstColumnPos = j;
							lastColumnPos = j;
							isChanged = true;
						}
					}					
				}				
				
				if (isChanged){
					MergeXYPosition.add(new XYPositionforMerge(arCaptionCol[j], firstRowPos, firstColumnPos,lastRowPos,lastColumnPos));
					isChanged = false;
				}
			}		
		}
		
		// 상하위 값을 위한 비교 로직 .. 위 상하위 비교가 복잡하여 별도 처리함
		for (int i = 0; i < MergeXYPosition.size(); i++) {			
			
			// 상하비교 로직
			for(int j=i ; j < MergeXYPosition.size(); j++){
				if (MergeXYPosition.get(i).dataText.equals( MergeXYPosition.get(j).dataText)
				   && MergeXYPosition.get(i).firstColumn == MergeXYPosition.get(j).firstColumn
				   && MergeXYPosition.get(i).lastColumn == MergeXYPosition.get(j).lastColumn) {
					
					// 검색시 lastRow 가 더 큰것이 있으면 i 번째의 lastRow 를 빠꾸고, j 는 없앤다.
					if (MergeXYPosition.get(i).lastRow < MergeXYPosition.get(j).lastRow){
						
						MergeXYPosition.set(i, new XYPositionforMerge(MergeXYPosition.get(i).dataText, MergeXYPosition.get(i).firstRow , MergeXYPosition.get(i).firstColumn, MergeXYPosition.get(j).lastRow ,MergeXYPosition.get(i).lastColumn) )  ;
						MergeXYPosition.remove(j);
					}				
				}
			}                                    
		}
		
		// 실제 머지가 이루어 지는 부문
		for (int i = 0; i < MergeXYPosition.size(); i++) {
			
			objSheet.addMergedRegion(new CellRangeAddress(MergeXYPosition.get(i).firstRow
													      ,MergeXYPosition.get(i).lastRow
					                                      ,MergeXYPosition.get(i).firstColumn
					                                      ,MergeXYPosition.get(i).lastColumn));
		}
		*/
		
		// DATA 생성 부분
		if(dataChkFlg){
			NumberFormat f = NumberFormat.getInstance();
			f.setGroupingUsed(false);
			//JSON객체 인경우 
			if(arr != null){
				for(int i=0; i<arr.size(); i++){
					//행추가
					XSSFRow objRow = objSheet.createRow(nRowCount++); 
					objRow.setHeightInPoints(sDataHeight * 72 / 96);
					
					//JSON객체에서 DATAGRID 각 행에 대한 내용을 파싱해서 MAP 으로 담음
					obj2 = (JSONObject)arr.get(i);
					Iterator<?> iter = obj2.keySet().iterator();
					
					short j = 0; // 밑 루프에 사용될 INDEX
					//System.out.println("arRef.length : "+arRef.length);
					while(iter.hasNext()){
						//각 행의 컬럼수 만큼 루프돌면서 행 추가
						XSSFCell objCell = objRow.createCell(j++);
						//System.out.println("j : [ " + j + "] obj2.get(arRef[j-1]) : " + obj2.get(arRef[j-1]));
						if(obj2.get(arRef[j-1]) instanceof Long){
						    objCell.setCellValue(f.format((Long)obj2.get(arRef[j-1])));
						    
						// 2015-01-21 안종수 추가
						} else if (obj2.get(arRef[j-1]) instanceof Double){
							objCell.setCellValue((Double)obj2.get(arRef[j-1]));
					    } else{
							objCell.setCellValue((String)obj2.get(arRef[j-1])); //DATAGRID 컬럼의 REF값이 KEY값이 된다.
						}
						objCell.setCellStyle(alColStyle.get(j-1));
						
						//arRef Index 까지만 Loop
						if(arRef.length == j) break;
					}
				}
			}
		}else{
			//XML 인경우
			Node xnData = selectSingleNode(m_xnDatagrid, "data");
			if (null != xnData){
				NodeList xlData = xnData.getChildNodes();
				for (int i=0; i<xlData.getLength(); i++){
					Node xnRow = xlData.item(i);
					
					XSSFRow objRow = objSheet.createRow(nRowCount++);
					objRow.setHeightInPoints(sDataHeight * 72 / 96);
					NodeList xlCell = xnRow.getChildNodes();
					for (int j=0; j<nColCount; j++){
						Node xnCell = findChildByNodeName(xlCell, arRef[j]);
						if (null != xnCell){
							XSSFCell objCell = objRow.createCell(j);
							objCell.setCellValue(getTextValue(xnCell));
							objCell.setCellStyle(alColStyle.get(j));
						}
					}
				}
			}
		}
		return objWorkbook;
	}

	private Node findChildByNodeName (NodeList xlRow, String strName){
		for (int i=0; i<xlRow.getLength(); i++){
			Node xnCell = xlRow.item(i);
			if (Node.ELEMENT_NODE == xnCell.getNodeType() && strName.equals(xnCell.getNodeName())){
				return xnCell;
			}
		}
		return null;
	}
	
	public void cmExcelUpload(HttpServletRequest objRequest, HttpServletResponse objResponse) throws ServletException, IOException{
		
		//MultipartRequest multi = null; 
		
		ServletContext context = objRequest.getSession().getServletContext();
		
		//String realPath = context.getRealPath("/");
		
		/*
		File f = new File(realPath + "kr\\softbowl");
		if(!f.isDirectory()){
			if(f.mkdirs()){
				//System.out.println("Directory Make !");
			}
		}
		*/
		
		/*
		if (-1 != System.getProperty("os.name").indexOf("Window")) {
			multi = new MultipartRequest(objRequest, realPath + "kr\\softbowl");  
		}else{ 
			multi = new MultipartRequest(objRequest, realPath + "/kr/softbowl");
		}
		*/
		
		MultipartHttpServletRequest multiReq = (MultipartHttpServletRequest)objRequest;
		
		/*
		String strGridNodeset	= multi.getParameter("nodeset");
		String dataType			= multi.getParameter("Type");
		String targetGrid		= multi.getParameter("targetGrid");
		String gridJsonRef		= multi.getParameter("gridJsonRef");
		*/
		boolean cntCk = false;
		String strCnt = "";
		
		String strGridNodeset	= multiReq.getParameter("nodeset");
		String dataType				= multiReq.getParameter("Type");
		String targetGrid			= multiReq.getParameter("targetGrid");
		String gridJsonRef			= multiReq.getParameter("gridJsonRef");
		int startIdx					= Integer.parseInt(multiReq.getParameter("startIdx")) - 1;
		String strCntYn				= multiReq.getParameter("strCntYn");
		if("true".equals(strCntYn)) {
			cntCk = true;
			strCnt					= multiReq.getParameter("strCnt");
			System.out.println(strCnt);
		}
		StringBuffer sb			= new StringBuffer();
		
		if(dataType != null && "xml".equals(dataType)){
			sb.append("<root>");
			try{
				StringBuffer strNodeSetXML = new StringBuffer();
				String[] arGridData = strGridNodeset.split("@");
				String[] arNodeSet = arGridData[0].replaceAll("/root/", "").split("/");
				String[] arRefs = URLDecoder.decode(arGridData[1],"UTF-8").split(",");
				String strFileType = arGridData[2];
				for (int i=0; i< arNodeSet.length - 1; i++){
					strNodeSetXML.append("<"+ arNodeSet[i] +">");
				}
				//multi.getFilesystemName("excel");
				MultipartFile multiFile = multiReq.getFile("excel");
				//File fExcel = multi.getFile("excel");
				
				if ("xls".equals(strFileType) || "cell".equals(strFileType)){
					//HSSFWorkbook XLSworkbook = new HSSFWorkbook(new POIFSFileSystem(new FileInputStream(fExcel)));
					HSSFWorkbook XLSworkbook = new HSSFWorkbook(new POIFSFileSystem(multiFile.getInputStream()));
					HSSFSheet sheet = XLSworkbook.getSheetAt(0);
					
					int nStartRow = sheet.getFirstRowNum();
					int nEndRow = sheet.getLastRowNum();
					for (int i=nStartRow; i<=nEndRow; i++){
						strNodeSetXML.append("<"+ arNodeSet[arNodeSet.length - 1] +">");
						HSSFRow row = sheet.getRow(i);
						if (null != row){
							for(int j=0; j<arRefs.length; j++){
								HSSFCell cell = row.getCell(j);
								if(null != cell){
									strNodeSetXML.append("<"+ arRefs[j] +"><![CDATA[");
									switch (cell.getCellType()) {
										case 0: // XSSFCell.CELL_TYPE_NUMERIC:
											String strValue = "" + cell.getNumericCellValue();
											String strDotValue =  strValue.substring(strValue.lastIndexOf("."), strValue.length());
											if (".0".equals(strDotValue)){
												strValue = strValue.replaceAll("\\.0", "");
											}
											strNodeSetXML.append(strValue);
											break;
										case 2: // XSSFCell.CELL_TYPE_FORMULA:
											strNodeSetXML.append(cell.getNumericCellValue());
											break;
										case 1: // XSSFCell.CELL_TYPE_STRING:
											@SuppressWarnings("deprecation")
											String strCellValue = cell.getStringCellValue().replaceAll("\\'","\\\\'");
											strCellValue = strCellValue.replace("\n", "__SB_N");
											strCellValue = strCellValue.replace("\t", "__SB_T");
											strCellValue = strCellValue.replace("\r", "__SB_R");
											strNodeSetXML.append(strCellValue);
											break;
									}
									 strNodeSetXML.append("]]></"+ arRefs[j] +">");
								}else{
									 strNodeSetXML.append("<"+ arRefs[j] +"/>");
								}
							}
						}else{
							for (int j=0; j<arRefs.length; j++){
								strNodeSetXML.append("<"+ arRefs[j] +"/>");
							}
						}
						strNodeSetXML.append("</"+ arNodeSet[arNodeSet.length - 1] +">");
					}
				}else{
					XSSFWorkbook XLSXworkbook = new XSSFWorkbook(multiFile.getInputStream());
					XSSFSheet sheet = XLSXworkbook.getSheetAt(0);
					int rows = sheet.getPhysicalNumberOfRows();
					for (int i=0; i<rows; i++){
						strNodeSetXML.append("<"+ arNodeSet[arNodeSet.length - 1] +">");
						XSSFRow row = sheet.getRow(i);
						if (null != row){
							for (int j=0; j<arRefs.length; j++){
								XSSFCell cell = row.getCell(j);
								if(null != cell){
									strNodeSetXML.append("<"+ arRefs[j] +"><![CDATA[");
									switch (cell.getCellType()) {
										case 0: // XSSFCell.CELL_TYPE_NUMERIC:
											String strValue = "" + cell.getNumericCellValue();
											String strDotValue =  strValue.substring(strValue.lastIndexOf("."), strValue.length());
											if (".0".equals(strDotValue))
											{
												strValue = strValue.replaceAll(".0", "");
											}
											strNodeSetXML.append(strValue);
											break;
										case 2: // XSSFCell.CELL_TYPE_FORMULA:
											strNodeSetXML.append(cell.getNumericCellValue());
											break;
										case 1: // XSSFCell.CELL_TYPE_STRING:
											String strCellValue = cell.getStringCellValue().replaceAll("\\'","\\\\'");
											strCellValue = strCellValue.replace("\n", "__SB_N");
											strCellValue = strCellValue.replace("\t", "__SB_T");
											strCellValue = strCellValue.replace("\r", "__SB_R");
											strNodeSetXML.append(strCellValue);
											break;
									}
									 strNodeSetXML.append("]]></"+ arRefs[j] +">");
								}else{
									 strNodeSetXML.append("<"+ arRefs[j] +"/>");
								}
							}
						}else{
							for (int j=0; j<arRefs.length; j++){
								strNodeSetXML.append("<"+ arRefs[j] +"/>");
							}
						}
						strNodeSetXML.append("</"+ arNodeSet[arNodeSet.length - 1] +">");
					}
				}
				
//				fExcel.delete();
				
				for (int i = arNodeSet.length - 2; i >= 0; i--){
					strNodeSetXML.append("</"+ arNodeSet[i] +">");
				}
				sb.append(strNodeSetXML.toString());
			}catch (Exception e){
				sb.append("<TF_EXCEL_ERR/>");
				e.printStackTrace();
			}finally{
				sb.append("</root>");
				StringBuffer strHTML = new StringBuffer();
				strHTML.append("<html><head><script type='text/javascript'>");
				strHTML.append("var strLoadXml = '" + sb.toString()+ "';");
				strHTML.append("strLoadXml =strLoadXml.split(\"\\_\\_SB\\_N\").join(\"\\n\");");
				strHTML.append("strLoadXml =strLoadXml.split(\"\\_\\_SB\\_T\").join(\"\\t\");");
				strHTML.append("strLoadXml =strLoadXml.split(\"\\_\\_SB\\_R\").join(\"\\r\");");
				strHTML.append("</script></head><body/><html>");
				sendResult(objResponse, strHTML.toString(), "text/html");
			}
			
		}else if(dataType != null && "json".equals(dataType)){
			JSONArray  jsonArr = new JSONArray();
			JSONObject jsonObj1 = new JSONObject();
			try{
				String[] arGridData = strGridNodeset.split("@");
				String[] arRefs = URLDecoder.decode(arGridData[1],"UTF-8").split(",");
				String strFileType = arGridData[2];
				
				//multi.getFilesystemName("excel");
				MultipartFile multiFile = multiReq.getFile("excel");
				//File fExcel = multi.getFile("excel");
				
				if ("xls".equals(strFileType) || "cell".equals(strFileType)){
					HSSFWorkbook XLSworkbook = new HSSFWorkbook(new POIFSFileSystem(multiFile.getInputStream()));
					HSSFSheet sheet = XLSworkbook.getSheetAt(0);
					int nStartRow = sheet.getFirstRowNum();
					int nEndRow = sheet.getLastRowNum();
					
					for (int i=nStartRow; i<=nEndRow; i++){
						JSONObject jsonObj  = new JSONObject();
						HSSFRow row = sheet.getRow(i);
						if (null != row){
							for(int j=0; j<arRefs.length; j++){
								HSSFCell cell = row.getCell(j);
								if(null != cell){
									switch (cell.getCellType()) {
										case 0: // XSSFCell.CELL_TYPE_NUMERIC:
											String strValue = "" + cell.getNumericCellValue();
											String strDotValue =  strValue.substring(strValue.lastIndexOf("."), strValue.length());
											if (".0".equals(strDotValue)){
												strValue = strValue.replaceAll("\\.0", "");
											}
											jsonObj.put(arRefs[j], strValue);
											break;
										case 2: // XSSFCell.CELL_TYPE_FORMULA:
											jsonObj.put(arRefs[j], cell.getNumericCellValue());
											break;
										case 1: // XSSFCell.CELL_TYPE_STRING:
											@SuppressWarnings("deprecation")
											String strCellValue = cell.getStringCellValue().replaceAll("\\'","\\\\'");
											//System.out.println("strCellValue :::: ####" + strCellValue);
											strCellValue = strCellValue.replace("\n", "__SB_N");
											strCellValue = strCellValue.replace("\t", "__SB_T");
											strCellValue = strCellValue.replace("\r", "__SB_R");
											jsonObj.put(arRefs[j], strCellValue);
											break;
									}
								}else{
									jsonObj.put(arRefs[j], "");
								}
							}
						}
						jsonArr.add(jsonObj);
					}
				}else{
					XSSFWorkbook XLSXworkbook = new XSSFWorkbook(multiFile.getInputStream());
					XSSFSheet sheet = XLSXworkbook.getSheetAt(0);
					int rows = sheet.getPhysicalNumberOfRows();
					//System.out.println("row : [" + rows + "]");
					//System.out.println(startIdx);
					//Object rltVal = "";
					for (int i=0+startIdx; i<rows; i++){
						JSONObject jsonObj  = new JSONObject();
						XSSFRow row = sheet.getRow(i);
						if (null != row){
							for (int j=0; j<arRefs.length; j++){
								XSSFCell cell = row.getCell(j);
								if(null != cell){
									switch (cell.getCellType()) {
										case 0: // XSSFCell.CELL_TYPE_NUMERIC:
											cell.setCellType(cell.CELL_TYPE_STRING);
											String strValue = cell.getStringCellValue();
											jsonObj.put(arRefs[j], strValue);
											break;
										case 2: // XSSFCell.CELL_TYPE_FORMULA:
											jsonObj.put(arRefs[j], cell.getNumericCellValue());
											break;
										case 1: // XSSFCell.CELL_TYPE_STRING:
											String strCellValue = cell.getStringCellValue().replaceAll("\\'","\\\\'");
											strCellValue = strCellValue.replace("\n", "__SB_N");
											strCellValue = strCellValue.replace("\t", "__SB_T");
											strCellValue = strCellValue.replace("\r", "__SB_R");
											jsonObj.put(arRefs[j], strCellValue);
											jsonObj.put(arRefs[j], strCellValue);
											break;
									}
								}else{
									jsonObj.put(arRefs[j], "");
								}
							}
						}
						jsonArr.add(jsonObj);
					}
					jsonObj1.put("grid", jsonArr);
				}
				
				
				//fExcel.delete();
				
				
			}catch (Exception e){
				e.printStackTrace();
			}finally{
				StringBuffer strHTML = new StringBuffer();
				strHTML.append("<html><head><script type='text/javascript'>");
				strHTML.append("function sbGridLoadExcel()");
				strHTML.append("{ eval(parent."+gridJsonRef+"="+jsonArr+");");
				strHTML.append("eval(parent."+targetGrid+".rebuild());}");
				strHTML.append("</script></head><body onload='sbGridLoadExcel()'>");
				strHTML.append("</body></html>");
				//System.out.println("jsonArr : " + jsonArr);
				sendResult(objResponse, strHTML.toString(), "text/html");
			}
		}else{
			System.out.println("Not Setting [[ DataType ]] !!");
		}
	}
	
	private static short toShort (String strValue){
		short nValue;

		try{
			// 소수점 제거
			String strInt = strValue.replaceAll("^([^\\.]*).*", "$1");
			nValue = (short)Integer.parseInt(strInt);
		}catch (Exception e){
			nValue = 0;
		}
		return nValue;
	}
	

	private static int toInt (String strValue){
		int nValue;

		try{
			// 소수점 제거
			String strInt = strValue.replaceAll("^([^\\.]*).*", "$1");
			nValue = Integer.parseInt(strInt);
		}catch (Exception e){
			nValue = 0;
		}
		return nValue;
	}

	/**
	 * 파일을 생성한다.
	 * @param objFile
	 */
	private static void create (File objFile){
		if (!objFile.exists()){
			String strParent = objFile.getParent();
			if (null != strParent){
				File objParent = new File(strParent);
				if (!objParent.exists()){
					objParent.mkdirs();
				}
			}
			try{
				objFile.createNewFile();
			}catch (IOException e){
				e.printStackTrace();
			}
		}
	}
	
	
	/**
	 * String이 비어있는지 체크
	 * @param str
	 * @return
	 */
	private static boolean isEmpty (String str){
		return (null == str || 0 == str.length()) ? true : false;
	}
	
	
	private static Color getColorFromHexString (String strHex){
		Color objColor = null;
		if (null != strHex && ! isEmpty(strHex)){
			if (-1 < strHex.indexOf("#")){
				strHex = strHex.replace("#", "");
				objColor = new Color(0xff000000 | Integer.parseInt(strHex, 16));
			}
		}
		return objColor;
	}
	
	
	
	private static String docToString (Document xnDoc){
		StringWriter swResult = new StringWriter();
		Transformer objTransformer = null;
		try{
			objTransformer = TransformerFactory.newInstance().newTransformer();
			objTransformer.setOutputProperty(OutputKeys.METHOD, "xml");
			objTransformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
			objTransformer.setOutputProperty(OutputKeys.INDENT, "no");
			objTransformer.transform(new DOMSource(xnDoc), new StreamResult(swResult));
		}catch(TransformerConfigurationException e){
			e.printStackTrace();
		}
		catch(TransformerException e)
		{
			e.printStackTrace();
		}finally{
			if(null != swResult){
				return swResult.toString();
			}
		}
		return "";
	}
	
	
	/**
	 * Node 의 내용을 String 으로 반환한다.
	 * @param xnNode
	 * @return
	 */
	private static String nodeToString (Node xnNode){
		StringWriter swResult = new StringWriter();
		try{
			Transformer objTransformer = TransformerFactory.newInstance().newTransformer();
			objTransformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			objTransformer.transform(new DOMSource(xnNode), new StreamResult(swResult));
		}catch (TransformerException e){
			e.printStackTrace();
		}
		return swResult.toString();
	}
	
	private static String nodeToStringForCData (Node xnNode){
		String strResult = nodeToString(xnNode);
		if (null == strResult){
			return "";
		}
		/*strResult = strResult.replace("\\", "\\\\");	
		strResult = strResult.replace("\n", "\\n");
		strResult = strResult.replace("\t", "\\t");
		strResult = strResult.replace("\r", "\\r");
		strResult = strResult.replace("\"", "\\\"");*/
		
		return strResult;
	}
	
	private static String nodeToStringForInstance (Node xnNode){
		// 노드를 순회 하여 텍스트 노드를 삭제한다.
		removeTextNode(xnNode);
		
		String strResult = nodeToString(xnNode);
		strResult = strResult.replaceAll(">((\\s|\n|\t|\r)+)<","><");
		if (null == strResult){
			return "";
		}
		strResult = strResult.replace("\"", "\\\"");
		strResult = strResult.replace("\n", "__SB_N");
		strResult = strResult.replace("\t", "__SB_T");
		strResult = strResult.replace("\r", "__SB_R");
		return strResult;
	}
	
	private static void removeTextNode (Node xnNode){
		if (null == xnNode){
			return;
		}
		NodeList xlChild = xnNode.getChildNodes();
		int nCount = xlChild.getLength();
		for (int i=nCount-1; i>=0; i--){
			Node xnChild = xlChild.item(i);
			if (Node.TEXT_NODE == xnChild.getNodeType() && 1 < nCount){
				xnNode.removeChild(xnChild);
			}else if (Node.ELEMENT_NODE == xnChild.getNodeType()){
				removeTextNode(xnChild);
			}
		}
	}

	/**
	 * 스크림으로부터 XML 문서를 로드한다.
	 * @param stream
	 * @return XML 문서의 Root Node
	 */
	private static Node loadXmlDocument (InputStream stream){
		Document docOwner = null;
		Node xnRoot = null;
		DocumentBuilder builder = null;
		
		try{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			if(null != builder){
				try{
					docOwner = builder.parse(stream);
					if(null != docOwner)
						xnRoot = docOwner.getDocumentElement();
				}
				catch(SAXException e){
					// 예외
				}catch(IOException e){
					// 예외
				}
			}				
		}
		catch(ParserConfigurationException e){
			// 예외
		}
		finally{
			docOwner = null;
			builder = null;
		}
		return xnRoot;
	}
	
	/**
	 * @param source
	 * @return
	 */
	private static Node loadXmlDocument (InputSource source){
		Document docOwner = null;
		Node xnRoot = null;
		DocumentBuilder builder = null;
		try{
			builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
			if(null != builder){
				try{
					docOwner = builder.parse(source);
					if(null != docOwner)
						xnRoot = docOwner.getDocumentElement();
				}catch(SAXException e){
					e.printStackTrace();
				}catch(IOException e){
					e.printStackTrace();
				}
			}				
		}catch(ParserConfigurationException e){
			e.printStackTrace();
		}finally{
			docOwner = null;
			builder = null;
		}
		return xnRoot;
	}
	
	/**
	 * 임시로 문자열에서 XML을 읽을 수 있게 만들었습니다.
	 * @param strXml
	 * @return
	 */
	private static Node loadXmlDocument (String strXml){
		InputSource source = new InputSource(new StringReader(strXml));
		return loadXmlDocument(source);
	}
	
	/**
	 * @param xnNode
	 * @return
	 */
	private static Document getOwnerDocument (Node xnNode){
		Document docOwner = null;
		if (null != xnNode)
			docOwner = xnNode.getOwnerDocument();
		return docOwner;
	}
	
	
	/**
	 * @param xnNode
	 * @param strAttribute
	 * @return
	 */
	private static String getAttributeXml (Node xnNode, String strAttribute){
		String strResult = "";
		if (null != xnNode && null != strAttribute && 0 < strAttribute.length()){
			if (Node.ELEMENT_NODE == xnNode.getNodeType()){
				Element element = (Element)xnNode;
				strResult = element.getAttribute(strAttribute);
			}
		}
		return strResult;
	}
	
	
	/**
	 * @param xnNode
	 * @return
	 */
	private static String getTextValue (Node xnNode){
		String strResult = "";
		if (null != xnNode && null != xnNode.getFirstChild()){
			if (Node.TEXT_NODE == xnNode.getFirstChild().getNodeType() || Node.CDATA_SECTION_NODE == xnNode.getFirstChild().getNodeType()){
				
				String strTempText = nodeToString(xnNode);
				int nStartIndex = strTempText.indexOf("<![CDATA[");
				int nEndIndex = strTempText.lastIndexOf("]]>");
				if (-1 == nStartIndex || -1 == nEndIndex){
					strResult = xnNode.getFirstChild().getNodeValue();
				}else{
					strResult = strTempText.substring(nStartIndex + 9, nEndIndex);
				}
			}
		}
		return strResult;
	}
	
	
	/**
	 * @param xnNode
	 * @param strAttribute
	 * @param strValue
	 */
	private static void setAttribute (Node xnNode, String strAttribute, String strValue){
		if (null != xnNode && null != strAttribute && null != strValue){
			if (Node.ELEMENT_NODE == xnNode.getNodeType()){
				Element element = (Element)xnNode;
				element.setAttribute(strAttribute, strValue);
			}
		}
	}
	
	private static void removeAttribute (Node xnNode, String strAttribute){
		if (null != xnNode && null != strAttribute && 0< strAttribute.length()){
			if (Node.ELEMENT_NODE == xnNode.getNodeType()){
				Element element = (Element)xnNode;
				element.removeAttribute(strAttribute);
			}
		}
	}
	
	/**
	 * @param xnNode
	 * @param strValue
	 */
	private static void setTextValue (Node xnNode, String strValue){
		if (null != xnNode && null != strValue){
			if (false == xnNode.hasChildNodes()){
				Node xnText = xnNode.getOwnerDocument().createTextNode(strValue);
				xnNode.appendChild(xnText);
			}else if (Node.TEXT_NODE == xnNode.getFirstChild().getNodeType()) {// Node.TEXT_NODE = 3
				xnNode.getFirstChild().setNodeValue(strValue);
			}
		}
	}
	
	
	/**
	 * xnNode에서 XPath가 strPath인 노드를 구한다.
	 * @param xnNode
	 * @param strPath
	 * @return
	 */
	private static Node selectSingleNode (Node xnNode, String strPath){
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Node selectNode = null;
		try{
			XPathExpression expr = xpath.compile(strPath);
			selectNode = (Node)expr.evaluate(xnNode, XPathConstants.NODE);
		}catch (XPathExpressionException e){
			e.printStackTrace();
		}
		 
		return selectNode;
	}
	
	/**
	 * xnNode에서 XPath가 strPath인 노드 리스트를 구한다.
	 * @param xnNode
	 * @param strPath
	 * @return
	 */
	private static NodeList selectNodes (Node xnNode, String strPath){
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList selectNodes = null;
		try{
			XPathExpression expr = xpath.compile(strPath);
			selectNodes = (NodeList)expr.evaluate(xnNode, XPathConstants.NODESET);
		}catch (XPathExpressionException e){
			e.printStackTrace();
		}
		return selectNodes;
	}
}
