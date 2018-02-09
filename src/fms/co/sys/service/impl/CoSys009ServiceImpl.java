package fms.co.sys.service.impl;

import egovframework.rte.fdl.cmmn.EgovAbstractServiceImpl;
import egovframework.rte.psl.dataaccess.util.EgovMap;
import fms.co.sys.dao.CoSys009DAO;
import fms.co.sys.service.CoSys009Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;

/**
 * @Class Name : CoSys009Controller.java
 * @Description : CoSys009 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.04.20
 * @version 1.0
 * @see
 *  
 */
@Service("coSys009Service")
public class CoSys009ServiceImpl extends EgovAbstractServiceImpl implements CoSys009Service {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(CoSys009ServiceImpl.class);
	
    @Resource(name="coSys009DAO")
    private CoSys009DAO coSys009DAO;
    /**
     * 셀렉트 박스 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public List coSys009AddrSelectBoxList(EgovMap map) throws Exception {
		String gb = (String) map.get("gb");
		String sido = (String) map.get("sido");
		String sigungu = (String) map.get("sigungu");
		String doro = (String) map.get("doro");
		String jibun = (String) map.get("jibun");
		
		// sido, sigungu 없을때
		if("".equals(sigungu)){
			return coSys009DAO.coSys009AddrSelectBoxSidoGugunList(map);	//sido 없을때는 쿼리에서 처리
		}else{
			if("doro".equals(gb))
				return coSys009DAO.coSys009AddrSelectBoxDoroList(map);
			else
				return coSys009DAO.coSys009AddrSelectBoxBubList(map);
		}
	}

    /**
     * 셀렉트 박스 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public List coSys009AddrList(EgovMap map) throws Exception{
		map.put("upmen", null);
		Map upmen = coSys009DAO.coSys009SelectUpmen(map);
		if(upmen != null){
    		if(upmen.containsKey("upmen"))
    			map.put("upmen", upmen.get("upmen"));
		}
		return coSys009DAO.coSys009AddrList(map);
	}
	
	/**
     * 셀렉트 박스 조회
     * @param request
     * @param response
     * @param map
     * @return
     * @throws Exception
     */     
	public int coSys009txtUp(String txt) throws Exception{
		int successCnt = 0;
 		char chHeader = 'Y';
 		EgovMap map;
 		StringTokenizer stringTokenizer = new StringTokenizer(txt, "\n");
 		
 		
		while (stringTokenizer.hasMoreTokens()) {
			
			map = new EgovMap();
			String str = stringTokenizer.nextToken();
			
			if(chHeader=='Y'){
				chHeader='N'; continue;
			}
			
			String[] arr = str.split("\\|");
			
			map.put("zipcode", arr[0]);							//새우편번호(구역번호)
			map.put("sidoK", arr[1]);								//시도
			map.put("sigunguK", arr[2]);							//시군구
			map.put("upmenK", arr[3]);							//읍면
			map.put("doroCd", arr[4]);							//도로명코드
			map.put("doroK", arr[5]);								//도로명
			map.put("ziha", arr[6]);								//지하여부_0지상1지하
			map.put("gunmulbonCd", arr[7]);					//건물번호본번
			map.put("gunmulbuCd", arr[8]);					//건물번호부번
			map.put("gunmulgwnCd", arr[9]);					//건물관리번호
			
			map.put("daryng", arr[10]);							//다량배달처_삭제예정
			map.put("sigunguGunmul", arr[11]);				//시군구용건물명
			map.put("bubCd", arr[12]);							//법정동코드
			map.put("bub", arr[13]);								//법정동명
			map.put("li", arr[14]);									//리명
			map.put("hangNm", arr[15]);						//행정동명
			map.put("san", arr[16]);								//산여부_0토지1산
			map.put("zibonCd", arr[17]);						//지번본번
			map.put("zibunbuCd", arr[18]);						//지번부번
			map.put("reasonX", arr[19]);						//이동사유
			
			map.put("oldUpmendongCdX", arr[20]);		//변경전읍면동일련번호
			map.put("upmendongCd", arr[21]);				//읍면동일련번호 (변경후읍면동일련번호)
			map.put("ymdX", arr[22]);							//연계일시
			//map.put("zipcodeOld", arr[23]);					//예전우편번호6자리_삭제예정
			//map.put("zipcodeSn", arr[24]);						//우편번호일련번호_삭제예정
			
			
			if("63".equals(map.get("reasonX"))) {
				coSys009DAO.coSys009txtDel(map); 
				successCnt++;
			}
			else {
				coSys009DAO.coSys009txtUp(map); 
				successCnt++;
			}
			
			
		}; /* while */
		
		System.out.println("successCnt:"+successCnt);
		
		return successCnt;
	}
}