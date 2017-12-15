package webInterface;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Explanation;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Formatter;
import org.apache.lucene.search.highlight.Fragmenter;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.search.highlight.SimpleSpanFragmenter;
import org.apache.lucene.search.highlight.TokenSources;
import org.apache.lucene.store.FSDirectory;

public class SearcherCode {
	
	IndexReader reader;
	public List<String> search(String text){
		List<String> list = new ArrayList<String>();
		
		String index = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\Index";
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		}
		catch(Exception e) {
			list.add("Index File Not Found");
		}
		long startTime = System.currentTimeMillis();
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		QueryParser parser = new QueryParser("contents", analyzer);
		try {
			Query query = parser.parse(text);
			
			/** Highlighter Code Start ****/
	         
	        //Uses HTML &lt;B&gt;&lt;/B&gt; tag to highlight the searched terms
	        Formatter formatter = new SimpleHTMLFormatter();
	         
	        //It scores text fragments by the number of unique query terms found
	        //Basically the matching score in layman terms
	        QueryScorer scorer = new QueryScorer(query);
	         
	        //used to markup highlighted terms found in the best sections of a text
	        Highlighter highlighter = new Highlighter(formatter, scorer);
	         
	        //It breaks text up into same-size texts but does not split up spans
	        Fragmenter fragmenter = new SimpleSpanFragmenter(scorer, 10);
	         
	        //breaks text up into same-size fragments with no concerns over spotting sentence boundaries.
	        //Fragmenter fragmenter = new SimpleFragmenter(10);
	         
	        //set fragmenter to highlighter
	        highlighter.setTextFragmenter(fragmenter);
			
			
			
			TopDocs results = searcher.search(query, 5);
			long endTime   = System.currentTimeMillis();
			float totalTime = (endTime - startTime) / 100;
			System.out.println(results.totalHits + " total matching documents");
			list.add("<h1> You Searched: " + text + "</h1>");
			list.add("Time Taken: " + totalTime  + " seconds <br>");
			list.add(results.totalHits + " total matching documents <br>");
			for (int i = 0; i < 5; i++) {
				System.out.println("----------");
				Explanation explanation = searcher.explain(query, results.scoreDocs[i].doc);
				//TokenStream stream = TokenSources.getAnyTokenStream(reader, results.scoreDocs[i].doc, "dataHighlight", analyzer);
				@SuppressWarnings("deprecation")
				TokenStream stream = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "title", analyzer);
				
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				
				//Get stored text from found document
				String dataContent = doc.get("title");
				//Get highlighted text fragments
	            String[] frags = highlighter.getBestFragments(stream, dataContent, 10);
				String path = doc.get("path");
				System.out.println((i + 1) + ". " + path);
				String highlightedTitle = null;
				//System.out.println("FRAGS--------->" + frags.length);
				if(frags.length !=0) {
					highlightedTitle = "";
				}
				for (String frag : frags)
	            {
	                System.out.println("=======================");
	                System.out.println("Inside for loop frag: " + frag);
	                highlightedTitle += frag;
	            }
				String title = doc.get("title");
				if (title != null) {
					System.out.println("   Title: " + dataContent
							);
					//list.add((i+1) + "-> "  + path + "<br>" + dataContent + "<br>");
					if(highlightedTitle != null) {
						list.add((i+1) + "-> <a href = "  + path + ">" + path +"</a><br>" + "Title -> " + highlightedTitle + 
								"<br>Score ->" + explanation.getValue() +"<br><br>");
					}
					else {
						list.add((i+1) + "-> <a href = "  + path + ">" + path +"</a><br>" + "Title ->" + dataContent + 
								"<br>Score ->" + explanation.getValue() +"<br><br>");
					}
					
					
				}
				Explanation [] explain = explanation.getDetails();
				for(int x = 0; x < explain.length; x++) {
					System.out.println("explain[" + x + "]--> " + explain[x]);
				}
				System.out.println("Exp: " + explanation.getDescription() + "Exp Value ->" + explanation.getValue());
				
			}
			reader.close();
		}
		catch(Exception e) {
			System.out.println("Calling Spelling Checking");
			CheckSpelling checkSpelling = new CheckSpelling();
			String spellResult = checkSpelling.spellingChecker(text);
			list.remove(0);
			list.add(0, spellResult);
			//list.add("No matching documents found");
		}
		
		
		
		
		return list;
		
	}

}
