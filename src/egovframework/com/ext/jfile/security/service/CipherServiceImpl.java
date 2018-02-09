/*
 * eGovFrame JFile
 * Copyright The eGovFrame Open Community (http://open.egovframe.go.kr)).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * @author 정호열 커미터(표준프레임워크 오픈커뮤니티 리더)
 */
package egovframework.com.ext.jfile.security.service;

import egovframework.com.ext.jfile.security.JCrypto;
import org.apache.commons.codec.binary.Base64;

import javax.crypto.*;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;

/**
 *  클래스
 * @author 정호열
 * @since 2010.10.17
 * @version 1.0
 * @see
 *
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일        수정자       수정내용
 *  -------       --------    ---------------------------
 *   2010.10.17   정호열       최초 생성
 *   2013.12.19	표준프레임워크	공통컴포넌트 추가 적용 (패키지 변경 및 sun.misc 패키지 사용 제외)
 *
 * </pre>
 */
public class CipherServiceImpl implements CipherService {
	
	/**
	 * 암호화 정보객체
	 */
	private JCrypto jcrypto;

	/**
	 * 암호화 정보객체를 전달받는다..
	 * @param jcrypto 암호화 정보객체
	 */
	public void setJcrypto(JCrypto jcrypto) {
		this.jcrypto = jcrypto;
	}

	/**
	 * 암호화 객체를 얻는다.
	 * @return Cipher 암호화 객체.
	 * @throws NoSuchAlgorithmException 알고리즘을 찾을 수 없을때 예외 처리.
	 * @throws NoSuchPaddingException 패딩 정보를 찾을 수 없을때 예외 처리.
	 */
	private Cipher getCipherInstance() throws NoSuchAlgorithmException,
			NoSuchPaddingException {
		Cipher cipher = Cipher.getInstance(this.jcrypto.getAlgorithm());
		return cipher;
	}

	/**
	 * 암호화 키를 생성한다.
	 * @param keyAlgorithm 키 알고리즘.
	 * @param algorithm 알고리즘.
	 * @param keyData 키 데이타.
	 * @return Key 암호화 키.
	 * @throws NoSuchAlgorithmException 알고리즘 정보를 찾을 수 없을때 예외 처리.
	 * @throws InvalidKeyException 유효하지 않은 key 일때 예외 처리
	 * @throws InvalidKeySpecException 유효하지 않은 keySpec 일때 예외 처리
	 */
	private static Key generateKey(String keyAlgorithm, String algorithm,
			byte[] keyData) throws NoSuchAlgorithmException,
			InvalidKeyException, InvalidKeySpecException {
		if (keyAlgorithm == null || "".equals(keyAlgorithm))
			throw new NoSuchAlgorithmException("algorithm is nessary");
		String upper = keyAlgorithm.toUpperCase();
		if ("DES".equals(upper)) {
			KeySpec keySpec = new DESKeySpec(keyData);
			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(keyAlgorithm);

			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;

		} else if (upper.indexOf("DESEDE") > -1
				|| upper.indexOf("TRIPLEDES") > -1) {
			KeySpec keySpec = new DESedeKeySpec(keyData);

			SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance(keyAlgorithm);
			SecretKey secretKey = secretKeyFactory.generateSecret(keySpec);
			return secretKey;
		} else {
			SecretKeySpec keySpec = new SecretKeySpec(keyData, keyAlgorithm);
			return keySpec;
		}
	}

	public byte[] encrypt(byte[] data, JCryptoHelper helper) throws NoSuchAlgorithmException,
			InvalidKeyException, NoSuchPaddingException, IOException,
			InvalidKeySpecException, IllegalBlockSizeException,
			BadPaddingException, InvalidAlgorithmParameterException {
		Cipher cipher = getCipherInstance();

		if (helper.isNecessaryIvBytes(this.jcrypto.getAlgorithm())) {
			IvParameterSpec ivParameterSpec = new IvParameterSpec(helper.DEFAULT_IV_BYTES);
			cipher.init(Cipher.ENCRYPT_MODE,generateKey(
					helper.getKeyAlgorithm(this.jcrypto.getAlgorithm()),this.jcrypto.getAlgorithm(), this.jcrypto.getKeyBytes()), ivParameterSpec);
		} else {
			cipher.init(Cipher.ENCRYPT_MODE,generateKey(
					helper.getKeyAlgorithm(this.jcrypto.getAlgorithm()),this.jcrypto.getAlgorithm(), this.jcrypto.getKeyBytes()));
		}
		if (jcrypto.isApplyBase64()) {
			return Base64.encodeBase64(cipher.doFinal(data));
			
		} else {
			return cipher.doFinal(data);
		}
	}

	public byte[] decrypt(byte[] data, JCryptoHelper helper) throws InvalidKeyException,
			NoSuchAlgorithmException, InvalidKeySpecException,
			NoSuchPaddingException, IllegalBlockSizeException,
			BadPaddingException, IOException,
			InvalidAlgorithmParameterException {
		Cipher cipher = getCipherInstance();
		if (helper.isNecessaryIvBytes(this.jcrypto.getAlgorithm())) {
			IvParameterSpec ivParameterSpec = new IvParameterSpec(helper.DEFAULT_IV_BYTES);
			cipher.init(
					Cipher.DECRYPT_MODE, 
					generateKey(helper.getKeyAlgorithm(this.jcrypto.getAlgorithm()),this.jcrypto.getAlgorithm(), this.jcrypto.getKeyBytes()), ivParameterSpec);
		} else {
			cipher.init(
					Cipher.DECRYPT_MODE,
					generateKey(helper.getKeyAlgorithm(this.jcrypto.getAlgorithm()),this.jcrypto.getAlgorithm(), this.jcrypto.getKeyBytes()));
		}

		byte[] bData = null;
		if (jcrypto.isApplyBase64()) {

			//byte[] temp = new sun.misc.BASE64Decoder().decodeBuffer(new String(	data));
			byte[] temp = Base64.decodeBase64(data);
			bData = temp;
		} else {
			bData = data;
		}

		return cipher.doFinal(bData);

	}


}
