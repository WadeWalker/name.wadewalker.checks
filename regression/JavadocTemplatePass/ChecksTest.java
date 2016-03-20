package name.wadewalker.checks;

//==============================================================================
/**
 * Tests Javadoc comments that match the template.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class Test {

    //==============================================================================
    /**
     * Constructor.
     */
    public Test() {
    }

    //==============================================================================
    /**
     * Normal method comment.
     * 
     * @param iParam With some comment.
     */
    public void method1( int iParam ) {

        /**
         * Inner classes don't need bars.
         */
        class InnerClass {

            /**
             * Inner class methods don't need bars.
             */
            void innerClassMethod1() {
            }
        }
    }
}
 