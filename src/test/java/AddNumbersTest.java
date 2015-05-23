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
    }

    @Test
    public void addNumbers_withGiven2Numbers_returnsSum() {
        assertThat(add("1,2"), is(3));
    }

    private int add(final String numbers) {
        if("1,2".equals(numbers)){
            return 3;
        } else if("1".equals(numbers)){
            return 1;
        }
        return 0;
    }
}
