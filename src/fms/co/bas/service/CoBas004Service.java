package fms.co.bas.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.13
 */
public interface CoBas004Service {
	/**주민번호 복호화*/
    String getDecRRN(String encRRN) throws Exception;
	/**고객정보 조회*/
    List<?> getCustomerInformation(EgovMap map) throws Exception;
    /**고객상세 조회*/
	List<?> getMoreInformation(EgovMap map) throws Exception;
	/**고객 계좌정보 조회*/
	List<?> getBanAcnInformation(EgovMap map) throws Exception;
	/**담당자 정보 조회*/
	List<?> getCgpInformation(EgovMap map) throws Exception;
    /**고객변경 조회*/
	List<?> getChangeInformation(EgovMap map) throws Exception;
    /**고객정보 저장*/
	EgovMap saveCustomerInformation(EgovMap map) throws Exception;
    /**고객상세 저장*/
	EgovMap saveMoreInformation(EgovMap map) throws Exception;
    /**고객변경 저장*/
	EgovMap saveChangeInformation(EgovMap map) throws Exception;
	/**저장*/
    List<?> save(EgovMap map) throws Exception;

    /**계약번호를 이용하여 계약구분을 조회*/
    String getCtrGb(String ctrGb) throws Exception;

    /**구분(개인/단체)로 주민번호 복호화*/
    String getDecRsdtNo(EgovMap map) throws Exception;
}
