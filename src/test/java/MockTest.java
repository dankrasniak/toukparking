import com.sun.javaws.exceptions.InvalidArgumentException;
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
import parking.entities.DateContainer;
import parking.entities.Plate;
import parking.entities.enums.Region;
import parking.repositories.PlateRepository;
import parking.services.PricingService;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;

import static junit.framework.TestCase.*;
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

    @Autowired
    private PricingService pricingService;

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
    public void shouldContainPlateInModel() throws Exception {
        mockMvc.perform(post("/savePlate")
                .sessionAttr("plate", new Plate()))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("plate"));
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
        plate.setStart(Instant.now());

        plateRepository.save(plate);

        mockMvc.perform(post("/savePlate")
                .param("plateNr", EXAMPLE_PLATE_NR)
                .param("region", Region.PLN.toString())
                .sessionAttr("plate", plate))
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

        mockMvc.perform(post("/stopAndPay").param("plateNr", EXAMPLE_PLATE_NR)
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

        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
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
                .param("region", Region.PLN.toString())
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
        findOpenPlateWhenAClosedOneIsInRepo("/operatorPlateSearch", "operatorPlateFound.plateFound");
    }

    private void priceCheck(Instant start, Instant end, boolean isVip) throws Exception {
        if (Duration.between(start, end).isNegative())
            throw new InvalidArgumentException(new String[]{"Negative Time!"});
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = new Plate();
        plate.setPlateNr(EXAMPLE_PLATE_NR);
        plate.setStart(start);
        plate.setRegion(Region.PLN);
        plate.setVip(isVip);
        plateRepository.save(plate);

        plate.setEnd(end);
        pricingService.updatePrice(plate);

        mockMvc.perform(post("/savePlate").param("plateNr", EXAMPLE_PLATE_NR)
                .param("region", Region.PLN.toString())
                .sessionAttr("plate", plate))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("value=\"" + plate.getPaid().toString() + "\"")));
    }

    @Test
    public void shouldHavePlatePricedFor2HoursNonVIP() throws Exception {
        priceCheck(Instant.now().minus(Duration.ofMinutes(121)), Instant.now(), false);
    }

    @Test
    public void shouldHavePlatePricedFor2HoursVIP() throws Exception {
        priceCheck(Instant.now().minus(Duration.ofMinutes(121)), Instant.now(), true);
    }

    @Test
    public void shouldHavePlatePricedFor24HoursNonVIP() throws Exception {
        priceCheck(Instant.now().minus(Duration.ofMinutes(1441)), Instant.now(), false);
    }

    @Test
    public void shouldHavePlatePricedFor24HoursVIP() throws Exception {
        priceCheck(Instant.now().minus(Duration.ofMinutes(1441)), Instant.now(), true);
    }

    private Plate getPlate(String plateNr, Instant start, Instant end, boolean isVip, BigDecimal paid) {
        Plate plate = new Plate();
        plate.setPlateNr(plateNr);
        plate.setStart(start);
        plate.setEnd(end);
        plate.setRegion(Region.PLN);
        plate.setVip(isVip);
        plate.setPaid(paid);
        return plate;
    }

    @Test
    public void shouldServiceReturnIncomeFor2PlatesHourEach() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);

        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        assertEquals(2, pricingService.getIncomeForDay(dateContainer), 0.0);
    }

    @Test
    public void shouldOwnerSeeIncomeFor2PlatesHourEach() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);

        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>2.0</span>")));
    }

    @Test
    public void shouldOwnerSeeIncomeFor2DifferentPlatesHourEach() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        final String EXAMPLE_PLATE_NR2 = "12345677";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR2, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);

        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>2.0</span>")));
    }

    @Test
    public void shouldOwnerSeeIncomeFor1PlateWhenSecondIsFromYesterday() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        final String EXAMPLE_PLATE_NR2 = "12345677";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now(), Instant.now().plus(Duration.ofMinutes(1)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR2, Instant.now().minus(Duration.ofHours(25)),
                Instant.now().plus(Duration.ofMinutes(1).minus(Duration.ofHours(25))), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);

        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>1.0</span>")));
    }

    @Test
    public void shouldOwnerSeeNoIncome() throws Exception {
        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>0.0</span>")));
    }

    @Test
    public void shouldOwnerSeeNoIncomeAsShouldWhenPlatesInRepo() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        final String EXAMPLE_PLATE_NR2 = "12345677";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now().minus(Duration.ofDays(25)), Instant.now().minus(Duration.ofDays(12)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR2, Instant.now().minus(Duration.ofHours(25)),
                Instant.now().plus(Duration.ofMinutes(1).minus(Duration.ofHours(25))), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);
        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>0.0</span>")));
    }

    @Test
    public void shouldOwnerSeeNoIncome1PlateYesterday1PlateTomorrow() throws Exception {
        final String EXAMPLE_PLATE_NR = "12345678";
        final String EXAMPLE_PLATE_NR2 = "12345677";
        Plate plate = getPlate(EXAMPLE_PLATE_NR, Instant.now().minus(Duration.ofHours(26)), Instant.now().minus(Duration.ofHours(25)), false, new BigDecimal(1));
        Plate plate2 = getPlate(EXAMPLE_PLATE_NR2, Instant.now().plus(Duration.ofHours(25)),
                Instant.now().plus(Duration.ofHours(25).plus(Duration.ofMinutes(1))), false, new BigDecimal(1));

        plateRepository.save(plate);
        plateRepository.save(plate2);
        DateContainer dateContainer = new DateContainer();
        dateContainer.setDateTime(LocalDate.now());
        mockMvc.perform(post("/getIncome")
                .param("dateTime", dateContainer.getDateTime().toString())
                .param("income", Double.toString(pricingService.getIncomeForDay(dateContainer)))
                .sessionAttr("dateContainer", dateContainer))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<span>0.0</span>")));
    }
}