package hello;

import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface GreetingRepository extends MongoRepository<Greeting, String> {

    public List<Greeting> findByContent(String content);
    public List<Greeting> findByType(String type);
}