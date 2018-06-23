import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import parking.Application;
import parking.repositories.PlateRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
public class ApplicationTest {

    @Autowired
    private PlateRepository plateRepository;

    @Test
    public void contextLoads() throws Exception {
    }

    @Test
    public void shouldExistPlateRepo() {
        assert (plateRepository != null);
    }

}