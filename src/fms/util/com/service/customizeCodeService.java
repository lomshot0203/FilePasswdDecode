package fms.util.com.service;
import java.util.List;

/**
*작성자:민광진
*날짜:2015.05.04
 */
public interface customizeCodeService {
    List<?> getCodePair(String target, List<String> code) throws Exception; 
}
