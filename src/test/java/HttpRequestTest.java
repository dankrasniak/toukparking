import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import parking.Application;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT, classes = Application.class)
public class HttpRequestTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void shouldReturnIAmA() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/",
                String.class)).contains("I am a");
    }

    @Test
    public void shouldReturnDriverPage() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/driver",
                String.class)).contains("Hello, driver!");
    }

    @Test
    public void shouldReturnOperatorPage() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/operator",
                String.class)).contains("Hello, operator!");
    }

    @Test
    public void shouldReturnOwnerPage() {
        assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/owner",
                String.class)).contains("Hello, Mr. Owner!");
    }
}