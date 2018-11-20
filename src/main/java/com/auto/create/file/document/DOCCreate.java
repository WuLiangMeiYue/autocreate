package com.auto.create.file.document;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class DOCCreate {

    private static String basePath = null;

    private static final File DOCUMENT = new File("c:\\users\\administrator\\desktop\\" + "接口文档.doc");

    private static final Logger LOGGER = LoggerFactory.getLogger(DOCCreate.class);

    static {
        try {
            basePath = new File("").getAbsolutePath() + "\\src\\main\\java\\com\\auto\\create\\file\\document\\";
            if (!DOCUMENT.exists()) {
                DOCUMENT.createNewFile();
            }
            LOGGER.info("自动生成接口文档模块初始化完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
            File projectDirectory = new File(
                    "E:\\ClassManagement\\ClassManagementSystemProject\\src\\main\\java\\com\\school\\management\\api\\controller\\");
            for (File f : projectDirectory.listFiles()) {
                readToClass(f.getName(), readComment(f));
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readComment(File f) throws ClassNotFoundException {
        String content = "";
        try {
            if (f.canRead()) {
                FileInputStream is = new FileInputStream(f);
                byte[] b = new byte[is.available()];
                int i = 0;
                int index = 0;
                while ((i = is.read()) != -1) {
                    b[index] = (byte) i;
                    index++;
                }
                content = new String(b);
                is.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    private static void readToClass(String className, String classPath) throws IOException, ClassNotFoundException {
//        JavaStringCompiler
    }

}
