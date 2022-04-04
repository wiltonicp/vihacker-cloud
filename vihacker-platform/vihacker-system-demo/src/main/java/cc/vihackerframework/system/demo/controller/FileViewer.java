package cc.vihackerframework.system.demo.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ranger on 2022/3/9
 */
public class FileViewer {

    public static void main(String[] args) {
        List<String> listFiles = getListFiles("/Users/mapeng/Desktop/main", "", true);
        for (int i = 0; i < listFiles.size(); i++) {
            System.out.println(listFiles.get(i));
        }
    }

    public static List<String> getListFiles(String path, String suffix,
                                            boolean isDepth) {
        List<String> lstFileNames = new ArrayList<String>();
        File file = new File(path);
        return FileViewer.listFile(lstFileNames, file, suffix, isDepth);
    }

    private static List<String> listFile(List<String> lstFileNames, File f,
                                         String suffix, boolean isDepth) {
        // 若是目录, 采用递归的方法遍历子目录
        if (f.isDirectory()) {
            File[] t = f.listFiles();
            lstFileNames.add(f.getAbsolutePath());
            for (int i = 0; i < t.length; i++) {
                if (isDepth || t[i].isFile()) {
                    listFile(lstFileNames, t[i], suffix, isDepth);
                }
            }
        } else {
            String filePath = f.getAbsolutePath();
            if (!suffix.equals("")) {
                int begIndex = filePath.lastIndexOf(".");
                String tempsuffix = "";

                if (begIndex != -1) {
                    tempsuffix = filePath.substring(begIndex + 1,
                            filePath.length());
                    if (tempsuffix.equals(suffix)) {
                        lstFileNames.add(filePath);
                    }
                }
            } else {
                lstFileNames.add(filePath);
            }
        }
        return lstFileNames;
    }
}
