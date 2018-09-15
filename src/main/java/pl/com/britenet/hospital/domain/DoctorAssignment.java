package pl.com.britenet.hospital.domain;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "hospital_doctors")
public class DoctorAssignment {

    private static final LocalDate MAX_DATE = LocalDate.parse("9999-01-01");

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Hospital hospital;

    @ManyToOne
    @JoinColumn(name = "doctor_id")
    private Doctor doctor;

    @Column(name = "contract_start_date")
    private LocalDate contractStartDate;

    @Column(name = "contract_end_date")
    private LocalDate contractEndDate;

    private String position;

    private String supervisor;

    @Column(name = "part_time")
    private boolean partTime;

    public DoctorAssignment() {
    }

    public DoctorAssignment(Hospital hospital, Doctor doctor) {
        this.hospital = hospital;
        this.doctor = doctor;
        this.contractStartDate = LocalDate.now();
        this.contractEndDate = MAX_DATE;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Hospital getHospital() {
        return this.hospital;
    }

    public void setHospital(Hospital hospital) {
        this.hospital = hospital;
    }

    public Doctor getDoctor() {
        return this.doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
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

    public String getPosition() {
        return this.position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSupervisor() {
        return this.supervisor;
    }

    public void setSupervisor(String supervisor) {
        this.supervisor = supervisor;
    }

    public boolean isPartTime() {
        return this.partTime;
    }

    public void setPartTime(boolean partTime) {
        this.partTime = partTime;
    }
}
