package pl.com.britenet.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.repository.DoctorRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class DoctorService {

    private final DoctorRepository doctorRepository;

    @Autowired
    public DoctorService(DoctorRepository doctorRepository) {
        this.doctorRepository = doctorRepository;
    }

    public Doctor addNewDoctor(Doctor doctor) {
        Optional<Doctor> doctorOptional = findDoctorByLicenseNumber(doctor.getLicenseNumber());
        if (doctorOptional.isPresent()) {
            throw new IllegalArgumentException("doctor with this license number already exists");
        }
        Doctor newDoctor = new Doctor(
                doctor.getName(),
                doctor.getSurname(),
                doctor.getLicenseNumber()
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
}
