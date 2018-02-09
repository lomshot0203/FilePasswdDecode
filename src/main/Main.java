package main;

import file.CustomFile;
import util.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    private static FileUtils util;

    public static void main(String[] args) {
        util = new FileUtils();
        HashMap<String, String> map = util.getProps("resource"); /*프로퍼티*/
        ArrayList<CustomFile> targetDir = new ArrayList<>(); /*업로드 루트 디렉토리*/

        File[] roots = util.getRootDirList();
        for (int idx=0; idx<roots.length; idx++) {
            util.getDirList(map.get("COM.ROOT.PATH"), map.get("COM.SOURCE.PATH"));
        }

        targetDir = util.getDirNameList();
        /*for (int idx=0; idx<targetDir.size(); idx++) {
            util.getFileList(targetDir.get(idx).getAbsolutePath());
        }*/
        util.getFileList(targetDir.get(0).getAbsolutePath());

        util.doFileRead(1); /*0:평문, 1:암호화 , 2:복호화*/
        util.doFileWrite(map.get("COM.ROOT.PATH"), map.get("COM.TARGET.PATH"), map.get("COM.SOURCE.PATH"));/*파일쓰기*/
    }

    public static void print() {
        for (CustomFile cf : util.getFileNameList()) {
            System.out.println(cf.getAbsolutePath());
        }
    }
}
