package com.auto.create.file.document;

import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.hwpf.usermodel.Range;
import org.apache.poi.hwpf.usermodel.Section;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.annotation.Annotation;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DOCCreate {

    private static final File DOCUMENT = new File("E:\\开发文档相关\\" + "接口文档.doc");

    private static final Logger LOGGER = LoggerFactory.getLogger(DOCCreate.class);

    private static final ClassPool POOL = ClassPool.getDefault();

    static {
        OutputStream outputStream = null;
        try {
            if (!DOCUMENT.exists()) {
                if (DOCUMENT.createNewFile()) {
                    String template = "1、${controller}\n" +
                            "1.1、接口说明：${title}\n" +
                            "1.2、接口地址：${url}\n" +
                            "1.3、请求方式：${method}\n" +
                            "1.4、接收参数：${paramTable}\n" +
                            "1.5、返回参数：${returnTable}\n";
                    outputStream = new FileOutputStream(DOCUMENT);
                    outputStream.write(template.getBytes(Charset.defaultCharset()));
                    outputStream.flush();
                    LOGGER.info("自动生成接口文档模块初始化完成");
                }
            }
        } catch (IOException e) {
            LOGGER.warn(e.getMessage());
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    LOGGER.warn(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        try {
            File projectDirectory = new File("E:\\ClassManagement\\ClassManagementSystemProject\\out\\production\\classes\\com\\school\\management\\api\\controller");
            File[] fileList = projectDirectory.listFiles();
            if (fileList != null) {
                for (File f : fileList) {
                    if (!f.getName().contains("$")) {
                        writeIntoDocument(readComment(f));
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

    private static void writeIntoDocument(List<Map<String, Object>> content) throws IOException {
        InputStream inputStream = new FileInputStream(DOCUMENT);
        WordExtractor extractor = new WordExtractor(inputStream);
//        HWPFDocument document = new HWPFDocument(inputStream);
//        Range range = document.getRange();
//        System.out.println(range.text());

    }
}
