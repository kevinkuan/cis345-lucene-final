package edu.uwsp.lucene;

import java.io.FileReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.document.Document;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class LuceneMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		String file = "department.xml";
		SAXParserFactory pfactory = SAXParserFactory.newInstance();
		pfactory.setValidating(false);
		pfactory.setNamespaceAware(true);
		SAXParser parser = pfactory.newSAXParser();
		XMLReader reader = parser.getXMLReader();
		LuceneSaxParser splitter = new LuceneSaxParser();
		reader.setContentHandler(splitter);
		reader.parse(new InputSource(new FileReader(file)));
		Document doc = splitter.getDoc();
		System.out.println(doc.getFields().size());
	}

}
