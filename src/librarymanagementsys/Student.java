package librarymanagementsys;

/**
 *
 * @author Ragad
 */

// Author: Ragad Mohammed Eid
public class Student extends Person {
    private static final long serialVersionUID = 4849675023127833L;
    private String studentId;
    private String specialization;
    private String enrolledDate;
    private double budget;

    public Student() {
    }

    public Student(String identityNumber, String name, String gender, String phone, String address,
                   String studentId, String specialization, String enrolledDate, double budget) {
        super(identityNumber, name, gender, phone, address);
        this.studentId = Validation.required(studentId, "studentId");
        this.specialization = Validation.required(specialization, "specialization");
        this.enrolledDate = Validation.required(enrolledDate, "enrolledDate");
        this.budget = Validation.money(budget, "Budget");
    }

    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = Validation.required(studentId, "studentId"); }

    public String getSpecialization() { return specialization; }
    public void setSpecialization(String specialization) { this.specialization = Validation.required(specialization, "specialization"); }

    public String getEnrolledDate() { return enrolledDate; }
    public void setEnrolledDate(String enrolledDate) { this.enrolledDate = Validation.required(enrolledDate, "enrolledDate"); }

    public double getBudget() { return budget; }
    public void setBudget(double budget) { this.budget = Validation.money(budget, "Budget"); }

    @Override
    public String getDetails() {
        return "Student: " + super.toString() + ", StudentID: " + studentId + ", Specialization: " + specialization + ", Enrolled: " + enrolledDate +
                ", Budget: " + budget;
    }

    @Override
    public String toString() {
        return getDetails();
    }
}
// Author: Ragad Mohammed Eid