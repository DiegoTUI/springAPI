package hello;

import org.springframework.data.annotation.Id;

public class Greeting {

    @Id
    private String id;

    public String type;
    public String content;

    Greeting() {
    }

    public Greeting(String content) {
        this(content,"regular");
    }

    public Greeting(String content, String type) {
        this.type = type == null ? "regular" : type;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, type = '%s', content='%s']",
                id, type, content);
    }
}
