package guru.qa;


import com.fasterxml.jackson.databind.ObjectMapper;
import guru.qa.model.User;
import guru.qa.model.UserInner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;


import java.io.InputStream;
import java.util.Objects;



public class FileParsingTestforJson {
    private final ClassLoader classLoader = FileParsingTestforJson.class.getClassLoader();

    @Test
    void jsonFileParsingTest() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        try (InputStream is = Objects.requireNonNull(classLoader.getResourceAsStream("User.json"))) {
            User user= mapper.readValue(is, User.class);

            Assertions.assertEquals(1, user.getId());
            Assertions.assertEquals("ivan@example.com", user.getEmail());

            UserInner address = user.getAddress();
            Assertions.assertEquals("123 Main St", address.getStreet());
            Assertions.assertEquals("New York", address.getCity());

        }

    }
}


