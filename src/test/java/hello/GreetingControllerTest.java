package hello;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@AutoConfigureMockMvc
public class GreetingControllerTest {

    @Autowired
    private MockMvc mvc;

    @Test
    public void getGreeting() throws Exception {
        final String EXPECTED_GREETING_TYPE = "regular";
        final String EXPECTED_GREETING_CONTENT = "Hello, kkfu!";
        mvc.perform(MockMvcRequestBuilders.get("/greeting?name=kkfu").accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").isString())
                .andExpect(jsonPath("$.type").value(EXPECTED_GREETING_TYPE))
                .andExpect(jsonPath("$.content").value(EXPECTED_GREETING_CONTENT));
    }
}
