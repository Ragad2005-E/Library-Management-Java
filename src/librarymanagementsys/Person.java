package librarymanagementsys;

import java.io.Serializable;

/**
 *
 * @author Ragad
 */
// Author: Ragad Mohammed Eid
public abstract class Person implements Serializable {
    private static final long serialVersionUID = 5588295574106707588L;
    private String idNumber;
    private String name;
    private String gender;
    private String phone;
    private String address;

    public Person() {
    }

    public Person(String identityNumber, String name, String gender, String phone, String address) {
        this.idNumber = Validation.required(identityNumber, "Identity number");
        this.name = Validation.required(name, "name");
        this.gender = Validation.required(gender, "gender");
        this.phone = Validation.required(phone, "phone");
        this.address = Validation.required(address, "address");
    }

    public String getIdentityNumber() { return idNumber; }
    public void setIdentityNumber(String identityNumber) { this.idNumber = Validation.required(identityNumber, "Identity number"); }

    public String getName() { return name; }
    public void setName(String name) { this.name = Validation.required(name, "name"); }

    public String getGender() { return gender; }
    public void setGender(String gender) { this.gender = Validation.required(gender, "gender"); }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = Validation.required(phone, "phone"); }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = Validation.required(address, "address"); }

    public abstract String getDetails();

    @Override
    public String toString() {
        return "Identity: " + idNumber + ", Name: " + name + ", Gender: " + gender + ", Phone: " + phone + ", Address: " + address;
    }
}
// Author: Ragad Mohammed Eid