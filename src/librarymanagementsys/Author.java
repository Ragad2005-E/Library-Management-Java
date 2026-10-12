package librarymanagementsys;

/**
 *
 * @author Ragad
 */
// Author: Ragad Mohammed Eid
public class Author extends Person {
    private static final long serialVersionUID = -1127921200519870947L;
    private int noOfBooks;
    private String email;
    private String workplace;

    public Author() {
    }

    public Author(String identityNumber, String name, String gender, String phone, String address,
                  int noOfBooks, String email, String workplace) {
        super(identityNumber, name, gender, phone, address);
        this.noOfBooks = Validation.range(noOfBooks, 0, 1000, "Number of books");
        this.email = Validation.required(email, "email");
        this.workplace = Validation.required(workplace, "workplace");
    }

    public int getNoOfBooks() { return noOfBooks; }
    public void setNoOfBooks(int noOfBooks) { this.noOfBooks = Validation.range(noOfBooks, 0, 1000, "Number of books"); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = Validation.required(email, "email"); }

    public String getWorkplace() { return workplace; }
    public void setWorkplace(String workplace) { this.workplace = Validation.required(workplace, "workplace"); }

    @Override
    public String getDetails() {
        return "Author => " + super.toString() + ", NoOfBooks: " + noOfBooks + ", Email: " + email + ", Workplace: " + workplace;
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
// Author: Ragad Mohammed Eid
