package pl.com.britenet.hospital.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.repository.HospitalRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


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
}
