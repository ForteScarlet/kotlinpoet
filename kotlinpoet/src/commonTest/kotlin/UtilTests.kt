import kotlin.test.Test
import kotlin.test.assertFalse
import kotlin.test.assertTrue

internal const val REGEX_PATTERN = (
  "((\\p{gc=Lu}+|\\p{gc=Ll}+|\\p{gc=Lt}+|\\p{gc=Lm}+|\\p{gc=Lo}+|\\p{gc=Nl}+)+" +
    "\\d*" +
    "\\p{gc=Lu}*\\p{gc=Ll}*\\p{gc=Lt}*\\p{gc=Lm}*\\p{gc=Lo}*\\p{gc=Nl}*)" +
    "|" +
    "(`[^\n\r`]+`)"
  )

internal expect fun initRegexMatcher(): (String) -> Boolean

/**
 *
 * @author ForteScarlet
 */
class UtilTests {
  private val matcher = initRegexMatcher()
  // private val IDENTIFIER_REGEX = REGEX_PATTERN.toRegex()
  // private val String.isIdentifier get() = IDENTIFIER_REGEX.matches(this)
  private val String.isIdentifier get() = matcher(this)

  @Test
  fun identifierRegexTest() {
    assertTrue("foo".isIdentifier, """"foo" expect true""")
    assertTrue("bAr1".isIdentifier, """"bAr1" expect true""")
    assertFalse("1".isIdentifier, """"1" expect false""")
    assertFalse("♦♥♠♣".isIdentifier, """"♦♥♠♣" expect false""")
    assertTrue("`♦♥♠♣`".isIdentifier, """"`♦♥♠♣`" expect true""")
    assertTrue("`  ♣ !`".isIdentifier, """"`  ♣ !`" expect true""")
    assertFalse("€".isIdentifier, """"€" expect false""")
    assertTrue("`€`".isIdentifier, """"`€`" expect true""")
    assertTrue("`1`".isIdentifier, """"`1`" expect true""")
    assertFalse("```".isIdentifier, """"```" expect false""")
    assertFalse("``".isIdentifier, """"``" expect false""")
    assertFalse("\n".isIdentifier, """"\n" expect false""")
    assertFalse("`\n`".isIdentifier, """"`\n`" expect false""")
    assertFalse("\r".isIdentifier, """"\r" expect false""")
    assertFalse("`\r`".isIdentifier, """"`\r`" expect false""")
    assertTrue("when".isIdentifier, """"when" expect true""")
    assertTrue("fun".isIdentifier, """"fun" expect true""")
    assertFalse("".isIdentifier, """"" expect false""")
  }

}
