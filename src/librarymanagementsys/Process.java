package librarymanagementsys;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Library inventory and simulated purchases. Author: Ragad Mohammed Eid. */
public class Process {
    private ArrayList<Book> books = new ArrayList<>();
    private ArrayList<Student> students = new ArrayList<>();
    private ArrayList<Category> categories = new ArrayList<>();
    private final Path dataFile;

    public Process() { this(Path.of("lib_data.dat")); }
    public Process(Path dataFile) { this.dataFile = Objects.requireNonNull(dataFile).toAbsolutePath(); }
    public ArrayList<Book> getBooks() { return new ArrayList<>(books); }
    public ArrayList<Student> getStudents() { return new ArrayList<>(students); }
    public ArrayList<Category> getCategories() { return new ArrayList<>(categories); }
    private static String key(String id) { return Validation.required(id, "ID").toLowerCase(Locale.ROOT); }
    public void addCategory(Category category) {
        Objects.requireNonNull(category, "Category");
        key(category.getCategoryId()); Validation.required(category.getName(), "Category name");
        if (searchCategoryById(category.getCategoryId()) != null) throw new IllegalArgumentException("Category ID already exists.");
        categories.add(category);
    }
    public Category searchCategoryById(String id) {
        if (id == null || id.isBlank()) return null;
        for (Category c : categories) if (key(c.getCategoryId()).equals(key(id))) return c;
        return null;
    }
    public Book searchBookById(String id) {
        if (id == null || id.isBlank()) return null;
        for (Book b : books) if (key(b.getBookId()).equals(key(id))) return b;
        return null;
    }
    private static void validateBook(Book b) {
        Objects.requireNonNull(b, "Book"); key(b.getBookId());
        Validation.required(b.getName(), "Book name"); Validation.required(b.getPublisher(), "Publisher");
        Validation.money(b.getPrice(), "Price"); Validation.range(b.getQuantity(), 0, 100000, "Quantity");
        Validation.range(b.getEditionYear(), 1900, 2100, "Edition year");
        Objects.requireNonNull(b.getCategory(), "Book category");
        key(b.getCategory().getCategoryId()); Validation.required(b.getCategory().getName(), "Category name");
        Author author = Objects.requireNonNull(b.getAuthor(), "Book author");
        Validation.required(author.getIdentityNumber(), "Author identity");
        Validation.required(author.getName(), "Author name");
        Validation.required(author.getEmail(), "Author email");
        Validation.required(author.getWorkplace(), "Author workplace");
        Validation.range(author.getNoOfBooks(), 0, 1000, "Author book count");
    }
    private static void validateStudent(Student s) {
        Objects.requireNonNull(s, "Student"); key(s.getStudentId());
        Validation.required(s.getIdentityNumber(), "Identity number"); Validation.required(s.getName(), "Student name");
        Validation.required(s.getSpecialization(), "Specialization"); Validation.required(s.getEnrolledDate(), "Enrollment date");
        Validation.money(s.getBudget(), "Budget");
    }
    public void addBook(Book b) {
        validateBook(b);
        if (searchBookById(b.getBookId()) != null) throw new IllegalArgumentException("Book ID already exists.");
        if (searchCategoryById(b.getCategory().getCategoryId()) == null) throw new IllegalArgumentException("Add the category first.");
        books.add(b);
    }
    public boolean deleteBookById(String id) {
        Book b = searchBookById(id); return b != null && books.remove(b);
    }
    public Book searchBook(String keyword) {
        if (keyword == null || keyword.isBlank()) return null;
        Book exact = searchBookById(keyword); if (exact != null) return exact;
        String term = keyword.trim().toLowerCase(Locale.ROOT);
        for (Book b : books) if (b.getName().toLowerCase(Locale.ROOT).contains(term)) return b;
        return null;
    }
    public boolean buyBook(String bookId, String studentId) {
        Book b = searchBookById(bookId); Student s = searchStudent(studentId);
        if (b == null || s == null) return false;
        validateBook(b); validateStudent(s);
        if (b.getQuantity() <= 0 || s.getBudget() < b.getPrice()) return false;
        double remaining = java.math.BigDecimal.valueOf(s.getBudget())
            .subtract(java.math.BigDecimal.valueOf(b.getPrice())).doubleValue();
        b.setQuantity(b.getQuantity() - 1); s.setBudget(remaining);
        return true;
    }
    public void addStudent(Student s) {
        validateStudent(s);
        if (searchStudent(s.getStudentId()) != null) throw new IllegalArgumentException("Student ID already exists.");
        students.add(s);
    }
    public Student searchStudent(String id) {
        if (id == null || id.isBlank()) return null;
        for (Student s : students) if (key(s.getStudentId()).equals(key(id))) return s;
        return null;
    }
    public String fullReport() {
        StringBuilder out = new StringBuilder("\nBOOKS REPORT------>\n");
        if (books.isEmpty()) out.append("No books found\n");
        for (Book b : books) out.append(b).append('\n');
        out.append("\nSTUDENTS REPORT------>\n");
        if (students.isEmpty()) out.append("No students found\n");
        for (Student s : students) out.append(s).append('\n');
        out.append("\nCATEGORIES REPORT------>\n");
        for (Category c : categories) out.append(c).append('\n');
        return out.toString();
    }
    public void saveToFile() {
        validateState(books, students, categories);
        Path temp = null;
        try {
            temp = Files.createTempFile(dataFile.getParent(), "library-", ".tmp");
            try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(temp))) {
                out.writeObject("LibraryManagementSys:v2");
                out.writeObject(books); out.writeObject(students); out.writeObject(categories);
            }
            // Never fall back to overwriting the original file if atomic replacement is unavailable.
            Files.move(temp, dataFile, StandardCopyOption.ATOMIC_MOVE, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new IllegalStateException("Could not save data; the existing file was preserved: " + e.getMessage(), e);
        } finally {
            if (temp != null) try { Files.deleteIfExists(temp); } catch (IOException ignored) { /* Best-effort temp cleanup. */ }
        }
    }
    private static <T> ArrayList<T> readList(Object value, Class<T> type) throws IOException {
        if (!(value instanceof ArrayList<?> raw)) throw new IOException("Invalid data list.");
        ArrayList<T> result = new ArrayList<>();
        for (Object item : raw) {
            if (!type.isInstance(item)) throw new IOException("Invalid " + type.getSimpleName() + " record.");
            result.add(type.cast(item));
        }
        return result;
    }
    private static void validateState(List<Book> bs, List<Student> ss, List<Category> cs) {
        Set<String> ids = new HashSet<>();
        for (Category c : cs) {
            Objects.requireNonNull(c); Validation.required(c.getName(), "Category name");
            if (!ids.add(key(c.getCategoryId()))) throw new IllegalArgumentException("Duplicate category ID.");
        }
        Set<String> categoryIds = new HashSet<>(ids); ids.clear();
        for (Book b : bs) {
            validateBook(b);
            if (!ids.add(key(b.getBookId()))) throw new IllegalArgumentException("Duplicate book ID.");
            if (!categoryIds.contains(key(b.getCategory().getCategoryId()))) throw new IllegalArgumentException("Unknown book category.");
        }
        ids.clear();
        for (Student s : ss) {
            validateStudent(s);
            if (!ids.add(key(s.getStudentId()))) throw new IllegalArgumentException("Duplicate student ID.");
        }
    }
    public void loadFromFile() {
        if (!Files.exists(dataFile)) return;
        try (ObjectInputStream in = new ObjectInputStream(Files.newInputStream(dataFile))) {
            in.setObjectInputFilter(ObjectInputFilter.Config.createFilter("maxdepth=30;maxrefs=100000;maxbytes=10000000;librarymanagementsys.*;java.util.ArrayList;java.lang.String;java.lang.Object;!*"));
            Object first = in.readObject();
            boolean currentFormat = "LibraryManagementSys:v2".equals(first);
            ArrayList<Book> newBooks = readList(currentFormat ? in.readObject() : first, Book.class);
            ArrayList<Student> newStudents = readList(in.readObject(), Student.class);
            ArrayList<Category> newCategories;
            if (currentFormat) newCategories = readList(in.readObject(), Category.class);
            else {
                // Older versions stored only books and students. Recover categories from books.
                newCategories = new ArrayList<>(); Set<String> seen = new HashSet<>();
                for (Book b : newBooks) if (b.getCategory() != null && seen.add(key(b.getCategory().getCategoryId()))) newCategories.add(b.getCategory());
            }
            validateState(newBooks, newStudents, newCategories);
            books = newBooks; students = newStudents; categories = newCategories;
        } catch (IOException | ClassNotFoundException | IllegalArgumentException | NullPointerException e) {
            throw new IllegalStateException("Could not load data; no records were replaced. Back up and inspect " + dataFile + ": " + e.getMessage(), e);
        }
    }
}
