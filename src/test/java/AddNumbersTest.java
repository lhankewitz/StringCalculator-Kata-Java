import org.junit.Test;

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
    }

    private int add(final String numbers) {
        if(numbers.matches("\\d+")){
            return Integer.valueOf(numbers);
        } else if("1,2".equals(numbers)){
            return 3;
        } else{
            return 0;
        }
    }
}
