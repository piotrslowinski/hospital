package pl.com.britenet.hospital.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.repository.HospitalRepository;

import java.util.Optional;

@Service
public class HospitalService {

    private final HospitalRepository hospitalRepository;

    @Autowired
    public HospitalService(HospitalRepository hospitalRepository) {
        this.hospitalRepository = hospitalRepository;
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

    private void saveHospital(Hospital hospital) {
        this.hospitalRepository.save(hospital);
    }
}
