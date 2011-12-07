package edu.uwsp.lucene;

import java.io.FileReader;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.analysis.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TermRangeFilter;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

public class LuceneMain {

	/**
	 * @param args
	 * @throws Exception 
	 */
	public static void main(String[] args) throws Exception{
		String indexDir = "data/";
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
		
		//System.out.println(doc.getFields().size());
		
		//create the index
		Directory d = new RAMDirectory();
		StandardAnalyzer anal = new StandardAnalyzer(Version.LUCENE_34);
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_34, null);
		IndexWriter iw = new IndexWriter(d, config);
		iw.addDocument(doc);
		iw.optimize();
		iw.close();
		
		IndexSearcher is = new IndexSearcher(d,true);
		Query query = new TermQuery(new Term("lastname", "Aboulnaga"));
        TopScoreDocCollector collector = TopScoreDocCollector.create(10,true);
        is.search(query,collector);
        ScoreDoc[] hits = collector.topDocs().scoreDocs;
        
        System.out.println("Found " + hits.length + " hits.");
        for(int i=0;i<hits.length;++i) {
          int docId = hits[i].doc;
          Document de = is.doc(docId);
          System.out.println((i + 1) + ". " + de.get("lastname"));
        }

        // searcher can only be closed when there
        // is no need to access the documents any more. 
        is.close();
        
	}
	
}
