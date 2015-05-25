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

    private static final int DELIMITER_ANNOTATION_LENGTH = 4;
    private static final String DEFAULT_DELIMITER = ";";

    @Test
    public void add_ForEmptyInput_returns_0() {
        assertThat(addIntegers(""), is(0));
    }

    @Test
    public void add_forSingleNumber_returnsSum() {
        assertThat(addIntegers("1"), is(1));
        assertThat(addIntegers("2"), is(2));
    }

    @Test
    public void addNumbers_withGiven2Numbers_returnsSum() {
        assertThat(addIntegers("1,2"), is(3));
        assertThat(addIntegers("2,3"), is(5));
    }

    @Test
    public void addNumber_forManyNumbers_returnSum() {
        assertThat(addIntegers("1,2,3,4,5,6,7,8,9,10"), is(55));
    }

    @Test
    public void addingNumber_includingNegariveNumbers_returnSum() {
        assertThat(addIntegers("-1"), is(-1));
    }

    @Test
    public void addingNumbers_includingSeveralNegativeNumbers_returnSum() {
        assertThat(addIntegers("1,-1,2,-1"), is(1));
    }

    @Test
    public void addNumber_withNewlineSeparator_returnsSum() {
        assertThat(addIntegers("1\n2,3"), is(6));
    }

    @Test
    public void addNumbers_acceptsDelimiterAnnotation() {
        assertThat(addIntegers("//;\n1;2"), is(3));
    }

    private int addIntegers(final String numbersAsString) {
        Optional<String> delimiter = getDelimiter(numbersAsString);
        String numberStringWithoutPrefix = getNumberStringWithoutPrefix(numbersAsString, delimiter);

        if (matchNumber(numberStringWithoutPrefix, delimiter.orElseGet(() -> DEFAULT_DELIMITER))) {
            List<Integer> numbers = extractNumbers(numberStringWithoutPrefix, delimiter);
            return addIntegers(numbers);
        }  else {
            return 0;
        }
    }

    private List<Integer> extractNumbers(String numberStringWithoutPrefix, final Optional<String> delimiter) {
        numberStringWithoutPrefix = normalizeDelimiter(numberStringWithoutPrefix, delimiter.get());
        return parseToIntegers(numberStringWithoutPrefix);
    }

    private String getNumberStringWithoutPrefix(final String numbersAsString, final Optional<String> delimiter) {
        String numberStringWithoutPrefix;
        if (delimiter.isPresent()) {
            numberStringWithoutPrefix = removeDelimiterAnnotation(numbersAsString);
        } else {
            numberStringWithoutPrefix = numbersAsString;
        }
        return numberStringWithoutPrefix;
    }

    private String removeDelimiterAnnotation(final String numbersAsString) {
        return numbersAsString.substring(DELIMITER_ANNOTATION_LENGTH);
    }

    private String normalizeDelimiter(final String numbersAsString, final String delimiter) {
        return numbersAsString
                .replace(",", DEFAULT_DELIMITER)
                .replace("\n", DEFAULT_DELIMITER)
                .replace(delimiter, DEFAULT_DELIMITER)
                ;
    }

    private Optional<String> getDelimiter(final String numbersAsString) {
        final Pattern delimiterDefinitionPattern = Pattern.compile("//(.)\\n.*");
        final Matcher matcher = delimiterDefinitionPattern.matcher(numbersAsString);
        String delimiter = null;
        if (matcher.matches()) {
            delimiter = matcher.group(1);
        }
        return Optional.ofNullable(delimiter);
    }

    private boolean matchNumber(final String numbersAsString, final String delimiter) {
        String delimiters = delimiter + DEFAULT_DELIMITER + ",\\n";
        return numbersAsString.matches("\\-?\\d+([" + delimiters + "]\\-?\\d+)*");
    }

    private List<Integer> parseToIntegers(final String numbersAsString) {
        final String[] singleNumbers = extractSingleNumber(numbersAsString);
        return getAsIntegers(singleNumbers);
    }

    private String[] extractSingleNumber(final String numbersAsString) {
        return numbersAsString.split(DEFAULT_DELIMITER);
    }

    private Integer addIntegers(final List<Integer> numbers) {
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
