package pl.com.britenet.hospital.repository;

import java.util.Optional;

import pl.com.britenet.hospital.domain.Doctor;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    Optional<Doctor> findByLicenseNumber(Long licenseNumber);
}
