import org.junit.jupiter.api.Test;

public class RegexTest {

    @Test
    public void regex_test() {
        String input = "123하이ab_123123";
        System.out.println(("매칭: " + input.matches("^[a-zA-Z0-9_가-힣]{2,10}$")));
    }
}
