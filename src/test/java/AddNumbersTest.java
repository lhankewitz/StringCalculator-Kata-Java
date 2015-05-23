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

    @Test
    public void addNumber_forManyNumbers_returnSum() {
        assertThat(add("1,2,3,4,5,6,7,8,9,10"), is(55) );
    }

    private int add(final String numbersAsString) {
        if(numbersAsString.matches("\\d+")){
            return Integer.valueOf(numbersAsString);
        } else if(numbersAsString.matches("\\d+,\\d+")){
            return add(parseToIntegers(numbersAsString));
        }  else if(numbersAsString.matches("\\d+(,\\d+)+")){
            return add(parseToIntegers(numbersAsString));
        } else{
            return 0;
        }
    }

    private List<Integer> parseToIntegers(final String numbersAsString) {
        final String[] singleNumbers = extractSingleNumber(numbersAsString);
        return getAsIntegers(singleNumbers);
    }

    private String[] extractSingleNumber(final String numbersAsString) {
        return numbersAsString.split(",");
    }

    private Integer add(final List<Integer> numbers) {
        return numbers.stream().reduce(0, Integer::sum);
    }

    private List<Integer> getAsIntegers(final String[] singleNumbers) {
        List<Integer> numbers = new ArrayList<>();
        for (String singleNumber : singleNumbers) {
            numbers.add(Integer.valueOf(singleNumber));
        }

        return numbers;
    }
}
