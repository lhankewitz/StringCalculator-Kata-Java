import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Class to test string calculator
 *
 * @author lumiha
 * @since 23/05/15.
 */
public class AddNumbersTest {

    private final StringCalculator stringCalculator = new StringCalculator();

    @Test
    public void add_ForEmptyInput_returns_0() {
        assertThat(stringCalculator.addIntegers(""), is(0));
    }

    @Test
    public void add_forSingleNumber_returnsSum() {
        assertThat(stringCalculator.addIntegers("1"), is(1));
        assertThat(stringCalculator.addIntegers("2"), is(2));
    }

    @Test
    public void addNumbers_withGiven2Numbers_returnsSum() {
        assertThat(stringCalculator.addIntegers("1,2"), is(3));
        assertThat(stringCalculator.addIntegers("2,3"), is(5));
    }

    @Test
    public void addNumber_forManyNumbers_returnSum() {
        assertThat(stringCalculator.addIntegers("1,2,3,4,5,6,7,8,9,10"), is(55));
    }

    @Test
    public void addingNumber_includingNegariveNumbers_returnSum() {
        assertThat(stringCalculator.addIntegers("-1"), is(-1));
    }

    @Test
    public void addingNumbers_includingSeveralNegativeNumbers_returnSum() {
        assertThat(stringCalculator.addIntegers("1,-1,2,-1"), is(1));
    }

    @Test
    public void addNumber_withNewlineSeparator_returnsSum() {
        assertThat(stringCalculator.addIntegers("1\n2,3"), is(6));
    }

    @Test
    public void addNumbers_acceptsDelimiterAnnotation() {
        assertThat(stringCalculator.addIntegers("//;\n1;2"), is(3));
    }

}
