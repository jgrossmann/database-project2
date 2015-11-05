import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ResultParser {
	
	Document dom;
	
	
	public static List<String> parseResults(String s, int numResults){
	
	    if(numResults < 1) return new ArrayList<String>(0);
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		Document dom = null;
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
		    dom = db.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));
			
		}catch(ParserConfigurationException x){
			System.out.print("Parse error");
			x.printStackTrace();
		}catch(SAXException se){
			se.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
		
		//get the root element
		Element docEle = dom.getDocumentElement();

        List<String> urls = new ArrayList<String>(4);
		//get a nodelist of elements
		NodeList nl = docEle.getElementsByTagName("entry");
		if(nl != null && nl.getLength() > 0) {
			for(int i = 0 ; (i < nl.getLength()) && (i < 4);i++) {

				Element el = (Element)nl.item(i);
                urls.add(el.getElementsByTagName("d:Url").item(0).getTextContent());
   
			}
		}
		
		return urls;
	}
		
}

