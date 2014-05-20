/**
 * 
 */
package sm.jnew;

import java.util.List;
import java.util.Map;
import java.util.Random;

import static java.util.stream.Collectors.*;
import static java.util.stream.IntStream.range;

/**
 * @author smazumder
 *
 */
public class Main8 {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// generate a list of 100 random names
		List<String> randomnames = range(0, 100).mapToObj(
				i -> randomString(new Random(), 'A', 'Z', 10))
				.collect(toList());

		// sort names and group by the first letter
		Map<Character, List<String>> directory = randomnames.stream().sorted()
				.collect(groupingBy(name -> name.charAt(0)));

		// print a nicely-formatted student directory
		directory.forEach((letter, names) -> System.out.println(letter + "\n\t"
				+ names.stream().collect(joining("\n\t"))));

	}

	/**
	 * ## The Random String Generator
	 *
	 * This method doesn't do much, except for generating a random string. It:
	 *
	 * Generates a random string at a given length, `length` * Uses only
	 * characters in the range given by `from` and `to`.
	 *
	 * Example:
	 *
	 * ```java randomString(new Random(), 'a', 'z', 10); ```
	 *
	 * @param r
	 *            the random number generator
	 * @param from
	 *            the first character in the character range, inclusive
	 * @param to
	 *            the last character in the character range, inclusive
	 * @param length
	 *            the length of the generated string
	 * @return the generated string of length `length`
	 */
	public static String randomString(Random r, char from, char to, int length) {
		return r.ints(from, to + 1)
				.limit(length)
				.collect(() -> new StringBuffer(),
						(sb, c) -> sb.append((char) c),
						(sb1, sb2) -> sb1.append(sb2)).toString();
	}
}
