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
        HashMap<String, String> map = new HashMap<>(); /*프로퍼티*/
        ArrayList<CustomFile> targetDir = new ArrayList<>(); /*업로드 루트 디렉토리*/

        File[] roots = util.getRootDirList();
        for (int idx=0; idx<roots.length; idx++) {
//            util.getDirList(roots[0].getAbsolutePath(), util.getProps("resource").get("COM.UPLOAD.PATH"));
            util.getDirList(util.getProps("resource").get("COM.UPLOAD.ROOT.PATH"), util.getProps("resource").get("COM.UPLOAD.PATH"));
        }

        targetDir = util.getDirNameList();
        for (int idx=0; idx<targetDir.size(); idx++) {
            util.getFileList(targetDir.get(idx).getAbsolutePath());
        }

        util.doFileEncrypt(); /*암호화*/
    }

    public static void print() {
        for (CustomFile cf : util.getFileNameList()) {
            System.out.println(cf.getAbsolutePath());
        }
    }
}
