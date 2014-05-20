/**
 * 
 */
package sm.jnew;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author smazumder
 *
 */
public class MainTest {

	@Test
	public void testTriple() {
		assertThat(Main.triple("AB"), equalTo("ABABAB"));
	}

}
