package com.auto.create.file.document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class DOCCreate {

    private static String classPath = null;

    private static String basePath = null;

    static {
        classPath = DOCCreate.class.getPackage().toString();
        classPath = classPath.substring(classPath.indexOf(' ') + 1);
        basePath = classPath.replace('.', '\\') + "\\" + DOCCreate.class.getSimpleName();
    }

    public static void main(String[] args) {
        try {
            File file = new File("c:\\users\\administrator\\desktop\\" + "接口文档.doc");
            if (!file.exists()) {
                file.createNewFile();
            }

            File projectDirectory = new File(
                    "E:\\ClassManagement\\ClassManagementSystemProject\\src\\main\\java\\com\\school\\management\\api\\controller\\");
            for (File f : projectDirectory.listFiles()) {
                Class content = readComment(f);
//                System.out.println(content.getName());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Class readComment(File f) throws ClassNotFoundException {
        String content = "";
        File byteFile = new File("..\\ByteFile.java");
        try {
            if (!byteFile.exists()) {
                byteFile.createNewFile();
            }
            if (f.canRead()) {
                FileInputStream is = new FileInputStream(f);
                FileOutputStream fileOutputStream = new FileOutputStream(byteFile);
                byte[] b = new byte[is.available()];
                int i = 0;
                int index = 0;
                while ((i = is.read()) != -1) {
                    b[index] = (byte) i;
                    index++;
                }
                content = new String(b);
                fileOutputStream.write(b);
                is.close();
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = byteFile.getName();
        System.out.println(byteFile.getAbsolutePath());
        return Class.forName(byteFile.getAbsolutePath());
    }
}
