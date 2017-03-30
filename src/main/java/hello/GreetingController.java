package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class GreetingController {
    @Autowired
    private GreetingRepository repository;

    @RequestMapping(value="/greeting", method=RequestMethod.GET)
    public List<Greeting> greetingGET(@RequestParam(value="type", defaultValue="regular") String type) {
        return repository.findByType(type);
    }

    @RequestMapping(value="/greeting", method=RequestMethod.POST)
    public Greeting greetingPOST(@RequestBody Greeting greeting) {
        greeting = greeting.type == null ? new Greeting(greeting.content) : greeting;
        repository.save(greeting);
        return greeting;
    }

    @RequestMapping(value="/greeting/{greetingId}", method=RequestMethod.PUT)
    public Greeting greetingPUT(@PathVariable String greetingId, @RequestBody Greeting greeting) {
        Greeting dbGreeting = repository.findOne(greetingId);
        if (dbGreeting == null) {
            throw new InvalidRequestException("Greeting not found by id");
        }
        dbGreeting.type = greeting.type;
        dbGreeting.content = greeting.content;

        repository.save(dbGreeting);
        return dbGreeting;
    }
}
