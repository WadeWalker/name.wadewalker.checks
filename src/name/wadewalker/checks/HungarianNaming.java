package name.wadewalker.checks;

import com.puppycrawl.tools.checkstyle.api.AbstractCheck;
import com.puppycrawl.tools.checkstyle.api.DetailAST;
import com.puppycrawl.tools.checkstyle.api.TokenTypes;

//==============================================================================
/**
 * A custom check for (modified) Hungarian naming of parameters and variables. 
 * Names use the convention
 *
 * [s][a]{prefix}[{name}]
 *
 * where
 *
 * - "s" means "static"
 * - "a" means "array"
 * - {prefix} is a prefix based on the variable type
 * - {name} is the variable name (optional if the prefix alone is a suitable name)
 *
 * The prefixes are:
 *
 * b: boolean
 * b: byte
 * c: char
 * d: double
 * f: float
 * i: int
 * l: long
 * o: Object
 * s: short or String
 * sb: StringBuffer or StringBuilder
 * {class name}: {class name}
 *
 * Copyright (c) 2011-2016 Wade Walker. All rights reserved.
 * @author Wade Walker
 */
public class HungarianNaming extends AbstractCheck {

    /** Format string for warning message.
     * 1: type
     * 2: " member", " parameter", or ""
     * 2: name
     * 3: prefix
     */
    private static final String ssWarningMsg = "Prepend %s%s name ''%s'' with ''%s''";

    /** Used when warning is about a member declaration. */
    private static final String ssMember = " member";
    
    /** Used when warning is about a patameter declaration. */
    private static final String ssParameter = " parameter";
    
    /** Java native types to check prefixes of. */
    private static final String [] sasNativeTypeNames = {
        "boolean", "byte", "char", "double", "float", "int", "long", "short"
    };

    /** Prefixes to use for native types (same order as type names). */
    private static final String [] sasNativePrefixes = {
        "b", "b", "c", "d", "f", "i", "l", "s"
    };

    /** Token types of the native types in the AST. */
    private static final int [] saiNativeTokenTypes = {
        TokenTypes.LITERAL_BOOLEAN, TokenTypes.LITERAL_BYTE, TokenTypes.LITERAL_CHAR, TokenTypes.LITERAL_DOUBLE,
        TokenTypes.LITERAL_FLOAT, TokenTypes.LITERAL_INT, TokenTypes.LITERAL_LONG, TokenTypes.LITERAL_SHORT
    };

    //==============================================================================
    /**
     * Constructor.
     */
    public HungarianNaming() {
    }

    //==============================================================================
    /**
     * Accessor.
     * @return the token types of the grammar terminals to check.
     * @see com.puppycrawl.tools.checkstyle.api.AbstractCheck#getDefaultTokens()
     */
    public int [] getDefaultTokens() {
        return new int [] {TokenTypes.PARAMETER_DEF, TokenTypes.VARIABLE_DEF};
    }

    //==============================================================================
    /**
     * Accessor.
     * @param detailast AST of a parameter or member.
     * @return true if the AST is a static, false otherwise.
     */
    private boolean isStatic( DetailAST detailast ) {
        DetailAST detailastModifiers = detailast.findFirstToken( TokenTypes.MODIFIERS );
        return( (detailastModifiers != null) && detailastModifiers.branchContains( TokenTypes.LITERAL_STATIC ) );
    }


    //==============================================================================
    /**
     * Gets the class name from an AST that's been advanced past any array declarators.
     * Concatenates names for inner types like Map.Entry.
     *
     * @param detailast AST to find the class name in.
     * @return the class name, or null if we can't find it (due to calling this method
     * on an unexpected part of the AST).
     */
    private String findClassName( DetailAST detailast ) {

        DetailAST detailastCurr = detailast;

        while( (detailastCurr != null) && (detailastCurr.getType() != TokenTypes.IDENT) ) {
            // dot means inner class, so traverse to inner name
            if ( (detailastCurr.getType() == TokenTypes.DOT) && (detailastCurr.getFirstChild() != null) ) {
                detailastCurr = detailastCurr.getFirstChild().getNextSibling();
            }
            else
                detailastCurr = null;
        }

        if ( detailastCurr != null )
            return( detailastCurr.getText() );
        else
            return( null );
    }

    //==============================================================================
    /**
     * Checks for naming violations. Called only on AST nodes of the grammar terminals we're checking.
     *
     * @param detailast AST node being visited.
     * @see com.puppycrawl.tools.checkstyle.api.AbstractCheck#visitToken(com.puppycrawl.tools.checkstyle.api.DetailAST)
     */
    public void visitToken( DetailAST detailast ) {

        // name of the grammar terminal we're called on
        String sTerminalName =   detailast.getType() == TokenTypes.PARAMETER_DEF
                               ? ssParameter
                               : (detailast.getType() == TokenTypes.VARIABLE_DEF ? ssMember : "");

        String sMemberOrParamName = detailast.findFirstToken( TokenTypes.IDENT ).getText();

        // index we're looking at in name (incremented for static and array prefixes)
        int iNameStartIndex = 0;

        // check for static member
        if( isStatic( detailast ) ) {
            if ( !sMemberOrParamName.startsWith( "s", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "static", sTerminalName, sMemberOrParamName, "s" ) );
            else
                iNameStartIndex++;
        }

        // check for variable length argument list
        if( detailast.findFirstToken( TokenTypes.ELLIPSIS ) != null ) {
            if ( !sMemberOrParamName.startsWith( "a", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "varargs", sTerminalName, sMemberOrParamName, "a" ) );
            else
                iNameStartIndex++;
        }

        // find type of member or parameter (always present)
        DetailAST detailastType = detailast.findFirstToken( TokenTypes.TYPE );

        // AST after any array declarators
        DetailAST detailastAfterArray = detailastType;

        // check for array member or parameter
        if( detailastType.getFirstChild().getType() == TokenTypes.ARRAY_DECLARATOR ) {

            // find the last ARRAY_DECLARATOR (more than one for multidimensional arrays)
            detailastAfterArray = detailastType.getFirstChild();
            while( detailastAfterArray.getType() == TokenTypes.ARRAY_DECLARATOR )
                detailastAfterArray = detailastAfterArray.getFirstChild();

            detailastAfterArray = detailastAfterArray.getParent();

            if( !sMemberOrParamName.startsWith( "a", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "array", sTerminalName, sMemberOrParamName, "a" ) );
            else
                iNameStartIndex++;
        }

        // check for native types
        for( int i = 0; i < saiNativeTokenTypes.length; i++ ) {
            if( (detailastType.getFirstChild() != null) && detailastType.getFirstChild().branchContains( saiNativeTokenTypes[i] ) ) {
                if( !sMemberOrParamName.startsWith( sasNativePrefixes[i], iNameStartIndex ) )
                    log( detailast.getLineNo(), String.format( ssWarningMsg, sasNativeTypeNames[i], sTerminalName, sMemberOrParamName, sasNativePrefixes[i] ) );

                return;
            }
        }

        String sClassName = findClassName( detailastAfterArray.getFirstChild() );

        // check for String
        if ( sClassName.equals( "String" ) ) {
            if( !sMemberOrParamName.startsWith( "s", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "String", sTerminalName, sMemberOrParamName, "s" ) );
        }
        // check for StringBuffer or StringBuilder
        else if( sClassName.equals( "StringBuffer" ) || sClassName.equals( "StringBuilder" ) ) {
            if( !sMemberOrParamName.startsWith( "sb", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "StringBuffer or StringBuilder", sTerminalName, sMemberOrParamName, "sb" ) );
        }
        // check for Object
        else if( sClassName.equals( "Object" ) ) {
            if( !sMemberOrParamName.startsWith( "o", iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, "Object", sTerminalName, sMemberOrParamName, "o" ) );
        }
        // check any other class type for prepended lowercase class name
        else {
            String sLowercaseClassName = sClassName.toLowerCase();
    
            if( !sMemberOrParamName.startsWith( sLowercaseClassName, iNameStartIndex ) )
                log( detailast.getLineNo(), String.format( ssWarningMsg, sClassName, sTerminalName, sMemberOrParamName, sLowercaseClassName ) );
        }
    }
}
