package pl.com.britenet.hospital.domain;

import javax.persistence.*;

@Entity
@Table(name = "hospitals")
public class Hospital {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String country;

    private String town;

    private String street;

    @Column(name = "postal_code")
    private String postalCode;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "fax_number")
    private String faxNumber;

    @Column(name = "number_of_ambulances")
    private int numberOfAmbulances;

    @Column(name = "helicopter_access")
    private boolean helicopterAccess;

    @Column(name = "teaching_hospital")
    private boolean teachingHospital;

    public Hospital() {
    }

    public Hospital(String name, String country, String town) {
        this.name = name;
        this.country = country;
        this.town = town;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return this.country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return this.town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return this.street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return this.postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return this.phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return this.faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public int getNumberOfAmbulances() {
        return this.numberOfAmbulances;
    }

    public void setNumberOfAmbulances(int numberOfAmbulances) {
        this.numberOfAmbulances = numberOfAmbulances;
    }

    public boolean isHelicopterAccess() {
        return this.helicopterAccess;
    }

    public void setHelicopterAccess(boolean helicopterAccess) {
        this.helicopterAccess = helicopterAccess;
    }

    public boolean isTeachingHospital() {
        return this.teachingHospital;
    }

    public void setTeachingHospital(boolean teachingHospital) {
        this.teachingHospital = teachingHospital;
    }
}