package pl.com.britenet.hospital.service;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.domain.DoctorAssignment;
import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.repository.DoctorRepository;
import pl.com.britenet.hospital.repository.HospitalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class HospitalService {

    private static final Logger log = LoggerFactory.getLogger(HospitalService.class);

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
        } else if (!isFieldValid("name", hospital.getName())) {
            log.debug("field validation error");
        }
        Hospital newHospital = new Hospital(
                hospital.getName(),
                hospital.getCountry(),
                hospital.getTown(),
                hospital.getStreet(),
                hospital.getPostalCode(),
                hospital.getPhoneNumber(),
                hospital.getFaxNumber(),
                hospital.getNumberOfAmbulances(),
                hospital.isHelicopterAccess(),
                hospital.isTeachingHospital()
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
        Hospital hospitalToUpdate = findHospitalById(hospitalId).get();
        if (!(hospital.getName() == null)) {
            hospitalToUpdate.setName(hospital.getName());
        }
        if (!(hospital.getCountry() == null)) {
            hospitalToUpdate.setCountry(hospital.getCountry());
        }
        if (!(hospital.getTown() == null)) {
            hospitalToUpdate.setTown(hospital.getTown());
        }
        if (!(hospital.getStreet() == null)) {
            hospitalToUpdate.setStreet(hospital.getStreet());
        }
        if (!(hospital.getPostalCode() == null)) {
            hospitalToUpdate.setPostalCode(hospital.getPostalCode());
        }
        if (!(hospital.getPhoneNumber() == null)) {
            hospitalToUpdate.setPhoneNumber(hospital.getPhoneNumber());
        }
        if (!(hospital.getFaxNumber() == null)) {
            hospitalToUpdate.setFaxNumber(hospital.getFaxNumber());
        }
        if (!(hospital.getNumberOfAmbulances() == 0)) {
            hospitalToUpdate.setNumberOfAmbulances(hospital.getNumberOfAmbulances());
        }
        hospitalToUpdate.setHelicopterAccess(hospital.isHelicopterAccess());
        hospitalToUpdate.setTeachingHospital(hospital.isTeachingHospital());
        saveHospital(hospitalToUpdate);
        return hospitalToUpdate;
    }

    public Optional<Hospital> findHospitalById(Long hospitalId) {
        Optional<Hospital> hospitalOptional = this.hospitalRepository.findById(hospitalId);
        if (!hospitalOptional.isPresent()) {
            throw new NoSuchElementException("hospital with this id doesn't exist");
        }
        return hospitalOptional;
    }

    public List<Hospital> getAllHospitals() {
        return this.hospitalRepository.findAll();
    }

    public void deleteHospital(Long hospitalId) {
        Hospital hospital = findHospitalById(hospitalId).get();
        this.hospitalRepository.delete(hospital);
    }

    public void assignNewDoctor(Long hospitalId, Long doctorId) {
        Doctor doctor = findDoctorById(doctorId).get();
        Hospital hospital = findHospitalById(hospitalId).get();

        Optional<DoctorAssignment> assignmentOptional = getCurrentAssignments(hospital, doctor);
        if (assignmentOptional.isPresent()) {
            throw new IllegalArgumentException("doctor already works in this hospital");
        }

        assignDoctor(hospital, doctor);
        saveHospital(hospital);
    }

    public Optional<Doctor> findDoctorById(Long doctorId) {
        Optional<Doctor> doctorOptional = this.doctorRepository.findById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw new NoSuchElementException("doctor with this id doesn't exist");
        }
        return doctorOptional;
    }

    private void assignDoctor(Hospital hospital, Doctor doctor) {
        DoctorAssignment newAssignment = new DoctorAssignment(hospital, doctor);
        Collection<DoctorAssignment> assignments = hospital.getDoctorAssignments();
        assignments.add(newAssignment);
    }

    public void unassignTheDoctorFromHospital(Long hospitalId, Long doctorId) {
        Doctor doctor = findDoctorById(doctorId).get();
        Hospital hospital = findHospitalById(hospitalId).get();

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

    private Optional<DoctorAssignment> getCurrentAssignments(Hospital hospital, Doctor doctor) {
        return hospital.getDoctorAssignments().stream()
                .filter((a) -> a.getDoctor()
                        .equals(doctor)).filter((assignment -> isAssignmentCurrent(assignment))).findFirst();
    }

    private boolean isAssignmentCurrent(DoctorAssignment assignment) {
        return (assignment.getContractEndDate().isAfter(LocalDate.now()) &&
                assignment.getContractStartDate().isBefore(assignment.getContractEndDate()));
    }

    private boolean isFieldValid(String field, String value) {
        if (value == null || value.trim().length() == 0) {
            throw new IllegalArgumentException(String.format("the %s field has not been filled out", field));
        }
        return true;
    }
}
