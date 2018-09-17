package pl.com.britenet.hospital.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import pl.com.britenet.hospital.Application;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;

@Transactional
@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@TestPropertySource("classpath:application-test.properties")
public class IntegrationTests {

    private static final String TRUNCATE_SCHEMA_QUERY = "TRUNCATE SCHEMA PUBLIC RESTART IDENTITY AND COMMIT NO CHECK";

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private MockMvc mvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Before
    public void cleanDb() {
        clean();
    }


    @Test
    public void successfulDoctorSave() throws Exception {
        saveDoctor(1L).andExpect(status().isOk());
    }

    @Test
    public void noLicenseNumberWhenDoctorSave() throws Exception {
        saveDoctor(null).andExpect(status().isBadRequest());
    }

    @Test
    public void tryingToSaveDoctorWithTheSameLicenseNumber() throws Exception {
        saveDoctor(1L);
        saveDoctor(1L).andExpect(status().isBadRequest());
    }

    @Test
    public void successfullHospitalSave() throws Exception {
        saveHospital("HospitalName").andExpect(status().isOk());
    }

    @Test
    public void noNameWhenSaveHospital() throws Exception {
        saveHospital(null).andExpect(status().isBadRequest());
    }

    @Test
    public void tryingToSaveHospitalWithTheSameName() throws Exception {
        saveHospital("HospitalName");
        saveHospital("HospitalName").andExpect(status().isBadRequest());
    }

    @Test
    public void successfulDoctorAssignement() throws Exception {
        saveHospital("HospitalName");
        saveDoctor(1L);

        assignDoctorToHospital(1L, 1L).andExpect(status().isOk());
    }

    @Test
    public void successfulDoctorUnassignement() throws Exception {
        saveHospital("HospitalName");
        saveDoctor(1L);
        assignDoctorToHospital(1L, 1L);

        unassignDoctorFromHospital(1L, 1L).andExpect(status().isOk());
    }

    private ResultActions unassignDoctorFromHospital(long hospitalId, long doctorId) throws Exception {
        return mvc.perform(delete("/hospitals/" + hospitalId + "/doctor/" +doctorId)
                .contentType(MediaType.APPLICATION_JSON));
    }


    private ResultActions assignDoctorToHospital(long hospitalId, long doctorId) throws Exception {
        return mvc.perform(put("/hospitals/" + hospitalId + "/doctor/" +doctorId)
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions saveHospital(String hospitalName) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("name", hospitalName);
        return mvc.perform(post("/hospitals").content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON));
    }

    private ResultActions saveDoctor(Long licenseNumber) throws Exception {
        Map<String, Object> body = new HashMap<>();
        body.put("licenseNumber", licenseNumber);
        return mvc.perform(post("/doctors").content(objectMapper.writeValueAsString(body))
                .contentType(MediaType.APPLICATION_JSON));
    }

    public void clean() {
        entityManager.createNativeQuery(TRUNCATE_SCHEMA_QUERY).executeUpdate();
    }
}
