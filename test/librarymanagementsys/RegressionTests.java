package librarymanagementsys;

import java.io.*;
import java.nio.file.*;
import java.util.*;

/** Standalone regression suite; no external dependencies. */
public final class RegressionTests {
    private static int checks;
    private static void check(boolean ok, String message) {
        checks++; if (!ok) throw new AssertionError(message);
    }
    private static void rejects(Runnable action, String message) {
        boolean rejected = false;
        try { action.run(); } catch (IllegalArgumentException | IllegalStateException | NullPointerException e) { rejected = true; }
        check(rejected, message);
    }
    private static Author author() { return new Author("A1", "Demo Author", "Female", "Demo", "Demo", 1, "demo@example.com", "Demo"); }
    private static Book book(String id, String name, Category c, double price, int qty) {
        return new Book(id, name, c, author(), "Demo Publisher", 2026, price, qty);
    }
    private static Student student(String id, double budget) {
        return new Student("DEMO", "Demo Student", "Female", "Demo", "Demo", id, "CS", "2026-01-01", budget);
    }
    public static void main(String[] args) throws Exception {
        Path folder = Files.createTempDirectory("library-regression-");
        Path file = folder.resolve("data.dat");
        Process p = new Process(file); Category c = new Category("C1", "Programming"); p.addCategory(c);
        Book b1 = book("B1", "B2 handbook", c, 10, 2), b2 = book("B2", "Other book", c, 20, 1);
        Student s = student("S1", 100); p.addBook(b1); p.addBook(b2); p.addStudent(s);
        check(p.buyBook(" b2 ", " s1 "), "Purchase exact normalized ID");
        check(b1.getQuantity() == 2 && b2.getQuantity() == 0 && s.getBudget() == 80, "Correct book and budget");
        check(!p.buyBook("B2", "S1"), "Out of stock");
        check(!p.buyBook("handbook", "S1"), "Name is not purchase ID");
        check(!p.buyBook("B1", "unknown"), "Unknown student");
        s.setBudget(1); check(!p.buyBook("B1", "S1") && b1.getQuantity() == 2, "Insufficient funds leaves state unchanged");
        check(p.searchBook("B2") == b2, "Exact ID prioritized over title match");
        check(p.searchBook("HANDBOOK") == b1, "Title search");
        check(p.searchBook(" ") == null, "Blank search");
        rejects(() -> p.addBook(book(" b1 ", "Duplicate", c, 1, 1)), "Duplicate book ID");
        rejects(() -> p.addStudent(student("s1", 5)), "Duplicate student ID");
        rejects(() -> p.addCategory(new Category("c1", "Duplicate")), "Duplicate category ID");
        rejects(() -> book("X", "Negative", c, -1, 1), "Negative price");
        rejects(() -> b1.setPrice(Double.POSITIVE_INFINITY), "Infinite price setter");
        rejects(() -> s.setBudget(Double.NaN), "NaN budget setter");
        rejects(() -> b1.setQuantity(-1), "Negative stock setter");
        rejects(() -> p.addBook(new Book()), "Incomplete book");
        rejects(() -> p.addStudent(new Student()), "Incomplete student");
        p.getBooks().clear(); check(p.getBooks().size() == 2, "List getter cannot remove internal records");
        p.saveToFile(); Process loaded = new Process(file); loaded.loadFromFile();
        check(loaded.getBooks().size() == 2 && loaded.getStudents().size() == 1 && loaded.getCategories().size() == 1, "All lists persisted");
        byte[] valid = Files.readAllBytes(file); b2.setBookId("B1");
        rejects(p::saveToFile, "Mutated duplicate caught at save");
        check(Arrays.equals(valid, Files.readAllBytes(file)), "Failed save preserves original"); b2.setBookId("B2");
        Path bad = folder.resolve("bad.dat"); Files.writeString(bad, "not serialized data");
        Process badProcess = new Process(bad); badProcess.addCategory(c);
        rejects(badProcess::loadFromFile, "Corrupt file rejected");
        check(badProcess.getCategories().size() == 1 && Files.readString(bad).equals("not serialized data"), "Failed load preserves memory and file");
        Process brokenDestination = new Process(folder.resolve("missing/data.dat"));
        rejects(brokenDestination::saveToFile, "Save failure reported");
        Path legacy = folder.resolve("legacy.dat");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(legacy))) { out.writeObject(p.getBooks()); out.writeObject(p.getStudents()); }
        Process old = new Process(legacy); old.loadFromFile();
        check(old.getBooks().size() == 2 && old.getCategories().size() == 1, "Legacy category recovery");
        Path truncated = folder.resolve("truncated.dat");
        try (ObjectOutputStream out = new ObjectOutputStream(Files.newOutputStream(truncated))) {
            out.writeObject("LibraryManagementSys:v2"); out.writeObject(p.getBooks()); out.writeObject(p.getStudents());
        }
        rejects(new Process(truncated)::loadFromFile, "Missing categories in current format rejected");
        s.setBudget(0.3); Book decimal = book("DEC", "Decimal", c, 0.1, 1); p.addBook(decimal);
        check(p.buyBook("DEC", "S1") && s.getBudget() == 0.2, "Decimal subtraction");
        check(p.deleteBookById(" b1 ") && p.searchBookById("B1") == null, "Delete exact ID");
        System.out.println("PASS: " + checks + " regression checks.");
    }
}
