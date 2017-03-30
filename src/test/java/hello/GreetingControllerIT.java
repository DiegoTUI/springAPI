package hello;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.embedded.LocalServerPort;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.test.context.transaction.TransactionConfiguration;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations="classpath:test.properties")
public class GreetingControllerIT {

    @LocalServerPort
    private int port;

    private String validId;

    private String baseURL;

    @Autowired
    private TestRestTemplate template;

    @Autowired
    public GreetingRepository repository;

    private void populate() {
        Greeting greeting = new Greeting("randomContent", "randomType");
        repository.save(greeting);
        validId = greeting.getId();
    }

    @Before
    public void setUp() throws Exception {
        this.baseURL = "http://localhost:" + port;
    }

    @After
    public void tearDown() {
        this.repository.deleteAll();
    }

    @Test
    public void getGreeting() throws Exception {
        final String EXPECTED_GREETING_TYPE = "regular";
        final String EXPECTED_GREETING_CONTENT = "Hello, kkfu!";
        ResponseEntity<String> response = template.getForEntity(baseURL + "/greeting",
                String.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        assertThat(responseJson.isArray(), is(true));
    }

    @Test
    public void postGreetingWithNoType() throws Exception {
        final String EXPECTED_GREETING_TYPE = "regular";
        final String EXPECTED_GREETING_CONTENT = "kkfu";
        ResponseEntity<String> response = template.postForEntity(baseURL + "/greeting", new Greeting("kkfu"), String.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode typeJson = responseJson.path("type");
        JsonNode contentJson = responseJson.path("content");

        assertThat( typeJson.isMissingNode() , is(false) );
        assertThat( typeJson.asText() , equalTo(EXPECTED_GREETING_TYPE));

        assertThat( contentJson.isMissingNode() , is(false) );
        assertThat( contentJson.asText() , equalTo(EXPECTED_GREETING_CONTENT));
    }

    @Test
    public void postGreetingWithType() throws Exception {
        final String EXPECTED_GREETING_TYPE = "special";
        final String EXPECTED_GREETING_CONTENT = "kkfu";
        ResponseEntity<String> response = template.postForEntity(baseURL + "/greeting", new Greeting("kkfu", EXPECTED_GREETING_TYPE), String.class);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode typeJson = responseJson.path("type");
        JsonNode contentJson = responseJson.path("content");

        assertThat( typeJson.isMissingNode() , is(false) );
        assertThat( typeJson.asText() , equalTo(EXPECTED_GREETING_TYPE));

        assertThat( contentJson.isMissingNode() , is(false) );
        assertThat( contentJson.asText() , equalTo(EXPECTED_GREETING_CONTENT));
    }

    @Test
    public void putGreeting() throws Exception {
        populate();

        final String EXPECTED_GREETING_TYPE = "special";
        final String EXPECTED_GREETING_CONTENT = "kkfu";

        String requestBody = "{\"content\":\"kkfu\", \"type\":\"special\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<String> response = template.exchange(baseURL + "/greeting/{greetingId}", HttpMethod.PUT, httpEntity, String.class, validId);

        assertThat( response.getStatusCode() , equalTo(HttpStatus.OK));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode typeJson = responseJson.path("type");
        JsonNode contentJson = responseJson.path("content");

        assertThat( typeJson.isMissingNode() , is(false) );
        assertThat( typeJson.asText() , equalTo(EXPECTED_GREETING_TYPE));

        assertThat( contentJson.isMissingNode() , is(false) );
        assertThat( contentJson.asText() , equalTo(EXPECTED_GREETING_CONTENT));
    }

    @Test
    public void putGreetingInvalidId() throws Exception {
        populate();

        final String EXPECTED_CODE = "InvalidRequest";
        final String EXPECTED_MESSAGE = "Greeting not found by id";

        String requestBody = "{\"content\":\"kkfu\", \"type\":\"special\"}";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> httpEntity = new HttpEntity<String>(requestBody, headers);

        ResponseEntity<String> response = template.exchange(baseURL + "/greeting/{greetingId}", HttpMethod.PUT, httpEntity, String.class, "invalidId");

        assertThat( response.getStatusCode() , equalTo(HttpStatus.NOT_FOUND));

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode responseJson = objectMapper.readTree(response.getBody());
        JsonNode codeJson = responseJson.path("code");
        JsonNode messageJson = responseJson.path("message");

        assertThat( codeJson.isMissingNode() , is(false) );
        assertThat( codeJson.asText() , equalTo(EXPECTED_CODE));

        assertThat( messageJson.isMissingNode() , is(false) );
        assertThat( messageJson.asText() , equalTo(EXPECTED_MESSAGE));
    }

}
