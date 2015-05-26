import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DelimiterManager {
    static final String DEFAULT_DELIMITER = ";";
    private final Optional<String[]> delimiter;
    private final String numberStringWithoutPrefix;
    private final String[] delimiters;

    public DelimiterManager(final String numbersAsString) {
        delimiter = getDelimiter(numbersAsString);
        numberStringWithoutPrefix = getNumberStringWithoutPrefix(numbersAsString, delimiter);
        delimiters = delimiter.orElseGet(() ->  new String[]{DEFAULT_DELIMITER});
    }


    public String getNumberStringWithoutPrefix(final String numbersAsString, final Optional<String[]> delimiter) {
        String numberStringWithoutPrefix;
        if (delimiter.isPresent()) {
            numberStringWithoutPrefix = removeDelimiterAnnotation(numbersAsString, delimiter);
        } else {
            numberStringWithoutPrefix = numbersAsString;
        }
        return numberStringWithoutPrefix;
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

    private String removeDelimiterAnnotation(final String numbersAsString, final Optional<String[]> delimiter) {
        int length = "//\n".length();
        for (String d : delimiter.get()) {
            length += d.length() + 2;
        }
        return numbersAsString.substring(length);
    }

    String normalizeDelimiter(final String numbersAsString) {
        String replace = numbersAsString
                .replace(",", DEFAULT_DELIMITER)
                .replace("\n", DEFAULT_DELIMITER);

        for (String aDelimiter : delimiters) {
            replace = replace.replace(aDelimiter, DEFAULT_DELIMITER);
        }

        return replace;
    }

    String getDelimiterRegExp() {
        StringBuilder delimitersRegExpBuilder = new StringBuilder("(");
        for (int i = 0; i < delimiters.length; i++) {
            if (i > 0) delimitersRegExpBuilder.append('|');
            delimitersRegExpBuilder.append(escapeRegExpCharacter(delimiters[i]));
        }
        delimitersRegExpBuilder.append("|[," + DEFAULT_DELIMITER + "\n]");
        delimitersRegExpBuilder.append(')');

        return delimitersRegExpBuilder.toString();
    }

    private String escapeRegExpCharacter(final String delimiter) {
        return delimiter.replace("*", "\\*").replace("-", "\\-").replace(".", "\\.");
    }


    public String getNumberStringWithoutPrefix() {
        return numberStringWithoutPrefix;
    }
}