import java.util.*;


/*
    Node in a category tree. Contains subcategories as well as a specificity
    value, a coverage value, a flag indicating if the database is classified as this
    category, and a list of top 4 urls for each string query done for this category.
*/
public class Category {
    String name;
    List<Category> subCategories;
    int coverage;
    double specificity;
    Category parent;
    HashMap<String, List<String>> urls;
    boolean aboveThresh;
    
    public Category(String name, Category parent) {
        this.name = name;
        subCategories = new ArrayList<Category>();
        coverage = 0;
        specificity = 0.0;
        urls = new HashMap<String, List<String>>();
        aboveThresh = false;
    }
    
    public Category getSubCategory(String name) {
        for(Category c : subCategories) {
            if(c.name.equalsIgnoreCase(name)) {
                return c;
            }
        }
        return null;
    }
    
    public void calculateSpecificity() {
        int sumChildCoverage = 0;
        for(Category subCategory : subCategories) {
            sumChildCoverage += subCategory.coverage;
        }
        
        for(Category subCategory : subCategories) {
            subCategory.specificity = (specificity * subCategory.coverage) / sumChildCoverage; 
        }
    }
}
