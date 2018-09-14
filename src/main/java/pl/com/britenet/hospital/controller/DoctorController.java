package pl.com.britenet.hospital.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.service.DoctorService;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;


@RestController
@RequestMapping("/doctors")
public class DoctorController {

    private final DoctorService doctorService;

    @Autowired
    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    @PostMapping
    public ResponseEntity<Doctor> addNewDoctor(@RequestBody Doctor doctor) {
        Doctor newDoc;
        try {
            newDoc = this.doctorService.addNewDoctor(doctor);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(newDoc, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Doctor> updateDoctor(@PathVariable("id") Long doctorId, @RequestBody Doctor doctor) {
        Doctor updatedDoc;
        try {
            updatedDoc = this.doctorService.updateDoctor(doctorId, doctor);
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(updatedDoc, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Doctor> getDoctorById(@PathVariable("id") Long doctorId) {
        Doctor doctor;
        try {
            doctor = this.doctorService.findDoctorById(doctorId).get();
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity(doctor, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<Doctor>> getAllDoctors() {
        List<Doctor> doctors;
        try {
            doctors = this.doctorService.getAllDoctors();
        } catch (NoSuchElementException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity(doctors, HttpStatus.OK);
    }
}
