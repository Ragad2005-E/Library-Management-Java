package librarymanagementsys;

final class Validation {
    private Validation() {}
    static String required(String value, String label) {
        if (value == null || value.trim().isEmpty()) throw new IllegalArgumentException(label + " is required.");
        return value.trim();
    }
    static double money(double value, String label) {
        if (!Double.isFinite(value) || value < 0) throw new IllegalArgumentException(label + " must be finite and non-negative.");
        return value;
    }
    static int range(int value, int min, int max, String label) {
        if (value < min || value > max) throw new IllegalArgumentException(label + " must be between " + min + " and " + max + ".");
        return value;
    }
}
