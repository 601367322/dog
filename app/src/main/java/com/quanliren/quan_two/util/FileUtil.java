package com.quanliren.quan_two.util;

import java.io.File;

public class FileUtil {

    public static void deleteFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                deleteFile(f);
            }
            file.delete();
        }
    }

    public static void deleteFile(String file) {
        deleteFile(new File(file));
    }

    public static File getFile(File file, String name) {
        if (file.isFile()) {
            if (file.getName().equals(name)) {
                return file;
            }
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
            }
            for (File f : childFile) {
                File temp = getFile(f, name);
                if (temp != null) {
                    return temp;
                }
            }
        }
        return null;
    }
}
