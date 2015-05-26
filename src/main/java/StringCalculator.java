import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {
    private static final String DEFAULT_DELIMITER = ";";
    private static final int SINGLE_NUMBER_LIMIT = 1000;


    int addIntegers(final String numbersAsString) {
        Optional<String[]> delimiter = getDelimiter(numbersAsString);
        String numberStringWithoutPrefix = getNumberStringWithoutPrefix(numbersAsString, delimiter);

        String[] delimiters = delimiter.orElseGet(() -> new String[]{DEFAULT_DELIMITER});
        String delimiterString = delimiters[0];
        if (matchNumber(numberStringWithoutPrefix, delimiterString, delimiters)) {
            List<Integer> numbers = extractNumbers(numberStringWithoutPrefix, delimiters);

            validateNotNegativeNumbers(numbers);

            return addIntegers(numbers);
        } else {
            return 0;
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

    private List<Integer> extractNumbers(String numberStringWithoutPrefix, final String[] delimiters) {
        numberStringWithoutPrefix = normalizeDelimiter(numberStringWithoutPrefix, delimiters);
        return parseToIntegers(numberStringWithoutPrefix);
    }

    private String getNumberStringWithoutPrefix(final String numbersAsString, final Optional<String[]> delimiter) {
        String numberStringWithoutPrefix;
        if (delimiter.isPresent()) {
            numberStringWithoutPrefix = removeDelimiterAnnotation(numbersAsString, delimiter);
        } else {
            numberStringWithoutPrefix = numbersAsString;
        }
        return numberStringWithoutPrefix;
    }

    private String removeDelimiterAnnotation(final String numbersAsString, final Optional<String[]> delimiter) {
        int length = "//\n".length();
        for (String d : delimiter.get()) {
            length += d.length() + 2;
        }
        return numbersAsString.substring(length);
    }

    private String normalizeDelimiter(final String numbersAsString, final String[] delimiters) {
        String replace = numbersAsString
                .replace(",", DEFAULT_DELIMITER)
                .replace("\n", DEFAULT_DELIMITER);

        for (String aDelimiter : delimiters) {
            replace = replace.replace(aDelimiter, DEFAULT_DELIMITER);
        }

        return replace;
    }

    private Optional<String[]> getDelimiter(final String numbersAsString) {
        final Pattern delimiterDefinitionPattern = Pattern.compile("//\\[(.*)\\]\\n.*");
        final Pattern delimiterPattern = Pattern.compile("\\]\\[");
        final Matcher matcher = delimiterDefinitionPattern.matcher(numbersAsString);
        String[] delimiters = null;
        if (matcher.matches()) {
            String delimiterGroup = matcher.group(1);
            delimiters = delimiterPattern.split(delimiterGroup);
        }
        return Optional.ofNullable(delimiters);
    }

    private boolean matchNumber(final String numbersAsString, final String inputDelimiter, final String[] strings) {
        StringBuilder delimiters = new StringBuilder();
        for (int i = 0; i < strings.length; i++) {
            if (i > 0) delimiters.append('|');
            delimiters.append(escapeRegExpCharacter(strings[i]));
        }
        String regex = "\\-?\\d+((" + delimiters.toString() + "|[," + DEFAULT_DELIMITER + "\\n])\\-?\\d+)*";
        return numbersAsString.matches(regex);
    }

    private String escapeRegExpCharacter(final String delimiter) {
        return delimiter.replace("*", "\\*").replace("-", "\\-").replace(".", "\\.");
    }

    private List<Integer> parseToIntegers(final String numbersAsString) {
        final String[] singleNumbers = extractSingleNumber(numbersAsString);
        return getAsIntegers(singleNumbers);
    }

    private String[] extractSingleNumber(final String numbersAsString) {
        return numbersAsString.split(DEFAULT_DELIMITER);
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