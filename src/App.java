import java.util.*;



/*
    Main class which queries databases in order to classify them into categories.
    Uses queries specified by the text files in the categories subdirectory. 
*/
public class App {

    //Outputs all unique urls as a document sample.
    public static String[] combineUrlMaps(List<Category> categories) {
        Set<String> set = new HashSet<String>();
        for(Category c : categories) {
            for(List<String> topUrls : c.urls.values()) {
                set.addAll(topUrls);
            }
        }
        return set.toArray(new String[set.size()]);
    }
    
    //uses document sample of top 4 urls and tokenizes all strings from each document
    //retrieved from each url. Calculates document frequency of each string found.
    public static void createContentSummary(Category root, String site) {
        System.out.println("\nCreating Content Summary for: "+root.name);
        List<Category> list = new ArrayList<Category>();
        list.add(root);
        for(Category c : root.subCategories) {
            if(c.aboveThresh) list.add(c);
        }
        String[] urls = combineUrlMaps(list);
        
        DocumentSample rootDs = new DocumentSample(root, urls);
        new ContentSummary(rootDs, site);
    }

   
    public static void main(String[] args) {
        if(args.length != 4) {
            System.out.println("Usage: please run ./run.sh <BING_ACCOUNT_KEY> <t_es> <t_ec> <host>");
            System.out.println("<BING_ACCOUNT_KEY> is your Bing Search Account Key");
            System.out.println("<t_es> is the specificity threshold (between 0 and 1)");
            System.out.println("<t_ec> is the coverage threshold");
            System.out.println("<host> is the URL of the database to be classified");
            return;
        }
        
        double sThresh = 0.0;
        int cThresh = 0;
        String key = args[0];
        try {
            sThresh = Double.parseDouble(args[1]);
            cThresh = Integer.parseInt(args[2]);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String site = args[3];
        
        
        
        QProber prober = new QProber(cThresh, sThresh, site, key);
        prober.qProberAlgorithm();
        Category root = prober.categorizeDatabase();
        createContentSummary(root, site);
        
        //creating content summaries for all categories above threshholds
        for(Category c : root.subCategories) {
            if(c.aboveThresh) {
                createContentSummary(c, site);
            }
        }
        
    }
}
