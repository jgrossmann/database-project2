import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.*;
import java.io.*;


//takes a DocumentSampel object, calculates the document frequency for each word
//in the documentsample wordset, and writes each word and corresponding doc frequency 
//in alphabetical order.
public class ContentSummary {
	DocumentSample ds;
	TreeMap<String, Integer> summary;
	
	public ContentSummary(DocumentSample ds, String site) {
	    this.ds = ds;
	    summary = new TreeMap<String, Integer>();
	    populateContentSummary();
	    writeToFile(ds.category.name+"-"+site+".txt");
	}
	

	//populate treemap with words and corresponding doc frequency
	public void populateContentSummary(){
	    
		for(Set<String> s : ds.sampleWords){
		    for(String str : s) {
			//update document frequency for each word
		        Integer freq = summary.get(str);
		        if(freq == null) {
		            freq = 0;
		        }
		        summary.put(str, ++freq);
		    }
		}
		
	}
	
	public void writeToFile(String path) {
	    File file = new File(path);
	    if(file == null) {
	        System.out.println("Can't create file at path: "+path);
	        return;
	    }
	    
	    try {
	        PrintWriter out = new PrintWriter(new FileOutputStream(file, false));
	        for(Map.Entry<String, Integer> entry : summary.entrySet()) {
	            out.println(entry.getKey()+"#"+entry.getValue());
	        }
	        out.flush();
	        out.close();
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
	}
	
}

