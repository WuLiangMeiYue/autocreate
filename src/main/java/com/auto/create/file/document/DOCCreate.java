package com.auto.create.file.document;

import javassist.*;
import javassist.bytecode.ClassFile;
import javassist.bytecode.FieldInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Arrays;

public class DOCCreate {

    private static String basePath = null;

    private static final File DOCUMENT = new File("c:\\users\\administrator\\desktop\\" + "接口文档.doc");

    private static final Logger LOGGER = LoggerFactory.getLogger(DOCCreate.class);

    private static final ClassPool POOL = ClassPool.getDefault();

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
                    "E:\\ClassManagement\\ClassManagementSystemProject\\out\\production\\classes\\com\\school\\management\\api\\controller");
            for (File f : projectDirectory.listFiles()) {
                String content = readComment(f);
                break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String readComment(File f) throws NotFoundException {
        String content = "";
        try {
            if (f.canRead()) {
                FileInputStream is = new FileInputStream(f);
                readToClass(f.toURI().toURL().toString(), is);
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
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return content;
    }


    private static void readToClass(String pathURL, InputStream inputStream) throws IOException, NoSuchMethodException, ClassNotFoundException {
        CtClass ctClass = POOL.makeClass(inputStream);
        Object[] objects = ctClass.getAvailableAnnotations();
        StringBuilder url = new StringBuilder();
        for (int i = 0; i < objects.length; i++) {
            Annotation annotation = (Annotation) objects[i];
            if (annotation.annotationType().getSimpleName().contains("RequestMapping")){
                String annotationValue = annotation.toString();
                annotationValue = annotationValue.substring(annotationValue.indexOf("value"), (annotationValue.length())-1);
                annotationValue = annotationValue.substring(annotationValue.indexOf("{\"")+2, (annotationValue.length())-2);
                url.append(annotationValue);
            }
        }
        for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
            System.out.println(ctMethod.getAnnotations());
        }
    }
}
