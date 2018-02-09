package fms.co.sys.service;

import egovframework.rte.psl.dataaccess.util.EgovMap;

import java.util.List;
import java.util.Map;

/**
 * @Class Name : CoSys013Controller.java
 * @Description : CoSys013 Controller class
 * @Modification Information
 *
 * @author c
 * @since 2015.07.13
 * @version 1.0
 * @see
 *  
 */
public interface CoSys013Service {
	List<?> listCoSys013(EgovMap map) throws Exception;
	Map saveCoSys013(EgovMap map) throws Exception;
	Map updateCoSys013(EgovMap map) throws Exception;
}

