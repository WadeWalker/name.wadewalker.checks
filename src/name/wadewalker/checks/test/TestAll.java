package name.wadewalker.checks.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

//==============================================================================
/**
 * Wraps all tests for this project into a suite.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
@RunWith( Suite.class )

@SuiteClasses( {
    TestChecks.class } )

public class TestAll {

    //==============================================================================
    /**
     * Can't instantiate this class (it's just here to encapsulate tests).
     */
    private TestAll() {
    }
    
    //==============================================================================
    /**
     * Dummy main so we can create a runnable JAR of the project without getting warnings.
     * @param asArgs Unused parameter.
     */
    public static void main( String [] asArgs ) {
    }
}
