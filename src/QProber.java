
public class QProber {
    int cThresh;
    double sThresh;
    Category root;
    
    public QProber(int cThresh, double sThresh) {
        this.cThresh = cThresh;
        this.sThresh = sThresh;
        root = createCategoryTree();
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
    
    
    
}
