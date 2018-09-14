package pl.com.britenet.hospital.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.com.britenet.hospital.domain.Doctor;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

}
