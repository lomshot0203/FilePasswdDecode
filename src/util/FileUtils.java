package util;

import egovframework.com.ext.jfile.security.service.CipherService;
import egovframework.com.ext.jfile.security.service.CipherServiceImpl;
import egovframework.com.ext.jfile.security.service.JCryptoHelper;
import file.CustomFile;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.GenericXmlApplicationContext;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

import static oracle.net.aso.C05.e;

public class FileUtils {

    private ArrayList<CustomFile> fileNameList = new ArrayList<>();
    private ArrayList<CustomFile> dirNameList = new ArrayList<>();
    private ApplicationContext context;
    private CipherService cipher;
    private JCryptoHelper helper;


    public FileUtils () {
        fileNameList = new ArrayList<>();
        dirNameList = new ArrayList<>();
        context = new GenericXmlApplicationContext("classpath:context-jfile.xml");
        cipher = (CipherService) context.getBean("cipherService");
        helper = new JCryptoHelper();
    }

    public  ArrayList<CustomFile> getFileNameList() {
        return fileNameList;
    }
    public  ArrayList<CustomFile> getDirNameList() {
        return dirNameList;
    }

    /** 시스템의루트 디렉토리를 조회한다.*/
    public  File[] getRootDirList () {
        ArrayList<File> list = new ArrayList<>();
        File[] roots = File.listRoots();
        return roots;
    }

    /** 프로퍼티 내용을 가져온다.*/
    public  HashMap<String, String> getProps(String name) {
        HashMap<String, String> renMap = new HashMap<>();
        InputStream is = FileUtils.class.getClassLoader().getResourceAsStream(name+".properties");
        BufferedInputStream bs = new BufferedInputStream(is);
        Properties props = new Properties();
        try {
            props.load(bs);
            Enumeration<Object> propsKeys = props.keys();
            while (propsKeys.hasMoreElements()) {
                String key = propsKeys.nextElement().toString();
                renMap.put(key, props.getProperty(key));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return renMap;
    }

    /**파일 리스트를 반환한다.*/
    public  void getFileList(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        for (int i=0; i<fileList.length; i++) {
            if (fileList[i].isFile() && fileList[i].canRead()) {
                CustomFile cf = new CustomFile(fileList[i].getAbsolutePath());
                cf.setName(fileList[i].getName());
                cf.setPath(fileList[i].getAbsolutePath());
                fileNameList.add(cf);
            }

            if (fileList[i].isDirectory() && fileList[i].canRead()) {
                if (getSubFileList(fileList[i].getAbsolutePath()).length > 0) {
                    getFileList(fileList[i].getAbsolutePath());
                }
            }
        }
    }


    /**디렉토리 리스트를 반환한다.*/
    public  void getDirList(String root, String name) {
        File file = new File(root);
        File[] fileList = file.listFiles();
        for (int i=0; i<fileList.length; i++) {
            if (fileList[i].isDirectory() && fileList[i].canRead() && fileList[i].canWrite()) {
                if (name.equals(fileList[i].getName())) {
                    CustomFile cf = new CustomFile(fileList[i].getAbsolutePath());
                    cf.setName(fileList[i].getName());
                    cf.setPath(fileList[i].getAbsolutePath());
                    dirNameList.add(cf);
                }
                if (getSubFileList(fileList[i].getAbsolutePath()) != null && getSubFileList(fileList[i].getAbsolutePath()).length > 0) {
                    getDirList(fileList[i].getAbsolutePath(), name);
                }
            }
        }
    }

    /** 디렉토리 하위의 파일리스트를 반환한다.*/
    private  File[] getSubFileList(String path) {
        File file = new File(path);
        File[] fileList = file.listFiles();
        return fileList;
    }

    /** 파일을 읽는다.*/
    public void doFileRead(int gb) {
        for (int idx=0; idx<fileNameList.size(); idx++) {
            try (BufferedInputStream br = new BufferedInputStream(new FileInputStream(new File(fileNameList.get(idx).getAbsolutePath())))) {
                byte[] readBuffer = new byte[2048];
                while ((br.read(readBuffer, 0, readBuffer.length)) != -1) {
                    if (gb == 0) { /*평문*/
                        fileNameList.get(idx).setContent(readBuffer);
                    } else if (gb == 1) {/*암호화*/
                        fileNameList.get(idx).setContent(encrypt(readBuffer, helper));
                    } else if (gb == 2) {/*복호화*/
                        fileNameList.get(idx).setContent(decrypt(readBuffer, helper));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
            } catch (InvalidKeyException e1) {
                e1.printStackTrace();
            } catch (InvalidAlgorithmParameterException e1) {
                e1.printStackTrace();
            } catch (NoSuchPaddingException e1) {
                e1.printStackTrace();
            } catch (BadPaddingException e1) {
                e1.printStackTrace();
            } catch (InvalidKeySpecException e1) {
                e1.printStackTrace();
            } catch (IllegalBlockSizeException e1) {
                e1.printStackTrace();
            }
        }
    }

    /** 파일을 바이트로 쓴다*/
    public void doFileWrite(String root, String targetPath, String sourcePath) {

        for (CustomFile cf : fileNameList) {
            String path = cf.getParent().replaceAll(sourcePath, targetPath);
            File backDir = new File(path);
            if (!backDir.exists()) {
                backDir.mkdirs();
            }
            try(FileWriter fw = new FileWriter(path+File.separator+cf.getName())){
                for (byte[] b : cf.getContent()) {
                    fw.write(new String(b));
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /** 암호화 한다.*/
    public byte[] encrypt(byte[] data, JCryptoHelper helper) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        return cipher.encrypt(data, helper);
    }

    /** 복호화 한다.*/
    public byte[] decrypt(byte[] data, JCryptoHelper helper) throws NoSuchPaddingException, InvalidAlgorithmParameterException, NoSuchAlgorithmException, IOException, BadPaddingException, IllegalBlockSizeException, InvalidKeyException, InvalidKeySpecException {
        return cipher.decrypt(data, helper);
    }
}
