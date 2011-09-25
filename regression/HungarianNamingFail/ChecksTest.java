package name.wadewalker.checks;

import java.util.HashMap;
import java.util.Map;

//==============================================================================
/**
 * Lots of variables that fail the Hungarian naming check.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class Test {

    /** Comment. */
    private static boolean ms1;

    /** Comment. */
    private static boolean [] ms2;

    /** Comment. */
    private static byte ms3;

    /** Comment. */
    private static byte [] ms4;

    /** Comment. */
    private static char ms5;

    /** Comment. */
    private static char [] ms6;

    /** Comment. */
    private static double ms7;

    /** Comment. */
    private static double [] ms8;

    /** Comment. */
    private static float ms9;

    /** Comment. */
    private static float [] ms10;

    /** Comment. */
    private static int ms11;

    /** Comment. */
    private static int [] ms12;

    /** Comment. */
    private static long ms13;

    /** Comment. */
    private static long [] ms14;

    /** Comment. */
    private static Object ms15;

    /** Comment. */
    private static Object [] ms16;

    /** Comment. */
    private static short ms17;

    /** Comment. */
    private static short [] ms18;

    /** Comment. */
    private static String ms19;

    /** Comment. */
    private static String [] ms20;

    /** Comment. */
    private static StringBuffer ms21;

    /** Comment. */
    private static StringBuffer [] ms22;

    /** Comment. */
    private static HungarianVariableNameFail.A.B ms23;

    /** Comment. */
    private static HungarianVariableNameFail.A.B [] ms24;

    /** Comment. */
    private boolean m1;

    /** Comment. */
    private boolean [] m2;

    /** Comment. */
    private byte m3;

    /** Comment. */
    private byte [] m4;

    /** Comment. */
    private char m5;

    /** Comment. */
    private char [] m6;

    /** Comment. */
    private double m7;

    /** Comment. */
    private double [] m8;

    /** Comment. */
    private float m9;

    /** Comment. */
    private float [] m10;

    /** Comment. */
    private int m11;

    /** Comment. */
    private int [] m12;

    /** Comment. */
    private long m13;

    /** Comment. */
    private long [] m14;

    /** Comment. */
    private Object m15;

    /** Comment. */
    private Object [] m16;

    /** Comment. */
    private short m17;

    /** Comment. */
    private short [] m18;

    /** Comment. */
    private String m19;

    /** Comment. */
    private String [] m20;

    /** Comment. */
    private StringBuffer m21;

    /** Comment. */
    private StringBuffer [] m22;

    /** Comment. */
    private HungarianVariableNameFail.A.B m23;

    /** Comment. */
    private HungarianVariableNameFail.A.B [] m24;

    /** Comment. */
    private Map<String, Object []> m25 = new HashMap<String, Object []>();

    /** Comment. */
    private Map<String, Object []> [] m26;

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
     * @param oParam A vararg parameter.
     */
    void testVarargs( Object... oParam ) {
    }
}
