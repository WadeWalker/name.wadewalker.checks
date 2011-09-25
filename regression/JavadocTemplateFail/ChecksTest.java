package name.wadewalker.checks;

/**
 * Tests Javadoc comments that don't match the template.
 *
 * Missing copyright line here.
 *
 * @author Wade Walker
 */
public class Test {

    /**
     * Constructor.
     */
    public Test() {
    }

    //====
    /**
     * Tests the trivial parameter comment that Eclipse generates by default.
     * 
     * @param iWhatever int
     */
    public void method1( int iWhatever ) {
    }

        //==============================================================================
    /**
     * Insures that equals sign bars can be indented by multiples of four.
     */
    public void method2() {
    }
}
 