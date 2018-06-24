import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import parking.Application;
import parking.entities.Plate;
import parking.repositories.PlateRepository;

import java.time.Instant;

import static junit.framework.TestCase.assertNotNull;
import static junit.framework.TestCase.assertTrue;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@AutoConfigureMockMvc
@PropertySource("classpath:messages.properties")
public class MockTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlateRepository plateRepository;

    @Autowired
    private Environment env;

    @Before
    public void setUp() {
        plateRepository.deleteAll();
    }

    @Test
    public void shouldGreetDriver() throws Exception {
        this.mockMvc.perform(get("/driver")).andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("driver.title"))));
    }

    @Test
    public void shouldGreetOperator() throws Exception {
        this.mockMvc.perform(get("/operator")).andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("operator.title"))));
    }

    @Test
    public void shouldGreetOwner() throws Exception {
        this.mockMvc.perform(get("/owner")).andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("owner.title"))));
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
        final String EXAMPLE_PLATE_NR = "12345678";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plate", hasProperty("plateNr", is(EXAMPLE_PLATE_NR))));
    }

    @Test
    public void shouldContainPlateNrInModel() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plateNr"));
    }

    @Test
    public void shouldContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plateNr", containsString(EXAMPLE_PLATE_NR)));
    }

    @Test
    public void shouldRepositoryContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        mockMvc.perform(post("/addPlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()));
        assertTrue(!plateRepository.findByPlateNr(EXAMPLE_PLATE_NR).isEmpty());
    }

    @Test
    public void shouldRepositoryNotContainExamplePlate() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("plateNotFound.plateNotFound"))));
    }

    @Test
    public void shouldNotFindPlatesByDriverWhereEndIsNotNull() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plate.setEnd(Instant.now());

        plateRepository.save(plate);

        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("plateNotFound.plateNotFound"))));
    }

    @Test
    public void shouldFindOpenPlatesByDriverWhereEndIsNull() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);

        plateRepository.save(plate);

        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("plateFound.plateFound"))));
    }

    @Test
    public void shouldOperatorContainPlateParam() throws Exception {
        mockMvc.perform(get("/operator"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plate"));
    }

    @Test
    public void shouldOperatorContainPlateNrParam() throws Exception {
        mockMvc.perform(get("/operator"))
                .andExpect(status().isOk())
                .andExpect(model().attribute("plate", hasProperty("plateNr")));
    }

    @Test
    public void shouldOperatorSearchPlatePageContainPlateNrParam() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";

        mockMvc.perform(post("/operatorPlateSearch").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plateNr"));
    }

    @Test
    public void shouldOperatorSearchPlatePageFindPlateWithGivenNr() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plateRepository.save(plate);

        mockMvc.perform(post("/operatorPlateSearch").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("operatorPlateFound.plateFound"))));
    }

    @Test
    public void shouldOperatorSearchPlatePageNotFindPlateWithGivenNr() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";

        mockMvc.perform(post("/operatorPlateSearch").param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("operatorPlateNotFound.plateNotFound"))));
    }

    @Test
    public void shouldDriverStopMeter() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plate.setStart(Instant.now());
        plateRepository.save(plate);

        mockMvc.perform(post("/stopAndPay").param("plateNr",EXAMPLE_PLATE_NR)
                .sessionAttr("plate", plate))
                .andExpect(status().isOk());
        assertNotNull(plateRepository.findAll().get(0).getPlateNr());
    }

    @Test
    public void shouldDriverNotFindPlateAfterStopMeter() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plate.setStart(Instant.now());
        plate.setEnd(Instant.now());
        plateRepository.save(plate);

        mockMvc.perform(post("/savePlate").param("plateNr",EXAMPLE_PLATE_NR)
                .sessionAttr("plate", plate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty("plateNotFound.plateNotFound"))));
    }

    private void findOpenPlateWhenAClosedOneIsInRepo(String url, String property) throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plate.setStart(Instant.now());
        plate.setEnd(Instant.now());
        plateRepository.save(plate);
        plate.setEnd(null);
        plateRepository.save(plate);

        mockMvc.perform(post(url).param("plateNr", EXAMPLE_PLATE_NR)
                .sessionAttr("plate", plate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString(env.getProperty(property))));
    }

    @Test
    public void shouldDriverFindNewPlateAfterMeterStopThatIsRunning() throws Exception {
        findOpenPlateWhenAClosedOneIsInRepo("/savePlate", "plateFound.plateFound");
    }

    @Test
    public void shouldOperatorFindNewPlateAfterMeterStopThatIsRunning() throws Exception {
        findOpenPlateWhenAClosedOneIsInRepo("/operatorPlateSearch","operatorPlateFound.plateFound");
    }
}