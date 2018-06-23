import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import parking.Application;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlateRepository plateRepository;

    @Test
    public void shouldGreetDriver() throws Exception {
        this.mockMvc.perform(get("/driver")).andExpect(status().isOk())
                .andExpect(content().string(containsString("driver")));
    }

    @Test
    public void shouldGreetOperator() throws Exception {
        this.mockMvc.perform(get("/operator")).andExpect(status().isOk())
                .andExpect(content().string(containsString("operator")));
    }

    @Test
    public void shouldGreetOwner() throws Exception {
        this.mockMvc.perform(get("/owner")).andExpect(status().isOk())
                .andExpect(content().string(containsString("Owner")));
    }

    @Test
    public void shouldContainPlateAttribute() throws Exception {
        mockMvc.perform(get("/driver"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plate"));
    }

    @Test
    public void shouldContainPlateNrWithinAttribute() throws Exception {
        mockMvc.perform(get("/driver"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plate", hasProperty("plateNr")));
    }

    @Test
    public void shouldContainPlateNrInParameters() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plate", hasProperty("plateNr", is(EXAMPLE_PLATE_NR))));
    }

    @Test
    public void shouldContainPlateNrInModel() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plateNr"));
    }

    @Test
    public void shouldContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plateNr", containsString(EXAMPLE_PLATE_NR)));
    }

    @Test
    public void shouldRepositoryContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/addPlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()));
        assertTrue(plateRepository.findByPlateNr(EXAMPLE_PLATE_NR).isPresent());
    }

    @Test
    public void shouldRepositoryNotContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("Plate number not found")));
    }
}