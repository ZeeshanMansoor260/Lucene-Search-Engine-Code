package webInterface;


import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.spell.*;
import org.apache.lucene.search.suggest.InputIterator;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class CheckSpelling {
	public String spellingChecker(String text){
		String result = "";
		String wordIndex = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\Dictionary\\index";
		String index = "D:\\Studies\\Masters\\Semester1\\Information Retrieval\\Project\\Data\\Index";
		Directory dir;
		IndexReader reader;
		try {
			dir = FSDirectory.open(Paths.get(wordIndex));
			reader = DirectoryReader.open(FSDirectory.open(Paths.get(index)));
			
//			LuceneDictionary luceneDictionary = new LuceneDictionary(reader, "contents");
//			InputIterator inputIterator = luceneDictionary.getEntryIterator();
			
			SpellChecker spellChecker = new SpellChecker(dir);
			String wordForSuggestions = text;
			int suggestionsNumber = 5;
			String[] suggestions = spellChecker.suggestSimilar(wordForSuggestions, suggestionsNumber, reader, null, SuggestMode.SUGGEST_MORE_POPULAR);
			if (suggestions!=null && suggestions.length>0) {
				result = "<h1>Did you mean: ";
				for (String word : suggestions) {
					System.out.println("Did you mean:" + word);
					result = result + word + ", ";
				}
				result = result + "</h1>";
			}
			else {
				System.out.println("No suggestions found for word:"+wordForSuggestions);
				result = "No suggestions found for word:"+wordForSuggestions;
			}
			spellChecker.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
		
	}
	
}
