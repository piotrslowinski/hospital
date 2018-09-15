package pl.com.britenet.hospital.dto;

import java.time.LocalDate;

import pl.com.britenet.hospital.domain.DoctorAssignment;


public class DoctorDto {

    private Long id;

    private String name;

    private String surname;

    private String title;

    private LocalDate contractStartDate;

    private LocalDate contractEndDate;

    public DoctorDto(DoctorAssignment assignment) {
        this.id = assignment.getDoctor().getId();
        this.name = assignment.getDoctor().getName();
        this.surname = assignment.getDoctor().getSurname();
        this.title = assignment.getDoctor().getTitle();
        this.contractStartDate = assignment.getContractStartDate();
        this.contractEndDate = assignment.getContractEndDate();
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

    public String getSurname() {
        return this.surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDate getContractStartDate() {
        return this.contractStartDate;
    }

    public void setContractStartDate(LocalDate contractStartDate) {
        this.contractStartDate = contractStartDate;
    }

    public LocalDate getContractEndDate() {
        return this.contractEndDate;
    }

    public void setContractEndDate(LocalDate contractEndDate) {
        this.contractEndDate = contractEndDate;
    }
}
