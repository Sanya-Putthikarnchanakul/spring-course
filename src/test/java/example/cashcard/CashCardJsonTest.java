package example.cashcard;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.boot.test.json.ObjectContent;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@JsonTest
public class CashCardJsonTest {

    @Test // mark this is a test method
    void myFirstTest() {
        // try a failed test first (TDD approach)
        // assertThat(1).isEqualTo(42);

        // passed test
        int result = 42;
        assertThat(result).isEqualTo(42);
    }

    @Autowired // dependency injection pattern in spring boot
    private JacksonTester<CashCard> json; // always making injected as private to limit the scope

    @Test
    void cashCardSerializationTest() throws IOException {
        var cashCard = new CashCard(99L, 123.45); // can be use var if clearly type inference

        JsonContent<CashCard> jsonContent = json.write(cashCard);

        // assert the serialized json is equal to expected json file
        // should add file on src/test/resources/example/cashcard/expected.json
        assertThat(jsonContent).isStrictlyEqualToJson("expected.json");

        // check serialized has id key and value should be 99
        var idKey = "@.id";
        assertThat(jsonContent).hasJsonPathNumberValue(idKey);
        assertThat(jsonContent).extractingJsonPathNumberValue(idKey).isEqualTo(99);

        // check serialized has id key and value should be 123.45
        var amountKey = "@.amount";
        assertThat(jsonContent).hasJsonPathNumberValue(amountKey);
        assertThat(jsonContent).extractingJsonPathNumberValue(amountKey).isEqualTo(123.45);
    }

    @Test
    void cashCardDeserializationTest() throws IOException {
        var expected = """
        {
            "id": 99,
            "amount": 123.45
        }
        """;

        var cashCard = new CashCard(99L, 123.45);
        ObjectContent<CashCard> expectedObj = json.parse(expected);
        CashCard expectedCashcard = json.parseObject(expected);

        assertThat(expectedObj).isEqualTo(cashCard);
        assertThat(expectedCashcard.id()).isEqualTo(99);
        assertThat(expectedCashcard.amount()).isEqualTo(123.45);
    }

}
