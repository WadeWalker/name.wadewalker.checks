package name.wadewalker.checks.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;

import com.puppycrawl.tools.checkstyle.Checker;
import com.puppycrawl.tools.checkstyle.ConfigurationLoader;
import com.puppycrawl.tools.checkstyle.DefaultLogger;
import com.puppycrawl.tools.checkstyle.PropertiesExpander;
import com.puppycrawl.tools.checkstyle.api.AuditListener;
import com.puppycrawl.tools.checkstyle.api.CheckstyleException;

import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

//==============================================================================
/**
 * Contains passing and failing unit tests for each check.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class TestChecks {

    /** Checkstyle configuration file. */
    private static String ssConfigFile = "../External/Checkstyle/Checks.xml";

    /** Used to invoke Checkstyle -- static because creation is slow. */
    private static Checker schecker;
    
    /** Test subdirectory inside regression directory. */
    private String sTestSubdir;

    /** Input Java file to check for the current test. */
    private String sCheckedFile;

    /** Current test's output directory. */
    private File fileTestOutputDir;

    /** Output file for current test. */
    private String sTestOutputFile;

    /** Gold file to compare test output to. */
    private String sGoldOutputFile;

    //==============================================================================
    /**
     * Constructor.
     */
    public TestChecks() {
    }

    //==============================================================================
    /**
     * Set up the Checkstyle object.
     * @throws CheckstyleException if the checker setup fails.
     */
    @BeforeClass
    public static void initializeClass() throws CheckstyleException {

        schecker = new Checker();
        // gives Checkstyle access to this project's .class files
        schecker.setModuleClassLoader( Thread.currentThread().getContextClassLoader() );

        String sUserDir = System.getProperty( "user.dir" );

        // set up properties used in XML config file
        Properties properties = new Properties();
        properties.setProperty( "basedir", sUserDir );

        // this shortens paths printed to output files (also skipping the user-specific parts
        // at the start)
        schecker.setBasedir( sUserDir );

        // read configuration file
        schecker.configure( ConfigurationLoader.loadConfiguration( ssConfigFile, new PropertiesExpander( properties ) ) );
    }

    //==============================================================================
    /**
     * Constructor.
     *
     * @param sTestName Name of the test.
     */
    public void initialize( String sTestName ) {

        sTestSubdir = "regression" + File.separator + sTestName;
        sCheckedFile = sTestSubdir + File.separator + "ChecksTest.java";
        fileTestOutputDir = new File( sTestSubdir + File.separator + "test" );
        sTestOutputFile = sTestSubdir + File.separator + "test/test.out";
        sGoldOutputFile = sTestSubdir + File.separator + "gold/test.out";
    }

    //==============================================================================
    /**
     * Tests Hungarian naming passing cases.
     */
    @Test
    public void hungarianNamingPass() {
        initialize( "HungarianNamingPass" );
        runCheckstyle();
    }

    //==============================================================================
    /**
     * Tests Hungarian naming failure cases.
     */
    @Test
    public void hungarianNamingFail() {
        initialize( "HungarianNamingFail" );
        runCheckstyle();
    }

    //==============================================================================
    /**
     * Tests Javadoc template passing cases.
     */
    @Test
    public void javadocTemplatePass() {
        initialize( "JavadocTemplatePass" );
        runCheckstyle();
    }

    //==============================================================================
    /**
     * Tests Javadoc template failure cases.
     */
    @Test
    public void javadocTemplateFail() {
        initialize( "JavadocTemplateFail" );
        runCheckstyle();
    }

    //==============================================================================
    /**
     * Checks for file equality.
     * @param fileA First file to check.
     * @param fileB Second file to check.
     * @return true if the files are different, false otherwise.
     */
    private static boolean diff( File fileA, File fileB ) {
        boolean bReturn = false;

        try {
            BufferedReader bufferedreaderA = new BufferedReader( new FileReader( fileA ) );
            BufferedReader bufferedreaderB = new BufferedReader( new FileReader( fileB ) );

            String sLineA;
            String sLineB;
            boolean bDone = false;

            while( !bDone ) {
                // replace so file paths won't cause spurious cross-platform diffs
                sLineA = bufferedreaderA.readLine();
                sLineB = bufferedreaderB.readLine();

                if( (sLineA == null) && (sLineB == null) ) {
                    bDone = true;
                    break;
                }

                if( ((sLineA != null) && (sLineB == null)) || ((sLineA == null) && (sLineB != null)) ) {
                    bReturn = true;
                    break;
                }

                if( !sLineA.equals( sLineB ) ) {
                    // replace so file paths won't cause spurious cross-platform diffs
                    // NOTE: only need to try this if strings are different
                    sLineA = sLineA.replace( '\\', '/' );
                    sLineB = sLineB.replace( '\\', '/' );

                    if( !sLineA.equals( sLineB ) ) {
                        bReturn = true;
                        break;
                    }
                }
            }
            
            bufferedreaderA.close();
            bufferedreaderB.close();
        }
        catch( FileNotFoundException filenotfoundexception ) {
            assertTrue( false );
        }
        catch( IOException ioexception ) {
            assertTrue( false );
        }

        return( bReturn );
    }

    //==============================================================================
    /**
     * Runs Checkstyle against one input file. Checks the output against a "gold" file
     * that's hand-verified as correct.
     */
    private void runCheckstyle() {

        try {
            // remove and re-create the output directory (insures we don't check against stale output)
            fileTestOutputDir.delete();
            fileTestOutputDir.mkdirs();

            // logger to write this output file
            AuditListener auditlistener = new DefaultLogger( new FileOutputStream( new File( sTestOutputFile ) ), true );
            schecker.addListener( auditlistener );

            // check the file
            schecker.process( Arrays.asList( new File( sCheckedFile ) ) );
            schecker.removeListener( auditlistener );

            // fail if test and gold files differ
            if ( diff( new File( sGoldOutputFile ), new File( sTestOutputFile ) ) )
                assertTrue( "Gold file diff failed.", false );
        }
        catch( FileNotFoundException filenotfoundexception ) {
            assertTrue( "Test threw an exception: " + filenotfoundexception.getMessage(), false );
        }
    }
}
