/*
 * Copyright (c) year Lutz Hankewitz.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DelimiterManager {
    private static final String DEFAULT_DELIMITER = ";";
    private static final Pattern DELIMITER_ANNOTATION_SEPARATOR_PATTERN = Pattern.compile("\\]\\[");
    private static final Pattern DELIMITER_ANNOTATION_PATTERN = Pattern.compile("//\\[(.*)\\]\\n.*");

    private final Optional<String[]> optionalDelimiter;
    private final String numbersDescription;
    private final String[] delimiters;

    public DelimiterManager(final String arithmeticDescription) {
        optionalDelimiter = getDelimitersFromAnnotation(arithmeticDescription);
        delimiters = optionalDelimiter.orElseGet(() -> new String[]{DEFAULT_DELIMITER});

        numbersDescription = extractNumberDescription(arithmeticDescription, optionalDelimiter);
    }

    String[] splitIntoNumbers(final String numbersDescription) {
        return normalizeDelimiter(numbersDescription, delimiters).split(DEFAULT_DELIMITER);
    }


    public String extractNumberDescription(final String arithmeticDescription, final Optional<String[]> delimiter) {
        String numberStringWithoutAnnotation = arithmeticDescription;

        if (delimiter.isPresent()) {
            numberStringWithoutAnnotation = removeDelimiterAnnotation(arithmeticDescription, delimiter.get());
        }

        return numberStringWithoutAnnotation;
    }

    private Optional<String[]> getDelimitersFromAnnotation(final String numbersAsString) {
        final Matcher delimiterAnnotationMatcher = DELIMITER_ANNOTATION_PATTERN.matcher(numbersAsString);
        String[] delimiters = null;
        if (delimiterAnnotationMatcher.matches()) {
            String delimiterGroup = delimiterAnnotationMatcher.group(1);
            delimiters = DELIMITER_ANNOTATION_SEPARATOR_PATTERN.split(delimiterGroup);
        }
        return Optional.ofNullable(delimiters);
    }

    private String removeDelimiterAnnotation(final String numbersAsString, final String[] delimiters) {
        return numbersAsString.substring(calculateDelimiterAnnotationLength(delimiters));
    }

    private int calculateDelimiterAnnotationLength(final String[] delimiters) {
        int length = "//\n".length();
        for (String delimiter : delimiters) {
            length += delimiter.length() + "[]".length();
        }
        return length;
    }

    static String normalizeDelimiter(final String numbersAsString, final String[] delimiters) {
        String replace = numbersAsString
                .replace(",", DEFAULT_DELIMITER)
                .replace("\n", DEFAULT_DELIMITER);

        for (String aDelimiter : delimiters) {
            replace = replace.replace(aDelimiter, DEFAULT_DELIMITER);
        }

        return replace;
    }

    String getDelimiterRegExp() {
        StringBuilder delimitersRegExpBuilder = new StringBuilder();
        delimitersRegExpBuilder.append("(");

        appendCustomDelimiterRegExp(delimitersRegExpBuilder, delimiters);

        delimitersRegExpBuilder.append("|[," + DEFAULT_DELIMITER + "\n]");

        delimitersRegExpBuilder.append(')');

        return delimitersRegExpBuilder.toString();
    }

    private void appendCustomDelimiterRegExp(final StringBuilder delimitersRegExpBuilder, final String[] delimiters) {
        for (int i = 0; i < delimiters.length; i++) {
            if (i > 0) delimitersRegExpBuilder.append('|');
            delimitersRegExpBuilder.append(Pattern.quote(delimiters[i]));
        }
    }


    public String extractNumberDescription() {
        return numbersDescription;
    }
}