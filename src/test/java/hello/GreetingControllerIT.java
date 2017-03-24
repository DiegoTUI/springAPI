package hello;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import java.net.URL;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class GreetingControllerIT {

    @LocalServerPort
    private int port;

    private URL base;

    @Autowired
    private TestRestTemplate template;

    @Before
    public void setUp() throws Exception {
        this.base = new URL("http://localhost:" + port + "/greeting?name=kkfu");
    }

    @Test
    public void getGreeting() throws Exception {
        final String EXPECTED_GREETING_TYPE = "regular";
        final String EXPECTED_GREETING_CONTENT = "Hello, kkfu!";
        ResponseEntity<String> response = template.getForEntity(base.toString(),
                String.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode idJson = responseJson.path("id");
        JsonNode typeJson = responseJson.path("type");
        JsonNode contentJson = responseJson.path("content");

        assertThat( idJson.isMissingNode() , is(false) );

        assertThat( typeJson.isMissingNode() , is(false) );
        assertThat( typeJson.asText() , equalTo(EXPECTED_GREETING_TYPE));

        assertThat( contentJson.isMissingNode() , is(false) );
        assertThat( contentJson.asText() , equalTo(EXPECTED_GREETING_CONTENT));
    }
}