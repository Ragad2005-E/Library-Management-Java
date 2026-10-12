package librarymanagementsys;

import java.io.Serializable;

/**
 *
 * @author Ragad
 */

// Author: Ragad Mohammed Eid
public class Book implements Serializable {
    private static final long serialVersionUID = 8799953548607578793L;
    private String bookId;
    private String name;
    private Category category;
    private Author author;
    private String publisher;
    private int editionYear;
    private double price;
    private int quantity;

    public Book() {
    }

    public Book(String bookId, String name, Category category, Author author,
                String publisher, int editionYear, double price, int quantity) {
        this.bookId = Validation.required(bookId, "bookId");
        this.name = Validation.required(name, "name");
        this.category = category;
        this.author = author;
        this.publisher = Validation.required(publisher, "publisher");
        this.editionYear = Validation.range(editionYear, 1900, 2100, "Edition year");
        this.price = Validation.money(price, "Price");
        this.quantity = Validation.range(quantity, 0, 100000, "Quantity");
    }

    public String getBookId() { return bookId; }
    public void setBookId(String bookId) { this.bookId = Validation.required(bookId, "bookId"); }

    public String getName() { return name; }
    public void setName(String name) { this.name = Validation.required(name, "name"); }

    public Category getCategory() { return category; }
    public void setCategory(Category category) { this.category = category; }

    public Author getAuthor() { return author; }
    public void setAuthor(Author author) { this.author = author; }

    public String getPublisher() { return publisher; }
    public void setPublisher(String publisher) { this.publisher = Validation.required(publisher, "publisher"); }

    public int getEditionYear() { return editionYear; }
    public void setEditionYear(int editionYear) { this.editionYear = Validation.range(editionYear, 1900, 2100, "Edition year"); }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = Validation.money(price, "Price"); }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) { this.quantity = Validation.range(quantity, 0, 100000, "Quantity"); }

    @Override
    public String toString() {
        String authorName = (author == null) ? "No Author" : author.getName();
        return "BookID: " + bookId + ", Name: " + name + ", Category: " + category + ", Author: " + authorName + ", Publisher: " + publisher +
                ", Year: " + editionYear + ", Price: " + price + ", Qty: " + quantity;
    }
}
// Author: Ragad Mohammed Eid