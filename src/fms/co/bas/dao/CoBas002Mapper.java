package fms.co.bas.dao;

import egovframework.rte.psl.dataaccess.mapper.Mapper;
import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;

/**
*작성자:민광진
*날짜:2015.04.30
 */
@Mapper
public interface CoBas002Mapper {
		/** 모계좌조회*/
	    public List<?> getMasterAccountList(EgovMap map) throws Exception;
		/** 은행+계좌 셀렉트박스 생성*/
	    public List<?> getBankPlusAccountSelectBox() throws Exception;
		/** 가상계좌조회*/
	    public List<?> getVirtualAccountList(EgovMap map) throws Exception;
	    /** 은행코드조회*/
	    public String getBanCd(String pntacnCd) throws Exception;
	    /**모계좌 저장*/
	    public void saveMasterAccount(EgovMap map) throws Exception;
	    /**가상계좌 저장*/
	    public void saveVirtualAccount(EgovMap map) throws Exception;
}
