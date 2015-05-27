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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Class to calculate a sum from an arithmetic description.
 * Solving the String calculator kata from <a href="http://osherove.com/tdd-kata-1">String Calculator Kata</a>
 *
 * @author Lutz Hankewitz
 * */
public class StringCalculator {
    private static final int SINGLE_NUMBER_LIMIT = 1000;

    /**
     * Calculate the sum of numbers. Defined in the format "//[<delimiter1>][delimiter2]...[delimitern]\n<integer1><delimiter 1 or 2 or ..n><integer2>"
     * where delimiter definition and more than one integer are optional.
     *
     * Sum of the empty string is 0.
     * Any numbers above SINGLE_NUMBER_LIMIT are ignored.
     * Default delimiter are ";", "," or "\n"
     *
     * @param arithmeticDescription of the terms to calculate
     * @return sum of the numbers defined in the arithmeticDescription
     * */
    public int addIntegers(final String arithmeticDescription) {
        final DelimiterManager delimiterManager = new DelimiterManager(arithmeticDescription);
        String numbersDescription = delimiterManager.extractNumberDescription();

        if (matchNumbersDefinition(numbersDescription, delimiterManager)) {
            List<Integer> numbers = extractNumbers(numbersDescription, delimiterManager);

            validateNotNegativeNumbers(numbers);

            return addIntegers(numbers);
        } else {
            return 0; // empty string or not parsable number
        }
    }

    private boolean matchNumbersDefinition(final String numbersDescription, final DelimiterManager delimiterManager) {
        String delimiterRegExp = delimiterManager.getDelimiterRegExp();
        String regex = "\\-?\\d+(" + delimiterRegExp + "\\-?\\d+)*";

        return numbersDescription.matches(regex);
    }

    private List<Integer> extractNumbers(String numbersDescription, final DelimiterManager delimiterManager) {
        String[] singleNumbers = delimiterManager.splitIntoNumbers(numbersDescription);
        return parseToIntegers(singleNumbers);
    }


    private List<Integer> parseToIntegers(final String[] singleNumbers) {
        List<Integer> numbers = new ArrayList<>();
        for (String singleNumber : singleNumbers) {
            numbers.add(Integer.valueOf(singleNumber));
        }

        return numbers;
    }

    private void validateNotNegativeNumbers(final List<Integer> numbers) {
        int[] negativeNumbers = numbers.stream().filter(n -> n < 0).mapToInt(n -> n).toArray();

        if (negativeNumbers.length > 0) {
            String message = "negatives not allowed";
            message += message + Arrays.toString(negativeNumbers);
            throw new RuntimeException(message);
        }
    }

    private Integer addIntegers(final List<Integer> numbers) {
        return numbers.stream().filter(n -> n <= SINGLE_NUMBER_LIMIT).reduce(0, Integer::sum);
    }
}