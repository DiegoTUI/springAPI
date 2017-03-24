package hello;

import java.util.concurrent.atomic.AtomicLong;

import org.springframework.web.bind.annotation.*;

@RestController
public class GreetingController {

    private static final String template = "Hello, %s!";
    private final AtomicLong counter = new AtomicLong();

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
    public Greeting greetingGET(@RequestParam(value="name", defaultValue="World") String name) {
        return new Greeting(Long.toString(counter.incrementAndGet()),
                String.format(template, name));
    }

    @RequestMapping(value="/greeting", method=RequestMethod.POST)
    public Greeting greetingPOST(@RequestBody Greeting greeting) {
        return greeting;
    }

    @RequestMapping(value="/greeting/{greetingId}", method=RequestMethod.PUT)
    public Greeting greetingPUT(@PathVariable String greetingId,  @RequestBody Greeting greeting) {
        return new Greeting(greetingId, greeting.getContent());
    }
}
