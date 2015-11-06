import java.util.*;
import java.io.*;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import org.apache.commons.codec.binary.Base64;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;


/*
    Class which implements the QProber algorithm for database classification.
*/
public class QProber {
    int cThresh;
    double sThresh;
    Category root;
    String site;
    HashMap<String, Integer> urls;
    
    //private static final String account_key = "BU3X9a6Qbmi7UwCgwo3iuHTfOqbU5PWVjuEul/WzOLk";
    String account_key;
    
    public QProber(int cThresh, double sThresh, String site, String key) {
        this.cThresh = cThresh;
        this.sThresh = sThresh;
        root = createCategoryTree();
        this.site = site;
        urls = new HashMap<String, Integer>();
        account_key = key;
    }
    
    
    //creates a tree of categories which correspond to possible categories to classify a database
    public Category createCategoryTree() {
        Category root = new Category("Root", null);
        root.specificity = 1.0;
        Category computers = new Category("Computers", root);
        Category health = new Category("Health", root);
        Category sports = new Category("Sports", root);
        
        root.subCategories.add(computers);
        root.subCategories.add(health);
        root.subCategories.add(sports);
        
        computers.subCategories.add(new Category("Hardware", computers));
        computers.subCategories.add(new Category("Programming", computers));
        health.subCategories.add(new Category("Fitness", health));
        health.subCategories.add(new Category("Diseases", health));
        sports.subCategories.add(new Category("Soccer", sports));
        sports.subCategories.add(new Category("Basketball", sports));
        return root;
    }


	
	//takes a bingUrl as input and returns the results as a string from bing.
	public String getBingResults(String bingUrl) throws IOException {
		byte[] accountKeyBytes = Base64.encodeBase64((account_key + ":" + account_key).getBytes());
		String accountKeyEnc = new String(accountKeyBytes);

		URL url = new URL(bingUrl);
		URLConnection urlConnection = url.openConnection();
		urlConnection.setRequestProperty("Authorization", "Basic " + accountKeyEnc);
				
		InputStream inputStream = (InputStream) urlConnection.getContent();		
		byte[] contentRaw = new byte[urlConnection.getContentLength()];
		inputStream.read(contentRaw);
		String content = new String(contentRaw);
		return content;
	}
	
	
	//performs queries for each category to generate a coverage for the database
	//then finds specificity for each category
	public void qProberAlgorithm() {
	    getQueryResults(root, "categories/root.txt");
	    for(Category c : root.subCategories) {
	        if(c.name.equalsIgnoreCase("Computer")) {
	            getQueryResults(c, "categories/computer.txt");
	            
	        }else if(c.name.equalsIgnoreCase("Health")) {
	            getQueryResults(c, "categories/health.txt");
	        }if(c.name.equalsIgnoreCase("Sports")) {
	            getQueryResults(c, "categories/sports.txt");
	        }
	    }
	    
	    root.calculateSpecificity();
	    for(Category c : root.subCategories) {
	        c.calculateSpecificity();
	    }
	    
	}
	
	//traverses category tree to see which categories have coverage and specificity
	//above the input thresholds. 
	public Category categorizeDatabase() {
	    System.out.println("Classifying...");
	    String classification = "Root";
	    root.aboveThresh = true;
	    List<String> printList = new ArrayList<String>();
	    for(Category c : root.subCategories) {
	        System.out.println("Specificity for category: "+c.name+" is "+c.specificity);
	        System.out.println("Coverage for category: "+c.name+" is "+c.coverage);
	        if(c.coverage >= cThresh) {
	            if(c.specificity >= sThresh) {
	                c.aboveThresh = true;
	                String subClass = classification+"/"+c.name;
	                boolean printed = false;
	                for(Category subCategory : c.subCategories) {
	                    System.out.println("Specificity for category: "+subCategory.name+" is "+subCategory.specificity);
	                    System.out.println("Coverage for category: "+subCategory.name+" is "+subCategory.coverage);
	                    if(subCategory.coverage >= cThresh && subCategory.specificity >= sThresh) {
	                        subCategory.aboveThresh = true;
	                        printed = true;
	                        printList.add(subClass+"/"+subCategory.name);
	                    }
	                }
	                
	                if(!printed) printList.add(subClass);
	            }
	            
	        }
	    }
	    
	    System.out.println("\nClassification:");
	    if(printList.size() == 0) {
	        System.out.println("Root");
	    }else {
	        for(String str : printList) {
	            System.out.println(str);
	        }
	    }
	    return root;
	}
    
    
    //performs the query for each line in the path given to the function.
    //adds the number of returned results to the coverage of the subcategory
    //specified on the query line. Also caches the top 4 result urls of each
    //query with the corresponding category.
    public void getQueryResults(Category root, String path){
        try{
        File file = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
       
	
       	while((line = in.readLine()) != null) {
            String[] args = line.split("\\s+");
            Category subCategory = root.getSubCategory(args[0]);
            String[] queryWords = Arrays.copyOfRange(args, 1, args.length);
            String url = createUrl(queryWords);
            String results = getBingResults(url);
            int numResults = getResultCount(results);

            root.urls.put(url, ResultParser.parseResults(results, numResults));
            subCategory.coverage += numResults;
        }
	}
	catch(IOException io){
		io.printStackTrace();
	}
        
    }
    
    public void calculateSpecificity() {
        root.calculateSpecificity();
        for(Category c : root.subCategories) {
            c.calculateSpecificity();
        }
    }


    //Parses the web results as a string to get the number of returned results
    public int getResultCount(String s){
		
		int count = 0;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		
		try{
			DocumentBuilder db = dbf.newDocumentBuilder();
			Document dom = db.parse(new InputSource(new ByteArrayInputStream(s.getBytes("utf-8"))));

			Element docEle = dom.getDocumentElement();
			
			String num = docEle.getElementsByTagName("d:WebTotal").item(0).getTextContent();
			//System.out.println("number is " + num);
			count = Integer.parseInt(num);
			
		}catch(ParserConfigurationException x){
			System.out.print("Parse error");
			x.printStackTrace();
		}catch(SAXException se){
			se.printStackTrace();
		}catch(IOException io){
			io.printStackTrace();
		}
		return count;
	}
	
	
	//creates a bing url from the string array of query words.
	public String createUrl(String[] queryWords) {

        String bingUrl = "https://api.datamarket.azure.com/Data.ashx/Bing/SearchWeb/v1/Composite?Query=%27";
        bingUrl += "site%3a" + site + "%20"; 
        int i = 0;
        for(; i < queryWords.length-1; i++) {
        	bingUrl += queryWords[i] + "%20";
        }
        bingUrl += queryWords[i] + "%27&$top=10&$format=Atom";

        return bingUrl;
	}
    
}
