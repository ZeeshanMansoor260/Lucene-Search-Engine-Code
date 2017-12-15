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
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
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

public class XMLSearcher {
	IndexReader reader;
	public List<String> search(String text, String option){
		List<String> list = new ArrayList<String>();
		
//		String index = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\IndexXML2";
		String index = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\newIndex\\Index";
		try {
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
		}
		catch(Exception e) {
			list.add("Index File Not Found");
		}
		long startTime = System.currentTimeMillis();
		IndexSearcher searcher = new IndexSearcher(reader);
		Analyzer analyzer = new StandardAnalyzer();
		Query query;
		
		
		try {
			if(option.equalsIgnoreCase("all")) {
				MultiFieldQueryParser parser = new MultiFieldQueryParser(new String[]{"title", "abstract"},analyzer);
				
					query = parser.parse(text);
				
			}
			else if(option.equalsIgnoreCase("authors")){
				QueryParser parser = new QueryParser("authors", analyzer);
				
					query = parser.parse(text);
				
			}
			else if(option.equalsIgnoreCase("year")) {
				QueryParser parser = new QueryParser("year", analyzer);
				
					query = parser.parse(text);
				
			}
			else {
				QueryParser parser = new QueryParser("venue", analyzer);
				
				query = parser.parse(text);
			}
			
			/** Highlighter Code Start ****/
	         
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
			
			
			
			TopDocs results = searcher.search(query, 5);
			long endTime   = System.currentTimeMillis();
			float totalTime = (endTime - startTime) / 1000;
			System.out.println(results.totalHits + " total matching documents");
			list.add("<h2> You Searched: " + text + "</h2>");
//			list.add("<h3> Time Taken: " + totalTime  + " seconds </h3>");
			list.add("<h3>" + results.totalHits + " total matching documents </h3>");
			if(results.totalHits == 0) {
				return CheckSpelling(text,list);
			}
			for (int i = 0; i < results.totalHits; i++) {
				System.out.println("----------");
				Explanation explanation = searcher.explain(query, results.scoreDocs[i].doc);
				//TokenStream stream = TokenSources.getAnyTokenStream(reader, results.scoreDocs[i].doc, "dataHighlight", analyzer);
				//@SuppressWarnings("deprecation")
				//TokenStream streamTitle = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "title", analyzer);
				
				
				Document doc = searcher.doc(results.scoreDocs[i].doc);
				
				//Get stored text from found document
				String dataContent = doc.get("title");
				//Get highlighted text fragments
	            
				String abstract1 = doc.get("abstract");
				String year = doc.get("year");
				String authors = doc.get("authors");
				String venue = doc.get("venue");
//				System.out.println("------------");
//				System.out.println("Title: " + dataContent);
//				System.out.println("Abstract: " + abstract1);
//				System.out.println("Year: " + year);
//				System.out.println("Authors: " + authors);
//				String[] frags = highlighter.getBestFragments(streamTitle, dataContent, 10);
				String fragsAbstract;
				if(option.equalsIgnoreCase("all")) {
					@SuppressWarnings("deprecation")
					TokenStream streamAbstract = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "abstract", analyzer);
					fragsAbstract = highlighter.getBestFragments(streamAbstract, abstract1, 10, "....");
					String docId = doc.get("docId");
					if (dataContent != null) {
						list.add("<div class = \"w3-panel w3-border\"><p style=\"color:blue;\">" + docId + "<br>");
						if(!fragsAbstract.equalsIgnoreCase("")) {
							list.add((i+1) +"-> " + dataContent +"</p>"+ 
									"<p>"+ fragsAbstract +"<br><div class =\"w3-text-deep-orange\">" + year + " &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() + "</p></div>");
						}
						else {
							list.add((i+1) + "-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-deep-orange\">" + year + " &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() +  "</p></div>");
						}
					}
					
				}
				else if(option.equalsIgnoreCase("authors")){
					@SuppressWarnings("deprecation")
					TokenStream streamAbstract = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "authors", analyzer);
					fragsAbstract = highlighter.getBestFragments(streamAbstract, authors, 10, "....");
					String docId = doc.get("docId");
					if (dataContent != null) {
						list.add("<div class = \"w3-panel w3-border\"><p style=\"color:blue;\">" + docId + "<br>");
						if(!fragsAbstract.equalsIgnoreCase("")) {
							list.add((i+1) +"-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\">" + year + " &nbsp <b>" + fragsAbstract + "</b> &nbsp " + venue + "</div><br>Score: " + explanation.getValue() +  "</p></div>");
						}
						else {
							list.add((i+1) + "-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\">" + year + " &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() +  "</p></div>");
						}
					}
					
				}
				else if(option.equalsIgnoreCase("year")){
					@SuppressWarnings("deprecation")
					TokenStream streamAbstract = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "year", analyzer);
					fragsAbstract = highlighter.getBestFragments(streamAbstract, year, 10, "....");
					String docId = doc.get("docId");
					if (dataContent != null) {
						list.add("<div class = \"w3-panel w3-border\"><p style=\"color:blue;\">" + docId + "<br>");
						if(!fragsAbstract.equalsIgnoreCase("")) {
							list.add((i+1) +"-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\"> <b>" + fragsAbstract + "</b> &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() +  "</p></div>");
						}
						else {
							list.add((i+1) + "-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\">" + year + " &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() + "</p></div>");
						}
					}
					
					
				}
				else {
					@SuppressWarnings("deprecation")
					TokenStream streamAbstract = TokenSources.getTokenStream(reader, results.scoreDocs[i].doc, "venue", analyzer);
					fragsAbstract = highlighter.getBestFragments(streamAbstract, venue, 10, "....");
					String docId = doc.get("docId");
					if (dataContent != null) {
						list.add("<div class = \"w3-panel w3-border\"><p style=\"color:blue;\">" + docId + "<br>");
						if(!fragsAbstract.equalsIgnoreCase("")) {
							list.add((i+1) +"-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\">" + year + " &nbsp " + authors + " &nbsp <b>" + fragsAbstract + "</b></div><br>Score: " + explanation.getValue() + "</p></div>");
						}
						else {
							list.add((i+1) + "-> " + dataContent +"</p>"+ 
									"<p>"+ abstract1 +"<br><div class =\"w3-text-blue\">" + year + " &nbsp " + authors + " &nbsp " + venue + "</div><br>Score: " + explanation.getValue() +  "</p></div>");
						}
					}
					
				}
				String highlightedTitle = null;
				String highlightedAbstract = null;
				//System.out.println("FRAGS--------->" + frags.length);
				if(!fragsAbstract.equalsIgnoreCase(""))
					System.out.println("FRAGSABS--------->" + fragsAbstract);
//				if(frags.length !=0) {
//					highlightedTitle = "";
//				}
//				for (String frag : frags)
//	            {
////	                System.out.println("=======================");
////	                System.out.println("Inside for loop frag: " + frag);
//	                highlightedTitle += frag;
//	            }
				
//				if(fragsAbstract.length !=0) {
//					highlightedAbstract = "";
//				}
//				for (String frag : fragsAbstract)
//	            {
////	                System.out.println("=======================");
////	                System.out.println("Inside for loop frag: " + frag);
//	                highlightedAbstract += frag;
//	            }
//				String docId = doc.get("docId");
//				if (dataContent != null) {
////					System.out.println("   Title: " + dataContent);
//					list.add("<p style=\"color:blue;\">" + docId + "<br>");
////					list.add((i+1) + "-> "  + dataContent +"<br>"+ 
////							"<p>Abstract: "+ abstract1 +"</p><br>" + "year: " + year + "  authors: " + authors + "<br><br>");
////					if(highlightedTitle != null && highlightedAbstract != null) {
////						System.out.println("Highlighted Title-> " + highlightedTitle + "\n HAbstract" + highlightedAbstract);
//////						list.add((i+1) + "-> <a href = "  + path + ">" + path +"</a><br>" + "Title -> " + highlightedTitle + 
//////								"<br>Score ->" + explanation.getValue() +"<br><br>");
////					}
////					else if(highlightedTitle != null && highlightedAbstract == null) {
////						System.out.println("Highlighted Title-> " + highlightedTitle + "\n SAbstract" + abstract1);
////					}
////					else if(highlightedTitle == null && highlightedAbstract != null) {
////						System.out.println("Highlighted Title-> " + dataContent + "\n HAbstract" + highlightedAbstract);
////					}
////					if(highlightedAbstract != null) {
////						list.add((i+1) + "-> "  + dataContent +"<br>"+ 
////								"<p>Abstract: "+ highlightedAbstract +"</p><br>" + "year: " + year + "  authors: " + authors + "<br><br>");
////					}
////					else {
//////						System.out.println("Title: " + dataContent + "\n SAbstract" + abstract1);
////						list.add((i+1) + "-> "  + dataContent +"<br>"+ 
////								"<p>Abstract: "+ abstract1 +"</p><br>" + "year: " + year + "  authors: " + authors + "<br><br>");
////					}
//					
//					if(!fragsAbstract.equalsIgnoreCase("")) {
//						list.add((i+1) +"-> " + dataContent +"</p>"+ 
//								"<p>"+ fragsAbstract +"<br>" + year + "  " + authors + "</p><br><br>");
//					}
//					else {
//						list.add((i+1) + "-> " + dataContent +"</p>"+ 
//								"<p>"+ abstract1 +"<br>" + year + "  " + authors + "</p><br><br>");
//					}
//					
//				}
//				Explanation [] explain = explanation.getDetails();
//				for(int x = 0; x < explain.length; x++) {
//					System.out.println("explain[" + x + "]--> " + explain[x]);
//				}
//				System.out.println("Exp: " + explanation.getDescription() + "Exp Value ->" + explanation.getValue());
//				
			}
			reader.close();
		}
		catch(Exception e) {
			
			System.out.println("Exception->" + e.toString());
//			CheckSpelling checkSpelling = new CheckSpelling();
//			String spellResult = checkSpelling.spellingChecker(text);
//			list.remove(0);
//			list.add(0, spellResult);
			//list.add("No matching documents found");
		}
		
		
		
		
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
