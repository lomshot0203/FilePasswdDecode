package egovframework.com.ext.jfile.security.service;

public class JCryptoHelper {
    /** 디폴트로 제공할 ivBytes 를 초기화 한다. */
    public byte[] DEFAULT_IV_BYTES = { (byte) 5, (byte) 6, (byte) 7,
            (byte) 8, (byte) 9, (byte) 7, (byte) 1, (byte) 2 };

    /** 입력받은 알고리즘 중에 키 알고리즘에 해당하는 부분을 추출한다.*/
    public String getKeyAlgorithm(String algorithm) {
        return algorithm.split("\\/")[0];
    }

    /** 운용방식에 따른 ivBytes 필요여부를 검사한다. */
    public boolean isNecessaryIvBytes(String algorithm) {
        String[] algorithmArr = algorithm.split("\\/");

        if (algorithmArr.length == 1) {
            return false;
        } else if (algorithmArr.length == 2 || algorithmArr.length == 3) {
            if ("CBC".equalsIgnoreCase(algorithmArr[1])
                    || "OFB".equalsIgnoreCase(algorithmArr[1])
                    || "CFB".equalsIgnoreCase(algorithmArr[1])) {
                return true;
            } else {
                return false;
            }
        }
        return false;
    }
}
