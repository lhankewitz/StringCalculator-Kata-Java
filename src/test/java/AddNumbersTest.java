import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Class to ...
 *
 * @author lumiha
 * @since 23/05/15.
 */
public class AddNumbersTest {

    @Test
    public void add_ForEmptyInput_returns_0() {
        assertThat(add(""), is(0));
    }

    @Test
    public void add_forSingleNumber_returnsSum() {
        assertThat(add("1"), is(1));
        assertThat(add("2"), is(2));
    }

    @Test
    public void addNumbers_withGiven2Numbers_returnsSum() {
        assertThat(add("1,2"), is(3));
        assertThat(add("2,3"), is(5));
    }

    private int add(final String numbersAsString) {
        if(numbersAsString.matches("\\d+")){
            return Integer.valueOf(numbersAsString);
        } else if(numbersAsString.matches("\\d+,\\d+")){
            final String[] singleNumbers = numbersAsString.split(",");
            final List<Integer> numbers = getAsIntegers(singleNumbers);
            Integer sum = 0;
            for (Integer number : numbers) {
                sum += number;
            }
            return sum;
        } else{
            return 0;
        }
    }

    private List<Integer> getAsIntegers(final String[] singleNumbers) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (String singleNumber : singleNumbers) {
            numbers.add(Integer.valueOf(singleNumber));
        }

        return numbers;
    }
}
