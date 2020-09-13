package xml;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.junit.Test;
import pojo.Student;


import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParseBean {
    @org.junit.Test
    public void delete() throws DocumentException, IOException {
        Document document = getDocument();
        Element root = document.getRootElement();
        List<Element> stus = root.elements("student");//获取根元素的子元素
        for (Object obj : stus) {
            Element element = (Element) obj;
            //删除id=3学生
            if ("2".equals(element.attributeValue("id"))) {
                element.getParent().remove(element);
                //root.remove(element);
            }
        }
        generateDocument(document);
    }

    @Test
    public void add() throws DocumentException, IOException {
        Document document = getDocument();
        Element stu = DocumentHelper.createElement("student");
        Element name = DocumentHelper.createElement("name");
        name.addText("李四");
        Element course = DocumentHelper.createElement("course");
        course.addText("java web");
        Element score = DocumentHelper.createElement("score");
        score.addText("88");
        stu.add(name);
        stu.add(course);
        stu.add(score);
        stu.addAttribute("id", "3");
        Element root = document.getRootElement();
        root.add(stu);//把新加的节点加入到根元素
        //写入文件
        generateDocument(document);
    }

    private Document getDocument() throws DocumentException {
        SAXReader reader = new SAXReader();
        File f = new File("src/scores.xml");
        return reader.read(f);
    }

    private void generateDocument(Document document)
            throws UnsupportedEncodingException, FileNotFoundException, IOException {
        OutputFormat format = OutputFormat.createPrettyPrint();
        // 设置文档的编码方式
        format.setEncoding("utf-8");
        // 树更新了，把更新的内容写入到xml文档中
        XMLWriter writer = new XMLWriter(new OutputStreamWriter(new FileOutputStream("src/scores.xml"), "utf-8"),
                format);
        writer.write(document);
        writer.close();
    }
    @Test
    public void read() throws DocumentException {
        //构造解析器
        Document document = getDocument();
        //获取根节点scores
        Element root = document.getRootElement();
        Iterator<Element> iterator = root.elementIterator("student");
        List<Student> stus = new ArrayList<>();
        //遍历有没有下一条
        while (iterator.hasNext()) {
            Element stuElement = iterator.next();
            Student student = new Student();
            student.setId(Integer.parseInt(stuElement.attributeValue("id")));
            student.setName(stuElement.elementText("name"));
            student.setCourse(stuElement.elementText("course"));
            student.setScore(Integer.parseInt(stuElement.elementText("score")));
            stus.add(student);
        }
        System.out.println(stus);

    }
}
