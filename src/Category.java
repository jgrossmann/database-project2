import java.util.*;

public class Category {
    String name;
    int numMatches;
    List<Category> subCategories;
    int coverage;
    double specificity;
    Category parent;
    
    public Category(String name, Category parent) {
        this.name = name;
        numMatches = 0;
        subCategories = new ArrayList<Category>();
        coverage = 0;
        specificity = 0.0;
    }
    
    public Category getSubCategory(String name) {
        for(Category c : subCategories) {
            if(c.name.equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
}