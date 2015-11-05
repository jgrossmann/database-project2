import java.util.*;
public class App {

    public static String[] combineUrlMaps(List<Category> categories) {
        HashMap<String, Integer> urlMap = new HashMap<String, Integer>();
        for(Category c : categories) {
            urlMap.putAll(c.urls);
        }
        return urlMap.keySet().toArray(new String[urlMap.size()]);
    }
    
    public static void createContentSummary(Category root, String site) {
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
        if(args.length != 3) {
            System.out.println("Usage: please run ./run.sh UPDATE USAGE");
            return;
        }
        
        double sThresh = 0.0;
        int cThresh = 0;
        
        try {
            sThresh = Double.parseDouble(args[0]);
            cThresh = Integer.parseInt(args[1]);
        } catch(NumberFormatException e) {
            e.printStackTrace();
            return;
        }
        String site = args[2];
        
        QProber prober = new QProber(cThresh, sThresh, site);
        prober.qProberAlgorithm();
        Category root = prober.categorizeDatabase();
        System.out.println("Root");
        createContentSummary(root, site);
        
        for(Category c : root.subCategories) {
            if(c.aboveThresh) {
                System.out.println(c.name);
                createContentSummary(root, site);
            }
        }
        
    }
}
