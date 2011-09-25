package name.wadewalker.checks;

import java.util.HashMap;
import java.util.Map;

//==============================================================================
/**
 * Same file as the failing Hungarian naming check, but with all the errors corrected.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class Test {

    /** Comment. */
    private static boolean sb1;

    /** Comment. */
    private static boolean [] sab2;

    /** Comment. */
    private static byte sb3;

    /** Comment. */
    private static byte [] sab4;

    /** Comment. */
    private static char sc5;

    /** Comment. */
    private static char [] sac6;

    /** Comment. */
    private static double sd7;

    /** Comment. */
    private static double [] sad8;

    /** Comment. */
    private static float sf9;

    /** Comment. */
    private static float [] saf10;

    /** Comment. */
    private static int si11;

    /** Comment. */
    private static int [] sai12;

    /** Comment. */
    private static long sl13;

    /** Comment. */
    private static long [] sal14;

    /** Comment. */
    private static Object so15;

    /** Comment. */
    private static Object [] sao16;

    /** Comment. */
    private static short ss17;

    /** Comment. */
    private static short [] sas18;

    /** Comment. */
    private static String ss19;

    /** Comment. */
    private static String [] sas20;

    /** Comment. */
    private static StringBuffer ssb21;

    /** Comment. */
    private static StringBuffer [] sasb22;

    /** Comment. */
    private static HungarianVariableNameFail.A.B sb23;

    /** Comment. */
    private static HungarianVariableNameFail.A.B [] sab24;

    /** Comment. */
    private boolean b1;

    /** Comment. */
    private boolean [] ab2;

    /** Comment. */
    private byte b3;

    /** Comment. */
    private byte [] ab4;

    /** Comment. */
    private char c5;

    /** Comment. */
    private char [] ac6;

    /** Comment. */
    private double d7;

    /** Comment. */
    private double [] ad8;

    /** Comment. */
    private float f9;

    /** Comment. */
    private float [] af10;

    /** Comment. */
    private int i11;

    /** Comment. */
    private int [] ai12;

    /** Comment. */
    private long l13;

    /** Comment. */
    private long [] al14;

    /** Comment. */
    private Object o15;

    /** Comment. */
    private Object [] ao16;

    /** Comment. */
    private short s17;

    /** Comment. */
    private short [] as18;

    /** Comment. */
    private String s19;

    /** Comment. */
    private String [] as20;

    /** Comment. */
    private StringBuffer sb21;

    /** Comment. */
    private StringBuffer [] asb22;

    /** Comment. */
    private HungarianVariableNameFail.A.B b23;

    /** Comment. */
    private HungarianVariableNameFail.A.B [] ab24;

    /** Comment. */
    private Map<String, Object []> map25 = new HashMap<String, Object []>();

    /** Comment. */
    private Map<String, Object []> [] amap26;

    /**
     * A.
     */
    public static class A {

        /**
         * A.
         */
        public A() { }

        /**
         * B.
         */
        public static class B {

            /**
             * B.
             */
            public B() { }

        }
    }

    //==============================================================================
    /**
     * Constructor.
     */
    public Test() {
    }

    //==============================================================================
    /**
     * Tests varargs parameter.
     * @param aoParam A vararg parameter.
     */
    void testVarargs( Object... aoParam ) {
    }
}
