package pl.com.britenet.hospital.dto;

import pl.com.britenet.hospital.domain.DoctorAssignment;

public class DoctorDto {

    private Long id;

    private String name;

    private String surname;

    private String title;

    public DoctorDto(DoctorAssignment assignment) {
        this.id = assignment.getDoctor().getId();
        this.name = assignment.getDoctor().getName();
        this.surname = assignment.getDoctor().getSurname();
        this.title = assignment.getDoctor().getTitle();
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

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
