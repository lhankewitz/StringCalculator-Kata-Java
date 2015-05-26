import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StringCalculator {
    private static final int SINGLE_NUMBER_LIMIT = 1000;

    int addIntegers(final String numbersAsString) {
        final DelimiterManager delimiterManager = new DelimiterManager(numbersAsString);
        String numberStringWithoutPrefix = delimiterManager.getNumberStringWithoutPrefix();

        if (matchNumber(numberStringWithoutPrefix, delimiterManager)) {
            List<Integer> numbers = extractNumbers(numberStringWithoutPrefix, delimiterManager);

            validateNotNegativeNumbers(numbers);

            return addIntegers(numbers);
        } else {
            return 0; // empty string or not parsable number
        }
    }

    private void validateNotNegativeNumbers(final List<Integer> numbers) {
        int[] negativeNumbers = numbers.stream().filter(n -> n < 0).mapToInt(n -> n).toArray();

        boolean hasNegativeNumbers = negativeNumbers.length > 0;

        if (hasNegativeNumbers) {
            String message = "negatives not allowed";
            message += message + Arrays.toString(negativeNumbers);
            throw new RuntimeException(message);
        }
    }

    private List<Integer> extractNumbers(String numberStringWithoutPrefix, final DelimiterManager delimiterManager) {
        numberStringWithoutPrefix = delimiterManager.normalizeDelimiter(numberStringWithoutPrefix);
        return parseToIntegers(numberStringWithoutPrefix);
    }


    private boolean matchNumber(final String numbersAsString, final DelimiterManager delimiterManager) {
        String delimiterRegExp = delimiterManager.getDelimiterRegExp();
        String regex = "\\-?\\d+(" + delimiterRegExp + "\\-?\\d+)*";

        return numbersAsString.matches(regex);
    }

    private List<Integer> parseToIntegers(final String numbersAsString) {
        final String[] singleNumbers = extractSingleNumber(numbersAsString);
        return getAsIntegers(singleNumbers);
    }

    private String[] extractSingleNumber(final String numbersAsString) {
        return numbersAsString.split(DelimiterManager.DEFAULT_DELIMITER);
    }

    private Integer addIntegers(final List<Integer> numbers) {
        return numbers.stream().filter(n -> n <= SINGLE_NUMBER_LIMIT).reduce(0, Integer::sum);
    }

    private List<Integer> getAsIntegers(final String[] singleNumbers) {
        List<Integer> numbers = new ArrayList<>();
        for (String singleNumber : singleNumbers) {
            numbers.add(Integer.valueOf(singleNumber));
        }

        return numbers;
    }
}