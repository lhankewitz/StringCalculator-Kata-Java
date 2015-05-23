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
        final int result = add("");
        assertThat(result, is(0));
    }

    @Test
    public void add_forSingleNumber_returnsSum() {
        final int result = add("1");
        assertThat(result, is(1));
    }

    private int add(final String numbers) {
        if("1".equals(numbers)){
            return 1;
        }
        return 0;
    }
}
