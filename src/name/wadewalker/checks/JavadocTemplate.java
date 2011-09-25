package name.wadewalker.checks;

import java.util.regex.Pattern;

import com.puppycrawl.tools.checkstyle.api.Check;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.FileContents;
import com.puppycrawl.tools.checkstyle.api.TextBlock;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

//==============================================================================
/**
 * Checks that Javadoc for classes and methods conforms to the standard template.
 *
 * Copyright (c) 2011 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class JavadocTemplate extends Check {

    /** Matches equals sign bar before class or interface comment (no spaces to left). */
    private static final Pattern spatternClassCommentBar = Pattern.compile( "//={78}" );

    /** Matches equals sign bar before method comment (multiple of four spaces to left). */
    private static final Pattern spatternMethodCommentBar = Pattern.compile( "(\\s{4})+//={78}" );

    /** Warning message for comment bar. */
    private static final String ssCommentBarWarning = "Missing or incorrect equals sign bar above Javadoc comment";
    
    /** Matches private copyright line. */
    private static final String ssCopyrightPrivate = " \\* Copyright \\(c\\) \\d\\d\\d\\d(\\s*-\\s*\\d\\d\\d\\d)? Wade Walker\\. All rights reserved\\.\\s*";

    /** Matches public copyright line. */
    private static final String ssCopyrightPublic = " \\* Copyright \\(c\\) \\d\\d\\d\\d(\\s*-\\s*\\d\\d\\d\\d)? Wade Walker\\. Free for any use, but credit is appreciated\\.\\s*";

    /** Matches any copyright line. */
    private static final Pattern spatternClassCopyright = Pattern.compile( "(" + ssCopyrightPrivate + ")|(" + ssCopyrightPublic + ")" );

    /** Warning message for copyright line. */
    private static final String ssCopyrightWarning = "Missing or incorrect copyright line in Javadoc comment";

    /** Matches a trivial Javadoc param tag (one with no words or only one word). */
    private static final Pattern spatternTrivialParam = Pattern.compile( ".*@param(\\s+\\S+){1,2}" );

    /** Warning message for trivial comment. */
    private static final String ssTrivialParamWarning = "Empty or trivial Javadoc parameter comment";

    //==============================================================================
    /**
     * Constructor.
     */
    public JavadocTemplate() {
    }

    //==============================================================================
    /**
     * Accessor.
     * @return the token types of the grammar terminals to check.
     * @see com.puppycrawl.tools.checkstyle.api.Check#getDefaultTokens()
     */
    public int [] getDefaultTokens() {
        return( new int [] {TokenTypes.CLASS_DEF, TokenTypes.CTOR_DEF, TokenTypes.INTERFACE_DEF, TokenTypes.METHOD_DEF} );
    }

    //==============================================================================
    /**
     * Checks for one Javadoc comment.
     *
     * @param detailast AST node being visited.
     * @see com.puppycrawl.tools.checkstyle.api.Check#visitToken(com.puppycrawl.tools.checkstyle.api.DetailAST)
     */
    public void visitToken( DetailAST detailast ) {

        FileContents filecontents = getFileContents();
        TextBlock textblockJavadoc = filecontents.getJavadocBefore( detailast.getLineNo() );

        // Checkstyle already has a rule for total lack of Javadoc, so don't check for that case
        if( textblockJavadoc == null )
            return;

        checkCommentBar( detailast, filecontents, textblockJavadoc.getStartLineNo() - 1 );
        checkJavadocContents( detailast, filecontents, textblockJavadoc.getStartLineNo(), textblockJavadoc.getEndLineNo() );
    }

    //==============================================================================
    /**
      * Accessor.
      *
      * @param detailast AST to check.
      * @return true if the class is a nested class, false otherwise.
      */
    private boolean isNestedClass( DetailAST detailast ) {

        int iNestingLevel = 0;
        
        // move up through nesting levels
        DetailAST detailastCurrent = detailast;
        while( detailastCurrent != null ) {
            if(    (detailastCurrent.getType() == TokenTypes.ANNOTATION_DEF)
                || (detailastCurrent.getType() == TokenTypes.CLASS_DEF)
                || (detailastCurrent.getType() == TokenTypes.ENUM_DEF)
                || (detailastCurrent.getType() == TokenTypes.INTERFACE_DEF) )
                iNestingLevel++;

            // no need to count more than one nesting level
            if( iNestingLevel > 1 )
                break;

            detailastCurrent = detailastCurrent.getParent();
        }

        return( iNestingLevel > 1 );
    }

    //==============================================================================
    /**
     * Checks this Javadoc to make sure the comment bar is present and correctly formatted.
     *
     * @param detailast The AST node the Javadoc is before.
     * @param filecontents Whole contents of the file.
     * @param iCommentBarLine The line we expect to find the comment bar on (one line before
     * the start line of the Javadoc comment).
     */
    private void checkCommentBar( DetailAST detailast, FileContents filecontents, int iCommentBarLine ) {

        // don't check nested classes
        if( isNestedClass( detailast ) )
            return;

        // get comment bar
        String sCommentBarLine = iCommentBarLine >= 1 ? filecontents.getLines()[iCommentBarLine - 1] : null;

        Pattern patternCommentBar = ((detailast.getType() == TokenTypes.CLASS_DEF) || (detailast.getType() == TokenTypes.INTERFACE_DEF))
              ? spatternClassCommentBar : spatternMethodCommentBar;

        if( (sCommentBarLine == null) || !patternCommentBar.matcher( sCommentBarLine ).matches() )
            log( iCommentBarLine, ssCommentBarWarning );
    }

    //==============================================================================
    /**
     * Checks for the correct copyright line in class Javadoc, and checks for
     * empty or trivial parameter comments in method Javadoc.
     *
     * @param detailast AST node after the Javadoc block.
     * @param filecontents Whole contents of the file.
     * @param iJavadocStartLine The start line of the Javadoc comment in the file.
     * @param iJavadocEndLine The end line of the Javadoc comment in the file.
     */
    private void checkJavadocContents( DetailAST detailast, FileContents filecontents, int iJavadocStartLine, int iJavadocEndLine ) {

        // copyright line only needed in top class
        boolean bTopClass = (detailast.getParent() == null) && (detailast.getType() == TokenTypes.CLASS_DEF);

        // parameter check only in methods
        boolean bMethodJavadoc = (detailast.getType() == TokenTypes.METHOD_DEF) || (detailast.getType() == TokenTypes.CTOR_DEF);

        boolean bFoundCopyright = false;

        // check every line in Javadoc
        for( int iLine = iJavadocStartLine; iLine < (iJavadocEndLine - 1); iLine++ ) {
            // check copyright match
            if( !bFoundCopyright && bTopClass && spatternClassCopyright.matcher( filecontents.getLines()[iLine] ).matches() )
                bFoundCopyright = true;

            // check for trivial parameter comment
            if( bMethodJavadoc && spatternTrivialParam.matcher( filecontents.getLines()[iLine] ).matches() )
                log( iLine + 1, ssTrivialParamWarning );
        }

        if( bTopClass && !bFoundCopyright )
            log( iJavadocEndLine, ssCopyrightWarning );
    }
}
