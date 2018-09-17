package pl.com.britenet.hospital.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.repository.DoctorRepository;

import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    private DoctorService doctorService;

    private Doctor sampleDoctor;

    @Before
    public void setUp() {
        sampleDoctor = new Doctor("Jan", "Nowak", 1L);
        doctorService = new DoctorService(doctorRepository);
    }

    @Test
    public void shouldAddNewNote() {
        //given
        when(doctorRepository.findByLicenseNumber(sampleDoctor.getLicenseNumber())).thenReturn(Optional.empty());

        //when
        Doctor doctor = this.doctorService.addNewDoctor(sampleDoctor);

        //then
        assertEquals("Jan", doctor.getName());
        assertEquals(Long.valueOf(1), doctor.getLicenseNumber());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorWhenAddSameLicenseNumber() {
        //given
        when(doctorRepository.findByLicenseNumber(sampleDoctor.getLicenseNumber())).thenReturn(Optional.of(sampleDoctor));

        //when
        Doctor doctor = this.doctorService.addNewDoctor(new Doctor("Zygmunt", "Nowak", 1L));
    }

    @Test
    public void shouldUpdateDoctor() {
        //given
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        //when
        Doctor doctor = this.doctorService.updateDoctor(1L,
                new Doctor("Jerzy", "Nowak", 1L));

        //then
        assertEquals("Jerzy", doctor.getName());
        assertEquals(Long.valueOf(1), doctor.getLicenseNumber());
    }

    @Test
    public void shouldReturnDoctorById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        //when
        Doctor doctor = this.doctorService.findDoctorById(1L).get();

        //then
        assertEquals("Jan", doctor.getName());
        assertEquals(Long.valueOf(1), doctor.getLicenseNumber());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowErrorWhenNoDoctorToReturnById() {
        when(doctorRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        this.doctorService.findDoctorById(1L).get();
    }

    @Test
    public void shouldReturnListOfDoctors() {
        Doctor newDoctor = new Doctor("Andrzej", "Nowak", 2L);

        when(doctorRepository.findAll()).thenReturn(Arrays.asList(sampleDoctor, newDoctor));

        //when
        List<Doctor> doctors = this.doctorService.getAllDoctors();

        //then
        assertEquals(2, doctors.size());
        assertEquals(Arrays.asList(sampleDoctor, newDoctor), doctors);
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowErrorWhenDoctorListIsEmpty() {
        when(doctorRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        this.doctorService.getAllDoctors();
    }
}
