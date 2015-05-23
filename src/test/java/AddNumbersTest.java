import org.junit.Ignore;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Test
    public void addingNumber_includingNegariveNumbers_returnSum() {
        assertThat(add("-1"), is(-1));
    }

    @Test
    public void addingNumbers_includingSeveralNegativeNumbers_returnSum() {
        assertThat(add("1,-1,2,-1"), is(1));
    }

    @Test
    public void addNumber_withNewlineSeparator_returnsSum() {
        assertThat(add("1\n2,3"), is(6));
    }

    @Test
    public void addNumbers_acceptsDelimiterAnnotation() {
        assertThat(add("//;\n1;2"), is(3));
    }

    private int add(final String numbersAsString) {
        Optional<String> delimiter = getDelimiter(numbersAsString);
        if(delimiter.isPresent()){
            String numberStringWithoutPrefix = numbersAsString.substring(4);
            numberStringWithoutPrefix = numberStringWithoutPrefix.replace(delimiter.get(), ",");
            return add(parseToIntegers(numberStringWithoutPrefix));
        } else if(matchNumber(numbersAsString, ";")){
            final String numbersWithUnifiedDelimiter = numbersAsString.replace("\n", ",");
            return add(parseToIntegers(numbersWithUnifiedDelimiter));
        } else {
            return 0;
        }
    }

    private Optional<String> getDelimiter(final String numbersAsString) {
        final Pattern delimiterPattern = Pattern.compile("//(.)\\n.*");
        final Matcher matcher = delimiterPattern.matcher(numbersAsString);
        String delimiter = null;
        if(matcher.matches()){
            delimiter = matcher.group(1);
        }
        return Optional.ofNullable(delimiter);
    }

    private boolean matchNumber(final String numbersAsString, final String delimiter) {
        return numbersAsString.matches("\\-?\\d+([" + delimiter + ";,\\n]\\-?\\d+)*");
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
