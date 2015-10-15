
public class QProber {
    int cThresh;
    double sThresh;
    Category root;
    String site;
    
    private static final String account_key = "BU3X9a6Qbmi7UwCgwo3iuHTfOqbU5PWVjuEul/WzOLk";
    
    public QProber(int cThresh, double sThresh, String site) {
        this.cThresh = cThresh;
        this.sThresh = sThresh;
        root = createCategoryTree();
        this.site = site;
    }
    
    public Category createCategoryTree() {
        Category root = new Category("Root", null);
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
    
    public static String createUrl(String[] queryWords) {
		String bingUrl = "https://api.datamarket.azure.com/Bing/Search/Web?Query=%27"; 
        int i = 0;
        for(; i < queryWords.length-1; i++) {
          bingUrl += queryWords[i] + "%20";
        }
        bingUrl += queryWords[i] + "%27&$top=10&$format=Atom";

		return bingUrl;
	}
	
	//takes a bingUrl as input and returns the results as a string from bing.
	public static String getBingResults(String bingUrl) throws IOException {
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
    
    public void getQueryResults(Category root, String path) {
        File file = new File(path);
        BufferedReader in = new BufferedReader(new FileReader(file));
        String line;
        
        while((line = in.readLine()) != null) {
            String[] args = line.split("\\s+");
            Category subCategory = root.getSubCategory(args[0]);
            String[] queryWords = Arrays.copyOfRange(args, 1, args.length);
            String url = createUrl(queryWords);
            String getBingResults(url);
            //parse string results to get number of results
            int numResults = //
            subCategory.numMatches += numResults;
            root.numMatches += numResults;
        }
        
    }
    
}
