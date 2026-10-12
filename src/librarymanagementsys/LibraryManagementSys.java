package librarymanagementsys;

import java.util.Scanner;
import java.util.NoSuchElementException;

/**
 *
 * @author Ragad
 */

// Author: Ragad Mohammed Eid
public class LibraryManagementSys {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try (Scanner input = new Scanner(System.in)) {
            Process process = new Process();
            
            try { process.loadFromFile(); }
            catch (IllegalStateException e) { System.err.println(e.getMessage()); return; }
            if (process.getCategories().isEmpty()) {
                process.addCategory(new Category("C1", "Programming"));
                process.addCategory(new Category("C2", "Database"));
                process.addCategory(new Category("C3", "Networks"));
                process.addCategory(new Category("C4", "AI"));
                process.addCategory(new Category("C5", "Math"));


            }
            int choice;
            do {
                printMenu();
                if (!input.hasNextLine()) { saveOnEnd(process); break; }
                choice = readIntRange(input, "Enter choice: ", 1, 9);
                
                try {
                switch (choice) {
                    case 1 -> showBooks(process);
                    
                    case 2 -> addBook(process, input);
                    
                    case 3 -> deleteBook(process, input);
                    
                    case 4 -> searchBook(process, input);
                    
                    case 5 -> buyBook(process, input);
                    
                    case 6 -> showStudents(process);
                    
                    case 7 -> addStudent(process, input);
                    
                    case 8 -> System.out.println(process.fullReport());
                    
                    case 9 -> {
                        try { process.saveToFile(); System.out.println("Data saved. Goodbye."); }
                        catch (IllegalStateException e) { System.err.println(e.getMessage()); choice = 0; }
                    }
                    
                    default -> System.out.println("Wrong choice!");
                }
                
                } catch (IllegalArgumentException e) { System.out.println(e.getMessage()); }
                  catch (NoSuchElementException e) { saveOnEnd(process); break; }
            } while (choice != 9);
        }
    }
    
    private static void printMenu() {
        System.out.println("\n--------------------------");
        System.out.println("1. Show Books");
        System.out.println("2. Add a Book");
        System.out.println("3. Delete a Book");
        System.out.println("4. Search for a Book");
        System.out.println("5. Buy a Book");
        System.out.println("6. Show Students");
        System.out.println("7. Add a Student");
        System.out.println("8. Full Report");
        System.out.println("9. Exit");
        System.out.println("--------------------------");
    }
    
    private static void showBooks(Process process) {
        if (process.getBooks().isEmpty()) {
            System.out.println("No books");
            return;
        }
        for (Book b : process.getBooks()) System.out.println(b);
    }

    private static void addBook(Process process, Scanner input) {
        Category chosen = chooseCategory(process, input);
        if (chosen == null) return;

        String id = readNonEmpty(input, "Book ID: ");
        if (process.searchBookById(id) != null) { System.out.println("Book ID already exists."); return; }
        String name = readNonEmpty(input, "Book Name: ");

        System.out.println("Author Info ---->");
        String aId = readNonEmpty(input, "Author Identity: ");
        String aName = readNonEmpty(input, "Author Name: ");
        String aGender = readGenderMF(input);
        String aPhone = readNonEmpty(input, "Phone: ");
        String aAddress = readNonEmpty(input, "Address: ");

        int noBooks = readIntRange(input, "No Of Books: ", 0, 1000);
        String email = readNonEmpty(input, "Email: ");
        String work = readNonEmpty(input, "Workplace: ");

        Author author = new Author(aId, aName, aGender, aPhone, aAddress, noBooks, email, work);

        String publisher = readNonEmpty(input, "Publisher: ");
        int year = readIntRange(input, "Edition Year: ", 1900, 2100);

        double price = readDoubleMin(input, "Price: ", 0);
        int qty = readIntRange(input, "Quantity: ", 0, 100000);

        Book book = new Book(id, name, chosen, author, publisher, year, price, qty);
        process.addBook(book);
        System.out.println("Book added");
    }

    private static void deleteBook(Process process, Scanner input) {
        System.out.print("Enter Book ID to delete: ");
        String id = input.nextLine();

        boolean ok = process.deleteBookById(id);
        System.out.println(ok ? "Deleted." : "Book not found");
    }

    private static void searchBook(Process process, Scanner input) {
        System.out.print("Search by ID or Name: ");
        String key = input.nextLine();

        Book b = process.searchBook(key);
        if (b == null) System.out.println("Not found");
        else System.out.println(b);
    }

    private static void buyBook(Process process, Scanner input) {
        System.out.print("Enter Book ID: ");
        String bookId = input.nextLine();

        System.out.print("Enter Student ID: ");
        String studentId = input.nextLine();

        boolean ok = process.buyBook(bookId, studentId);
        if (ok) System.out.println("Purchase done");
        else System.out.println("Purchase failed (check qty / student / budget)");
    }

    private static void showStudents(Process process) {
        if (process.getStudents().isEmpty()) {
            System.out.println("No students yet");
            return;
        }
        for (Student s : process.getStudents()) System.out.println(s);
    }

    private static void addStudent(Process process, Scanner input) {
        System.out.println("Student Info ---->");

        String identity = readNonEmpty(input, "Identity Number: ");
        String name = readNonEmpty(input, "Name: ");
        String gender = readGenderMF(input);
        String phone = readNonEmpty(input, "Phone: ");
        String address = readNonEmpty(input, "Address: ");

        String studentId = readNonEmpty(input, "Student ID: ");
        if (process.searchStudent(studentId) != null) { System.out.println("Student ID already exists."); return; }
        String spec = readNonEmpty(input, "Specialization: ");
        String date = readNonEmpty(input, "Enrolled Date: ");
        double budget = readDoubleMin(input, "Budget: ", 0);

        Student s = new Student(identity, name, gender, phone, address, studentId, spec, date, budget);
        process.addStudent(s);
        System.out.println("Student added");
    }
    
    private static void showCategories(Process process) {
        if (process.getCategories().isEmpty()) {
            System.out.println("No categories yet");
            return;
        }
        for (Category c : process.getCategories()) System.out.println(c);
    }

    private static Category chooseCategory(Process process, Scanner input) {
        if (process.getCategories().isEmpty()) {
            System.out.println("No categories available");
            return null;
        }

        while (true) {
            System.out.println("Available Categories ---->");
            showCategories(process);

            String catId = readNonEmpty(input, "Enter Category ID from the list: ");
            Category c = process.searchCategoryById(catId);

            if (c != null) return c;

            System.out.println("Wrong category id --> Try again...");
        }
    }
    
    private static String readNonEmpty(Scanner input, String message) {
        String value;
        while (true) {
            System.out.print(message);
            value = input.nextLine().trim();

            if (!value.isEmpty()) return value;

            System.out.println("This field can't be empty -> Try again...");
        }
    }
    
    private static String readGenderMF(Scanner input) {
        while (true) {
            String g = readNonEmpty(input, "Gender (m/f): ").toLowerCase();

            if (g.equals("m")) return "Male";
            if (g.equals("f")) return "Female";

            System.out.println("Please enter only m or f.");
        }
    }
    
    private static int readIntRange(Scanner input, String message, int min, int max) {
        while (true) {
            String line = readNonEmpty(input, message);
            try { int value = Integer.parseInt(line); if (value >= min && value <= max) return value; }
            catch (NumberFormatException ignored) { }
            System.out.println("Enter a whole number between " + min + " and " + max + ".");
        }
    }
    private static double readDoubleMin(Scanner input, String message, double min) {
        while (true) {
            String line = readNonEmpty(input, message);
            try {
                double value = Double.parseDouble(line);
                if (Double.isFinite(value) && value >= min) return value;
            } catch (NumberFormatException ignored) { }
            System.out.println("Enter a finite number >= " + min + ".");
        }
    }
    private static void saveOnEnd(Process process) {
        try { process.saveToFile(); System.out.println("Input ended. Data saved."); }
        catch (IllegalStateException e) { System.err.println(e.getMessage()); }
    }
}
