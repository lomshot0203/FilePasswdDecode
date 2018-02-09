package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.13
 */
@Mapper
public interface CoBas004Mapper {
		/**주민번호 복호화*/
	    public String getDecRRN(String encRRN) throws Exception;
		/**고객정보 조회*/
	    public List<?> getCustomerInformation(EgovMap map) throws Exception;
		/**고객상세 조회*/
	    public List<?> getMoreInformation(EgovMap map) throws Exception;
		/**고객변경 조회*/
	    public List<?> getChangeInformation(EgovMap map) throws Exception;
	    /**고객정보 저장*/
	    public void saveCustomerInformation(EgovMap map) throws Exception;
	    /**고객상세 저장*/
	    public void saveMoreInformation(EgovMap map) throws Exception;
	    /**고객변경 저장*/
	    public void saveChangeInformation(EgovMap map) throws Exception;
	    /**계약번호를 이용하여 계약구분을 조회*/
	    public String getCtrGb(String ctrGb) throws Exception;
	    /**고객 계좌정보 조회*/
	    public List<?> getBanAcnInformation(EgovMap map) throws Exception;
	    /**고객 계좌정보 저장*/
	    public void saveBanAcnInformation(EgovMap map) throws Exception;
	    /**고객 계좌정보 수정*/
	    public void updateBanAcnInformation(EgovMap map) throws Exception;
	    /**고객 계좌정보 삭제*/
	    public void deleteBanAcnInformation(EgovMap map) throws Exception;
	    /**담당자 정보 조회*/
	    public List<?> getCgpInformation(EgovMap map) throws Exception;
	    /**담당자 정보 저장*/
	    public void saveCgpInformation(EgovMap map) throws Exception;
	    /**담당자 정보 수정*/
	    public void updateCgpInformation(EgovMap map) throws Exception;
	    /**담당자 정보 삭제*/
	    public void deleteCgpInformation(EgovMap map) throws Exception;
	    /**구분(개인/단체)로 주민번호 복호화*/
	    public String getDecRsdtNo(EgovMap map) throws Exception;
}
