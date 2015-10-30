import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;

public class ContentSummary {
	DocumentSample ds;
	Set<String> allWords = new TreeSet<String>();
	HashMap<String, Integer> summary;
	
	
	public void populateContentSummary(){
		for(Set<String> s : ds.sampleWords){
			allWords.addAll(s);
		}
		
		//loop through all words
		for(String s : allWords){
			//get document frequency
			for(Set<String> sample : ds.sampleWords){
				if(sample.contains(s)){
					if(summary.containsKey(s)){
						int docFreq = summary.get(s);
						summary.put(s, docFreq+1);
					}
					else{
						summary.put(s, 1);
					}
				}
				//no need else
				//one of the sample has to contain s
			}
		}
		
	}
	
}

