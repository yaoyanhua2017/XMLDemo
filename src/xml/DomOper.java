package xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class DomOper{

    public static void main(String[] args) {
       /* try {
            parse();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
        try {
            delete();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TransformerException e) {
            e.printStackTrace();
        }
    }
    public static void delete() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = getDocument();
        // 删除id为003，Element有一个getAttribute(),Element可以Node转换
        // 可以根据标签名获取对应的标签
        NodeList list = doc.getElementsByTagName("book");
        for (int i = 0; i < list.getLength(); i++) {
            // element---book---books
            Element element = (Element) list.item(i);
            System.out.println("id=" + element.getAttribute("id"));
            if ("bk102".equals(element.getAttribute("id"))) {
                // 删除了，xml内容重写入的xml
                element.getParentNode().removeChild(element);
            }
        }
        genernateTree(doc);
    }

    private static Document getDocument() throws ParserConfigurationException, SAXException, IOException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document doc = builder.parse("src/book.xml");
        return doc;
    }

    // 增加节点
    public static void add() throws ParserConfigurationException, SAXException, IOException, TransformerException {
        Document doc = getDocument();
        // 增加bk102这本书
        Element book = doc.createElement("book");
        // 增加属性
        book.setAttribute("id", "bk102");
        Element title = doc.createElement("title");
        title.setTextContent("javaweb");
        Element author = doc.createElement("author");
        author.setTextContent("张三");
        Element price = doc.createElement("price");
        price.setTextContent("20元");
        // <book><title></title></book>
        book.appendChild(title);
        book.appendChild(author);
        book.appendChild(price);
        // 把新增加的book挂books
        NodeList list = doc.getElementsByTagName("books");
        list.item(0).appendChild(book);
        // 把dom树转为xml文件
        genernateTree(doc);
        System.out.println("完成节点添加！！！");
    }

    private static void genernateTree(Document doc) throws TransformerFactoryConfigurationError,
            TransformerConfigurationException, FileNotFoundException, TransformerException {
        TransformerFactory tf = TransformerFactory.newInstance();
        Transformer tform = tf.newTransformer();
        tform.setOutputProperty(OutputKeys.ENCODING, "utf-8");
        DOMSource source = new DOMSource(doc);
        // 写入book.xml文件
        StreamResult result = new StreamResult(new FileOutputStream("src/book.xml"));
        tform.transform(source, result);
    }

    // 解析
    private static void parse() throws ParserConfigurationException, SAXException, IOException {
        // 得到解析器工厂类对象
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        // 得到文档实例
        DocumentBuilder builder = factory.newDocumentBuilder();
        String name = "src/book.xml";
        Document doc = builder.parse(name);
        System.out.println(doc);
        //没有办法直接获取根节点
       // NodeList root= doc.getElementsByTagName("books");
        NodeList list = doc.getChildNodes();
        System.out.println("文档有几个子节点：" + list.getLength() + ",节点名字：" + list.item(0).getNodeName());
        // 遍历books子节点
        NodeList books = list.item(0).getChildNodes();
        System.out.println("books下面有" + books.getLength() + "节点");
        for (int i = 0; i < books.getLength(); i++) {
            System.out.println("books子节点的名字：" + books.item(i).getNodeName());
            Node book = books.item(i);// 去除空白节点
            System.out.println("得到书的标签：" + book.getNodeName());
            // 拿到title/author
            NodeList singlebook = book.getChildNodes();

            for (int j = 0; j < singlebook.getLength(); j++) {
                Node node = singlebook.item(j);
                // Element element=(Element)singlebook.item(j);
                // System.out.println("得到bookid属性："+element.getAttribute("id"));
                String nodename = node.getNodeName();
                String content = node.getTextContent();
                // node.get
                if ("title".equals(nodename)) {
                    System.out.println("书名：" + content);
                } else if ("author".equals(nodename)) {
                    System.out.println("作者：" + content);
                } else if ("price".equals(nodename)) {
                    System.out.println("价格：" + content);
                }

            }
        }
    }
}
