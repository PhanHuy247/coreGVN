/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vn.ntsc.chatserver.messageio;

/**
 *
 * @author tuannxv00804
 */
public class StringCoder {
    
    public static final String OpenBracket_Code = "&ob";
    public static final String CloseBracket_Code = "&cb";
    public static final String Equal_Code = "&eq";
    public static final String Semicolon_Code = "&sc";
    
    public static final String OpenBracket = "{";
    public static final String CloseBracket = "}";
    public static final String Equal = "=";
    public static final String Semicolon = ";";
    
    public static final String OpenBracket_Pattern = "\\{";
    public static final String CloseBracket_Pattern = "\\}";
    public static final String Equal_Pattern = "=";
    public static final String Semicolon_Pattern = ";";
    
    public static final String BlankChar = " ";
    
    public static String encode( String plainStr ){
        plainStr = plainStr.replaceAll( OpenBracket_Pattern, OpenBracket_Code );
        plainStr = plainStr.replaceAll( CloseBracket_Pattern, CloseBracket_Code );
//        plainStr = plainStr.replaceAll( Equal_Pattern, Equal );
        plainStr = plainStr.replaceAll( Semicolon_Pattern, Semicolon_Code );
        return plainStr;
    }
    
    public static String decode( String encodedStr ){
        encodedStr = encodedStr.replaceAll( OpenBracket_Code, OpenBracket );
        encodedStr = encodedStr.replaceAll( CloseBracket_Code, CloseBracket );
//        encodedStr = encodedStr.replaceAll( Equal_Code, Equal );
        encodedStr = encodedStr.replaceAll( Semicolon_Code, Semicolon );
        return encodedStr;
    }
    
}
