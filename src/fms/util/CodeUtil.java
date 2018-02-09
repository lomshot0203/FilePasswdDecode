package fms.util;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.StringTokenizer;

/**
 * 작성자:민광진
 * 날짜:2015.04.28
 * 소개:코드 검색 콤포넌트
 * 비고:인자값을 꼭 제대로 기입해야하며
 * "="를 토큰으로 잡고 있기 때문에 xml 작성시 유의
 * */

@Component
public class CodeUtil {
	public String codeParsing(String key) {
    	String value = "";
    	try {
			DocumentBuilderFactory docBuildFact = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuild = docBuildFact.newDocumentBuilder();
			Document doc = docBuild.parse("src/main/resources/GicoCode.xml");
			doc.getDocumentElement().normalize();
			NodeList tableList = doc.getDocumentElement().getChildNodes();
			for (int i = 0; i < tableList.getLength(); i++){
				if(tableList.item(i).getNodeType() == Node.ELEMENT_NODE){
					Element tableElement = (Element) tableList.item(i);
					NodeList columnList = tableElement.getChildNodes();
					for(int j = 0; j < columnList.getLength(); j++){
						if(columnList.item(j).getNodeType() == Node.ELEMENT_NODE){
							Element columnElement = (Element) columnList.item(j);
							NodeList codeList = columnElement.getElementsByTagName("CODE");
							for(int k = 0; k < codeList.getLength(); k++){
								Node code = codeList.item(k);
								if(code.getNodeType() == Node.ELEMENT_NODE){
									String codeValue = code.getFirstChild().getNodeValue();
									if(codeValue.matches(".*"+key+".*")){
										StringTokenizer stringTokenizer = new StringTokenizer(codeValue, "=");
										String [] tokenArray = new String[stringTokenizer.countTokens()];
										int tokenCount = 0;
										while (stringTokenizer.hasMoreTokens()) {
											String token = stringTokenizer.nextToken();
											tokenArray[tokenCount] = token;
											tokenCount++;
										}
										if(tokenArray[0].equals(key)){
											value = tokenArray[1];
										}else if(tokenArray[1].equals(key)){
											value = tokenArray[0];
										}
									}
								}
							}
						}
					}
				}
			}
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return value;
	}
}
