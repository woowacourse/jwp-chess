package study;

import static org.assertj.core.api.Assertions.assertThat;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

public class ObjectMapperTest {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void checkRequirements() throws JsonProcessingException {
        User user = new User(1, "jeong");

        String actual = objectMapper.writeValueAsString(user);
        String expected = "{\"id\":1,\"name\":\"jeong\"}";

        assertThat(actual).isEqualTo(expected);
    }

    @JsonAutoDetect(fieldVisibility = Visibility.ANY)
    private static class User {

        final int id;
        final String name;

        User(int id, String name) {
            this.id = id;
            this.name = name;
        }
    }
}
