/**
 * BSD-style license; for more info see http://pmd.sourceforge.net/license.html
 */
/* Generated By:JJTree: Do not edit this line. ASTLiteral.java */

package net.sourceforge.pmd.lang.java.ast;

import java.util.regex.Pattern;

public class ASTLiteral extends AbstractJavaTypeNode {

    private boolean isInt;
    private boolean isFloat;
    private boolean isChar;
    private boolean isString;

    /**
     * Pattern used to detect a single escaped character or octal character in a
     * String.
     */
    private static final Pattern SINGLE_CHAR_ESCAPE_PATTERN = Pattern
            .compile("^\"\\\\(([ntbrf\\\\'\\\"])|([0-7][0-7]?)|([0-3][0-7][0-7]))\"");

    public ASTLiteral(int id) {
        super(id);
    }

    public ASTLiteral(JavaParser p, int id) {
        super(p, id);
    }

    /**
     * Accept the visitor. *
     */
    @Override
    public Object jjtAccept(JavaParserVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    public void setIntLiteral() {
        this.isInt = true;
    }

    public boolean isIntLiteral() {
        String image = getImage();
        if (isInt && image != null && image.length() > 0) {
            if (!image.endsWith("l") && !image.endsWith("L")) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether this literal is a long integer.
     * 
     * @return <code>true</code> if this literal is a long
     */
    public boolean isLongLiteral() {
        String image = getImage();
        if (isInt && image != null && image.length() > 0) {
            if (image.endsWith("l") || image.endsWith("L")) {
                return true;
            }
        }
        return false;
    }

    public void setFloatLiteral() {
        this.isFloat = true;
    }

    public boolean isFloatLiteral() {
        String image = getImage();
        if (isFloat && image != null && image.length() > 0) {
            char lastChar = image.charAt(image.length() - 1);
            if (lastChar == 'f' || lastChar == 'F') {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks whether this literal describes a double.
     * 
     * @return <code>true</code> if this literal is a double.
     */
    public boolean isDoubleLiteral() {
        String image = getImage();
        if (isFloat && image != null && image.length() > 0) {
            char lastChar = image.charAt(image.length() - 1);
            if (lastChar == 'd' || lastChar == 'D' || Character.isDigit(lastChar) || lastChar == '.') {
                return true;
            }
        }
        return false;
    }

    public void setCharLiteral() {
        this.isChar = true;
    }

    public boolean isCharLiteral() {
        return isChar;
    }

    public void setStringLiteral() {
        this.isString = true;
    }

    public boolean isStringLiteral() {
        return isString;
    }

    /**
     * Tries to reconstruct the original string literal. If the original length
     * is greater than the parsed String literal, then probably some unicode
     * escape sequences have been used.
     *
     * @return
     */
    public String getEscapedStringLiteral() {
        String image = getImage();
        if (!isStringLiteral() && !isCharLiteral()) {
            return image;
        }
        int fullLength = getEndColumn() - getBeginColumn();
        if (fullLength > image.length()) {
            StringBuilder result = new StringBuilder(fullLength);
            for (int i = 0; i < image.length(); i++) {
                char c = image.charAt(i);
                if (c < 0x20 || c > 0xff || image.length() == 1) {
                    String hex = "0000" + Integer.toHexString(c);
                    result.append("\\u").append(hex.substring(hex.length() - 4));
                } else {
                    result.append(c);
                }
            }
            return result.toString();
        }
        return image;
    }

    /**
     * Returns true if this is a String literal with only one character. Handles
     * octal and escape characters.
     *
     * @return true is this is a String literal with only one character
     */
    public boolean isSingleCharacterStringLiteral() {
        if (isString) {
            String image = getImage();
            int length = image.length();
            if (length == 3) {
                return true;
            } else if (image.charAt(1) == '\\') {
                return SINGLE_CHAR_ESCAPE_PATTERN.matcher(image).matches();
            }
        }
        return false;
    }

}
