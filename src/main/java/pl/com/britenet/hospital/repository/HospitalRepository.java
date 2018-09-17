package pl.com.britenet.hospital.repository;

import java.util.Optional;

import pl.com.britenet.hospital.domain.Hospital;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HospitalRepository extends JpaRepository<Hospital, Long> {

    Optional<Hospital> findByName(String name);
}
