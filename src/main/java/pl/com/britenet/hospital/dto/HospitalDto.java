package pl.com.britenet.hospital.dto;

import pl.com.britenet.hospital.domain.DoctorAssignment;
import pl.com.britenet.hospital.domain.Hospital;

import java.util.Collection;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class HospitalDto {

    private Long id;

    private String name;

    private String country;

    private String town;

    private String street;

    private String postalCode;

    private String phoneNumber;

    private String faxNumber;

    private int numberOfAmbulances;

    private boolean helicopterAccess;

    private boolean teachingHospital;

    private Collection<DoctorDto> doctors;

    public HospitalDto(Hospital hospital) {
        this.id = hospital.getId();
        this.name = hospital.getName();
        this.country = hospital.getCountry();
        this.town = hospital.getTown();
        this.street = hospital.getStreet();
        this.postalCode = hospital.getPostalCode();
        this.phoneNumber = hospital.getPhoneNumber();
        this.faxNumber = hospital.getFaxNumber();
        this.numberOfAmbulances = hospital.getNumberOfAmbulances();
        this.helicopterAccess = hospital.isHelicopterAccess();
        this.teachingHospital = hospital.isTeachingHospital();
        this.doctors = hospital.getDoctorAssignments().stream().map(DoctorDto::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFaxNumber() {
        return faxNumber;
    }

    public void setFaxNumber(String faxNumber) {
        this.faxNumber = faxNumber;
    }

    public int getNumberOfAmbulances() {
        return numberOfAmbulances;
    }

    public void setNumberOfAmbulances(int numberOfAmbulances) {
        this.numberOfAmbulances = numberOfAmbulances;
    }

    public boolean isHelicopterAccess() {
        return helicopterAccess;
    }

    public void setHelicopterAccess(boolean helicopterAccess) {
        this.helicopterAccess = helicopterAccess;
    }

    public boolean isTeachingHospital() {
        return teachingHospital;
    }

    public void setTeachingHospital(boolean teachingHospital) {
        this.teachingHospital = teachingHospital;
    }

    public Collection<DoctorDto> getDoctors() {
        return doctors;
    }

    public void setDoctors(Collection<DoctorDto> doctors) {
        this.doctors = doctors;
    }
}
