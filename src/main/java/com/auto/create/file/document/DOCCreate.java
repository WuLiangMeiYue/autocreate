package com.auto.create.file.document;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOCCreate {

    private static final File DOCUMENT = new File("c:\\users\\administrator\\desktop\\" + "接口文档.doc");

    private static final Logger LOGGER = LoggerFactory.getLogger(DOCCreate.class);

    private static final ClassPool POOL = ClassPool.getDefault();

    static {
        try {
            if (!DOCUMENT.exists()) {
                if (DOCUMENT.createNewFile()) {
                    LOGGER.info("自动生成接口文档模块初始化完成");
                }
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {
            File projectDirectory = new File("E:\\ClassManagement\\ClassManagementSystemProject\\out\\production\\classes\\com\\school\\management\\api\\controller");
            File[] fileList = projectDirectory.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (!f.getName().contains("$")) {
                        List<Map<String, Object>> content = readComment(f);
                        System.out.println(content);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static List<Map<String, Object>> readComment(File f) throws IOException, ClassNotFoundException {
        List<Map<String, Object>> content = null;
        if (f.canRead()) {
            content = readToClass(new FileInputStream(f));
        }
        return content;
    }

    private static List<Map<String, Object>> readToClass(InputStream inputStream) throws IOException, ClassNotFoundException {
        CtClass ctClass = POOL.makeClass(inputStream);
        Object[] objects = ctClass.getAvailableAnnotations();
        StringBuilder url = new StringBuilder();
        List<Map<String, Object>> urlAsString = new ArrayList<Map<String, Object>>();
        for (Object object : objects) {
            Annotation annotation = (Annotation) object;
            if (annotation.annotationType().getSimpleName().contains("RequestMapping")) {
                String annotationValue = annotation.toString();
                annotationValue = annotationValue.substring(annotationValue.indexOf("value"), (annotationValue.length()) - 1);
                annotationValue = annotationValue.substring(annotationValue.indexOf("{\"") + 2, (annotationValue.length()) - 2);
                url.append(annotationValue);
            }
        }
        for (CtMethod ctMethod : ctClass.getDeclaredMethods()) {
            Object[] objectsList = ctMethod.getAnnotations();
            for (Object anObjectsList : objectsList) {
                Annotation annotation = (Annotation) anObjectsList;
                if (annotation.annotationType().getSimpleName().contains("tMapping")) {
                    Map<String, Object> map = new HashMap<String, Object>();
                    String name = annotation.annotationType().getSimpleName();
                    String annotationValue = annotation.toString();
                    annotationValue = annotationValue.substring(annotationValue.indexOf("value"), (annotationValue.length()) - 1);
                    annotationValue = annotationValue.substring(annotationValue.indexOf("{\"") + 2, (annotationValue.length()) - 2);
                    map.put(name.substring(0, name.indexOf("Mapping")), url + annotationValue);
                    urlAsString.add(map);
                }
            }
        }
        return urlAsString;
    }

    private static void writeIntoDocument(String content) {
//        XWPFDocument document = new ();
    }
}
