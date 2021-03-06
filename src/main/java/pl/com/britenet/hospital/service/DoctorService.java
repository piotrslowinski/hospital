package pl.com.britenet.hospital.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.repository.DoctorRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DoctorService {

    private static final Logger log = LoggerFactory.getLogger(DoctorService.class);

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor addNewDoctor(Doctor doctor) {
        Optional<Doctor> doctorOptional = findDoctorByLicenseNumber(doctor.getLicenseNumber());
        if (doctorOptional.isPresent()) {
            throw new IllegalArgumentException("doctor with this license number already exists");
        } else if (!isFieldValid("licenseNumber", doctor.getLicenseNumber())) {
            log.debug("field validation error");
        }
        Doctor newDoctor = new Doctor(
                doctor.getName(),
                doctor.getSurname(),
                doctor.getTitle(),
                doctor.getLicenseNumber(),
                doctor.getPhone(),
                doctor.getEmail(),
                doctor.getNationality(),
                doctor.getSpeciality(),
                doctor.getDateOfBirth(),
                doctor.isTeacher()
        );
        saveDoctor(newDoctor);
        return newDoctor;
    }

    public Doctor updateDoctor(Long doctorId, Doctor doctor) {
        Optional<Doctor> doctorOptional = findDoctorById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw new NoSuchElementException("doctor with this id doesn't exist");
        }
        Doctor doctorToUpdate = doctorOptional.get();
        doctorToUpdate.setName(doctor.getName());
        doctorToUpdate.setSurname(doctor.getSurname());
        doctorToUpdate.setEmail(doctor.getEmail());
        saveDoctor(doctorToUpdate);
        return doctorToUpdate;
    }

    private void saveDoctor(Doctor doctor) {
        this.doctorRepository.save(doctor);
    }

    public Optional<Doctor> findDoctorById(Long doctorId) {
        return this.doctorRepository.findById(doctorId);
    }

    private Optional<Doctor> findDoctorByLicenseNumber(Long licenseNumber) {
        return this.doctorRepository.findByLicenseNumber(licenseNumber);
    }

    public List<Doctor> getAllDoctors() {
        List<Doctor> doctors = this.doctorRepository.findAll();
        if (doctors.isEmpty()) {
            throw new NoSuchElementException("repository is empty");
        }
        return doctors;
    }

    public void deleteDoctor(Long doctorId) {
        Optional<Doctor> doctorOptional = findDoctorById(doctorId);
        if (!doctorOptional.isPresent()) {
            throw new NoSuchElementException("doctor with this id doesn't exist");
        }
        this.doctorRepository.delete(doctorOptional.get());
    }

    private boolean isFieldValid(String field, Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException(String.format("the %s field has not been filled out", field));
        }
        return true;
    }
}
