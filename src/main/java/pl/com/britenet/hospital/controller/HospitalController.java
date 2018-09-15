package pl.com.britenet.hospital.controller;

import java.util.List;
import java.util.NoSuchElementException;

import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.dto.HospitalDto;
import pl.com.britenet.hospital.service.HospitalService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/hospitals")
public class HospitalController {

    private final HospitalService hospitalService;

    @Autowired
    public HospitalController(HospitalService hospitalService) {
        this.hospitalService = hospitalService;
    }

    @PostMapping
    public ResponseEntity<Hospital> addNewHospital(@RequestBody Hospital hospital) {
        Hospital newHospital = null;
        try {
            newHospital = this.hospitalService.createHospital(hospital);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(newHospital, HttpStatus.OK);
    }

    @PutMapping("/{hospitalId}")
    public ResponseEntity<Hospital> updateHospital(@PathVariable Long hospitalId, @RequestBody Hospital hospital) {
        Hospital updatedHospital = null;
        try {
            updatedHospital = this.hospitalService.updateHospital(hospitalId, hospital);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(updatedHospital, HttpStatus.OK);
    }

    @GetMapping("/{hospitalId}")
    public ResponseEntity<HospitalDto> getHospitalById(@PathVariable Long hospitalId) {
        HospitalDto hospitalDto;
        Hospital hospital;
        try {
            hospital = this.hospitalService.findHospitalById(hospitalId).get();
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        hospitalDto = new HospitalDto(hospital);
        return new ResponseEntity(hospitalDto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Hospital>> getAllHospitals() {
        List<Hospital> hospitals;
        try {
            hospitals = this.hospitalService.getAllHospitals();
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(hospitals, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteHospital(@PathVariable Long id) {
        try {
            this.hospitalService.deleteHospital(id);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity("hospital deleted", HttpStatus.OK);
    }

    @PutMapping("/{hospitalId}/doctor/{doctorId}")
    public ResponseEntity<String> assignDoctorToHospital(@PathVariable Long hospitalId, @PathVariable Long doctorId) {
        try {
            this.hospitalService.assignNewDoctor(hospitalId, doctorId);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity("success", HttpStatus.OK);
    }

    @DeleteMapping("/{hospitalId}/doctor/{doctorId}")
    public ResponseEntity<String> unassignDoctorFromHospital(@PathVariable Long hospitalId, @PathVariable Long doctorId) {
        try {
            this.hospitalService.unassignTheDoctorFromHospital(hospitalId, doctorId);
        }
        catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
        return new ResponseEntity("success", HttpStatus.OK);
    }
}
