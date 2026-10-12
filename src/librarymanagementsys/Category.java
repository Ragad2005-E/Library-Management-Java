package librarymanagementsys;

import java.io.Serializable;

/**
 *
 * @author Ragad
 */

// Author: Ragad Mohammed Eid
public class Category implements Serializable {
    private static final long serialVersionUID = -7710041023797466533L;
    private String categoryId;
    private String name;

    public Category() {}

    public Category(String categoryId, String name) {
        this.categoryId = Validation.required(categoryId, "categoryId");
        this.name = Validation.required(name, "name");
    }

    public String getCategoryId() { return categoryId; }
    public void setCategoryId(String categoryId) { this.categoryId = Validation.required(categoryId, "categoryId"); }

    public String getName() { return name; }
    public void setName(String name) { this.name = Validation.required(name, "name"); }

    @Override
    public String toString() {
        return "CategoryID: " + categoryId + ", Name: " + name;
    }
}
// Author: Ragad Mohammed Eid