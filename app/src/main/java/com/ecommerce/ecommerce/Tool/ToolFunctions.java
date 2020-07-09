package com.ecommerce.ecommerce.Tool;

public class ToolFunctions {
    public static String CapStringFirstLetter(String str)
    {
        if(str.length()==0)
            return "";
        return str.toUpperCase().charAt(0)+str.substring(1,str.length());
    }
}
