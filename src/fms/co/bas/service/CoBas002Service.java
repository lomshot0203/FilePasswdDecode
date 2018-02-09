package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.30
 */
public interface CoBas002Service {
	/**모계좌조회*/
    List<?> getMasterAccountList(EgovMap map) throws Exception;
	/**은행+계좌 셀렉트박스 생성*/
    List<?> getBankPlusAccountSelectBox() throws Exception; 
	/**가상계좌조회*/
    List<?> getVirtualAccountList(EgovMap map) throws Exception;
    /** 은행코드조회*/
    String getBanCd(String pntacnCd) throws Exception;
    /**저장*/
    List<?> save(EgovMap map) throws Exception;
    /**모계좌 저장*/
    EgovMap saveMasterAccount(EgovMap map) throws Exception;
    /**가상계좌 저장*/
    EgovMap saveVirtualAccount(EgovMap map) throws Exception;
}
