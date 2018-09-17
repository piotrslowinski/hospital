package pl.com.britenet.hospital.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.com.britenet.hospital.domain.Doctor;
import pl.com.britenet.hospital.domain.DoctorAssignment;
import pl.com.britenet.hospital.domain.Hospital;
import pl.com.britenet.hospital.repository.DoctorRepository;
import pl.com.britenet.hospital.repository.HospitalRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.Silent.class)
public class HospitalServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @Mock
    private HospitalRepository hospitalRepository;

    private HospitalService hospitalService;

    private Doctor sampleDoctor;

    private Hospital sampleHospital;

    @Before
    public void setUp() {
        sampleDoctor = new Doctor("John", "Doe", 1L);
        sampleHospital = new Hospital("Hospital 1", "Poland", "Lublin");

        hospitalService = new HospitalService(hospitalRepository, doctorRepository);
    }

    @Test
    public void shouldAddNewHospital() {
        //given
        when(hospitalRepository.findByName(sampleDoctor.getName())).thenReturn(Optional.empty());

        //when
        Hospital hospital = this.hospitalService.createHospital(sampleHospital);

        //then
        assertEquals("Hospital 1", hospital.getName());
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowErrorWhenAddSameHospitalName() {
        //given
        when(hospitalRepository.findByName("Hospital 1")).thenReturn(Optional.of(sampleHospital));

        //when
        Hospital hospital = this.hospitalService.createHospital(
                new Hospital("Hospital 1", "USA", "New York")
        );
    }

    @Test
    public void shouldUpdateHospital() {
        //given
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));

        //when
        Hospital hospital = this.hospitalService.updateHospital(1L,
                new Hospital("New hospital", "USA", "New York"));

        //then
        assertEquals("New hospital", hospital.getName());
        assertEquals("USA", hospital.getCountry());
        assertEquals("New York", hospital.getTown());
    }

    @Test
    public void shouldFindHospitalById() {
        //given
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));

        //when
        Hospital hospital = this.hospitalService.findHospitalById(1L).get();

        //then
        assertEquals("Hospital 1", hospital.getName());
        assertEquals("Poland", hospital.getCountry());
        assertEquals("Lublin", hospital.getTown());
    }

    @Test(expected = NoSuchElementException.class)
    public void shouldThrowErrorWhenNoHospitalWithSpecifiedId() {
        //given
        when(hospitalRepository.findById(1L)).thenReturn(Optional.empty());

        //when
        this.hospitalService.findHospitalById(1L).get();
    }

    @Test
    public void shouldReturnAllHospitalsList() {
        //given
        Hospital newHospital = new Hospital("New hospital", "USA", "New York");
        when(hospitalRepository.findAll()).thenReturn(Arrays.asList(sampleHospital, newHospital));

        //when
        List<Hospital> hospitals = this.hospitalService.getAllHospitals();

        //then
        assertEquals(Arrays.asList(sampleHospital, newHospital), hospitals);
        assertEquals(2, hospitals.size());
    }

    @Test
    public void shouldReturnEmptyListWhenNoHospitalInRepository() {
        //given
        when(hospitalRepository.findAll()).thenReturn(Collections.emptyList());

        //when
        List<Hospital> hospitals = this.hospitalService.getAllHospitals();

        //then
        assertEquals(0, hospitals.size());
        assertEquals(Collections.emptyList(), hospitals);
    }

    @Test
    public void shouldAssignDoctorToHospital() {
        //given
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        //when
        this.hospitalService.assignNewDoctor(1L, 1L);

        //then
        Collection<DoctorAssignment> assignments = sampleHospital.getDoctorAssignments();
        assertEquals(1, assignments.size());
        assertEquals(LocalDate.parse("9999-01-01"), assignments
                .stream()
                .map(DoctorAssignment::getContractEndDate).findFirst().get());
        assertEquals(sampleDoctor, assignments
                .stream()
                .map(DoctorAssignment::getDoctor)
                .findFirst()
                .get()
        );
    }

    @Test
    public void shouldAssignMultipleDoctorToHospital() {
        //given
        Doctor newDoctor = new Doctor("Andrzej", "Nowak", 2L);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        this.hospitalService.assignNewDoctor(1L, 1L);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(newDoctor));

        //when
        this.hospitalService.assignNewDoctor(1L, 2L);

        //then
        assertEquals(2, sampleHospital.getDoctorAssignments().size());
        assertEquals(Arrays.asList(sampleDoctor, newDoctor), sampleHospital
                .getDoctorAssignments()
                .stream()
                .map(DoctorAssignment::getDoctor)
                .collect(Collectors.toList())
        );
    }

    @Test
    public void shouldSetContractEndDateWhenUnassignDoctorFromHospital() {
        //given
        Doctor doctorToFire = new Doctor("Andrzej", "Nowak", 2L);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        this.hospitalService.assignNewDoctor(1L, 1L);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctorToFire));

        this.hospitalService.assignNewDoctor(1L, 2L);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctorToFire));
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));

        //when
        this.hospitalService.unassignTheDoctorFromHospital(1L, 2L);

        //then
        Collection<DoctorAssignment> assignments = sampleHospital.getDoctorAssignments();

        assertEquals(Arrays.asList(doctorToFire), assignments
                .stream()
                .filter((assignment) -> assignment
                        .getContractEndDate()
                        .isEqual(LocalDate.now()))
                .map(DoctorAssignment::getDoctor)
                .collect(Collectors.toList()));
        assertEquals(LocalDate.now(), assignments
                .stream()
                .filter((a) -> a.getDoctor()
                        .equals(doctorToFire))
                .map(DoctorAssignment::getContractEndDate)
                .findFirst()
                .get()
        );
    }

    @Test
    public void shouldNotDeleteDoctorWhenUnassignDoctorFromHospital() {
        //given
        Doctor doctorToFire = new Doctor("Andrzej", "Nowak", 2L);
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));
        when(doctorRepository.findById(1L)).thenReturn(Optional.of(sampleDoctor));

        this.hospitalService.assignNewDoctor(1L, 1L);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctorToFire));

        this.hospitalService.assignNewDoctor(1L, 2L);

        when(doctorRepository.findById(2L)).thenReturn(Optional.of(doctorToFire));
        when(hospitalRepository.findById(1L)).thenReturn(Optional.of(sampleHospital));

        //when
        this.hospitalService.unassignTheDoctorFromHospital(1L, 2L);

        //then
        Collection<DoctorAssignment> assignments = sampleHospital.getDoctorAssignments();

        assertEquals(2, assignments.size());
        assertEquals(Arrays.asList(sampleDoctor, doctorToFire), assignments
                .stream()
                .map(DoctorAssignment::getDoctor)
                .collect(Collectors.toList())
        );
    }
}
