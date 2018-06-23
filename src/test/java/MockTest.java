import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import parking.Application;
import parking.entities.Plate;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
public class MockTest {

    @Autowired
    private MockMvc mockMvc;

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
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR).sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plate", hasProperty("plateNr", is(EXAMPLE_PLATE_NR))));
    }

    @Test
    public void shouldContainPlateNrInBody() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR).sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXAMPLE_PLATE_NR)));
    }

    @Test
    public void shouldContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "ASDASD";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR).sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(EXAMPLE_PLATE_NR)))
                .andExpect(content().string(containsString("Plate found!")));
    }
}