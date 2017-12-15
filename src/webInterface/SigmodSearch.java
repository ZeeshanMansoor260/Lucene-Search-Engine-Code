package webInterface;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queries.function.FunctionScoreQuery;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.DoubleValuesSource;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;


public class SigmodSearch {
	IndexReader reader;
	
	public List<String> searchRank(String search) throws IOException, ParseException, Exception {
		List<String> list = new ArrayList<String>();
		String index = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\PageRank\\Index";
		IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer);
		Query query = parser.parse(search);
		
		// Note that a Float field would work better.
		DoubleValuesSource boostByField = DoubleValuesSource.fromLongField("boost");

		// Create a query, based on the old query and the boost
		FunctionScoreQuery modifiedQuery = new FunctionScoreQuery(query, boostByField);
		
		
				
				//Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
		        Formatter formatter = new SimpleHTMLFormatter();
		         
		        //It scores text fragments by the number of unique query terms found
		        //Basically the matching score in layman terms
		        QueryScorer scorer = new QueryScorer(query);
		         
		        //used to markup highlighted terms found in the best sections of a text
		        Highlighter highlighter = new Highlighter(formatter, scorer);
		         
		        //It breaks text up into same-size texts but does not split up spans
		        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 100);
		         
		        //breaks text up into same-size fragments with no concerns over spotting sentence boundaries.
		        //Fragmenter fragmenter = new SimpleFragmenter(10);
		         
		        //set fragmenter to highlighter
		        highlighter.setTextFragmenter(fragmenter);
		        
		
		
		TopDocs results = searcher.search(modifiedQuery, 5);
		System.out.println(results.totalHits + " total matching documents");
		list.add("<h3>" + results.totalHits + " total matching documents </h3>");
		if(results.totalHits == 0) {
			return CheckSpelling(search,list);
		}
		for (int i = 0; i < 5; i++) {
			Document doc = searcher.doc(results.scoreDocs[i].doc);
			String fragsAbstract;
			@SuppressWarnings("deprecation")
			TokenStream streamAbstract = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "title", analyzer);
			fragsAbstract = highlighter.getBestFragments(streamAbstract, doc.get("title"), 10, "....");
//			System.out.println((i + 1) + "Title " + doc.get("title"));
			String title = doc.get("title");
			if (title != null) {
//				System.out.println("   Title: " + doc.get("contents"));
				list.add("<div class = \"w3-panel w3-border\"><p style=\"color:blue;\">" + i + "<br>");
				if(!fragsAbstract.equalsIgnoreCase("")) {
					System.out.println("   Title Highlighted: " + fragsAbstract);
					list.add((fragsAbstract +"</p>"+ 
							"<p>"+ doc.get("contents") + "</p><br></div>"));
				}
				else {
//					System.out.println("   Title : " + doc.get("title"));
					list.add((doc.get("title") +"</p>"+ 
							"<p>"+ doc.get("contents") + "</p><br></div>"));
				}
			}
		}
		reader.close();
		return list;
	}
	
	public List<String> CheckSpelling(String text, List<String> list) {
		System.out.println("Calling Spelling Checking->");
		CheckSpelling checkSpelling = new CheckSpelling();
		String spellResult = checkSpelling.spellingChecker(text);
		list.remove(0);
		list.add(0, spellResult);
		return list;
	}
}
