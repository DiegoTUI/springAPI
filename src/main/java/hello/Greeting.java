package hello;

import org.springframework.data.annotation.Id;

public class Greeting {

    @Id
    private String id;

    private String type;
    private String content;

    Greeting() {}

    public Greeting(String content) {
        this.type = "regular";
        this.content = content;
    }

    public Greeting(String type, String content) {
        this.type = type;
        this.content = content;
    }

    public String getId() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getContent() {
        return content;
    }

    @Override
    public String toString() {
        return String.format(
                "Customer[id=%s, content='%s']",
                id, content);
    }
}
