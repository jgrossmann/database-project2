import java.util.HashMap;
import java.util.Set;
import java.util.TreeSet;
import java.util.*;
import java.io.*;

public class ContentSummary {
	DocumentSample ds;
	TreeMap<String, Integer> summary;
	
	public ContentSummary(DocumentSample ds, String site) {
	    this.ds = ds;
	    summary = new TreeMap<String, Integer>();
	    populateContentSummary();
	    writeToFile(ds.category.name+"-"+site+".txt");
	}
	
	
	public void populateContentSummary(){
	    
		for(Set<String> s : ds.sampleWords){
		    for(String str : s) {
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
	        PrintWriter out = new PrintWriter(file);
	        for(Map.Entry<String, Integer> entry : summary.entrySet()) {
	            out.println(entry.getKey()+" "+entry.getValue());
	        }
	        out.flush();
	        out.close();
	    }catch(IOException e) {
	        e.printStackTrace();
	    }
	}
	
}

