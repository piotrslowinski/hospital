package pl.com.britenet.hospital.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.domain.DoctorAssignment;
import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.repository.DoctorRepository;
import pl.com.britenet.hospital.repository.HospitalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;


@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;
    private final DoctorRepository doctorRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository, DoctorRepository doctorRepository) {
        this.hospitalRepository = hospitalRepository;
        this.doctorRepository = doctorRepository;
    }

    public Hospital createHospital(Hospital hospital) {
        Optional<Hospital> hospitalOptional = findHospitalByName(hospital.getName());
        if (hospitalOptional.isPresent()) {
            throw new IllegalArgumentException("hospital with this name already exists");
        }
        Hospital newHospital = new Hospital(
                hospital.getName(),
                hospital.getCountry(),
                hospital.getTown()
        );
        saveHospital(newHospital);
        return newHospital;
    }

    private Optional<Hospital> findHospitalByName(String name) {
        return this.hospitalRepository.findByName(name);
    }

    @Transactional
    private void saveHospital(Hospital hospital) {
        this.hospitalRepository.save(hospital);
    }

    public Hospital updateHospital(Long hospitalId, Hospital hospital) {
        Optional<Hospital> hospitalOptional = findHospitalById(hospitalId);
        if (!hospitalOptional.isPresent()) {
            throw new NoSuchElementException("hospital with this id doesn't exist");
        }
        Hospital hospitalToUpdate = hospitalOptional.get();
        hospitalToUpdate.setName(hospital.getName());
        saveHospital(hospitalToUpdate);
        return hospitalToUpdate;
    }

    public Optional<Hospital> findHospitalById(Long hospitalId) {
        return this.hospitalRepository.findById(hospitalId);
    }

    public List<Hospital> getAllHospitals() {
        return this.hospitalRepository.findAll();
    }

    public void deleteHospital(Long hospitalId) {
        Optional<Hospital> hospitalOptional = findHospitalById(hospitalId);
        if (!hospitalOptional.isPresent()) {
            throw new NoSuchElementException("hospital with this id doesn't exist");
        }
        this.hospitalRepository.delete(hospitalOptional.get());
    }

    public void assignNewDoctor(Long hospitalId, Long doctorId) {
        Optional<Hospital> hospitalOptional = findHospitalById(hospitalId);
        if (!hospitalOptional.isPresent()) {
            throw new NoSuchElementException("hospital with this id doesn't exist");
        }
        Optional<Doctor> doctorOptional = this.doctorRepository.findById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw new NoSuchElementException("doctor with this id doesn't exist");
        }
        Doctor doctor = doctorOptional.get();
        Hospital hospital = hospitalOptional.get();
//
//        Collection<DoctorAssignment> assignments = hospital.getDoctorAssignments();

        Optional<DoctorAssignment> assignmentOptional = getCurrentAssignments(hospital, doctor);
        if (assignmentOptional.isPresent()) {
            throw new IllegalArgumentException("doctor already works in this hospital");
        }
//        DoctorAssignment newAssignment = new DoctorAssignment(hospital, doctor);
//        assignments.add(newAssignment);

        assignDoctor(hospital, doctor);
        saveHospital(hospital);
    }

    private void assignDoctor(Hospital hospital, Doctor doctor) {
        DoctorAssignment newAssignment = new DoctorAssignment(hospital, doctor);
        Collection<DoctorAssignment> assignments = hospital.getDoctorAssignments();
        assignments.add(newAssignment);
    }

    public void unassignTheDoctorFromHospital(Long hospitalId, Long doctorId) {
        Optional<Hospital> hospitalOptional = findHospitalById(hospitalId);
        if (!hospitalOptional.isPresent()) {
            throw new NoSuchElementException("hospital with this id doesn't exist");
        }
        Optional<Doctor> doctorOptional = this.doctorRepository.findById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw new NoSuchElementException("doctor with this id doesn't exist");
        }
        Doctor doctor = doctorOptional.get();
        Hospital hospital = hospitalOptional.get();

        Optional<DoctorAssignment> assignmentOptional = getCurrentAssignments(hospital, doctor);
        if (!assignmentOptional.isPresent()) {
            throw new IllegalArgumentException("doctor doesn't work in this hospital");
        }
        unassignDoctor(assignmentOptional.get());
        saveHospital(hospital);
    }

    private void unassignDoctor(DoctorAssignment assignment) {
        assignment.setContractEndDate(LocalDate.now());
    }

    private void checkIfDoctorIsAssigned(Collection<DoctorAssignment> assignments, Doctor doctor) {
        Optional<DoctorAssignment> assignementOptional = assignments.stream().filter((a) -> a.getDoctor()
                .equals(doctor)).findAny();
        if (assignementOptional.isPresent()) {
            throw new IllegalArgumentException("doctor already works in this hospital");
        }

    }

    private Optional<DoctorAssignment> getCurrentAssignments(Hospital hospital, Doctor doctor) {
        return hospital.getDoctorAssignments().stream()
                .filter((a) -> a.getDoctor()
                .equals(doctor)).filter((assignment -> isAssignmentCurrent(assignment))).findFirst();
    }

    private boolean isAssignmentCurrent(DoctorAssignment assignment) {
        return (assignment.getContractEndDate().isAfter(LocalDate.now())&&
                assignment.getContractStartDate().isBefore(assignment.getContractEndDate()));
    }
}
