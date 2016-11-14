package com.merlin.utils;

/**
 * Created by Merlin on 11/11/2016.
 */

public class StringUtils {

    public static boolean isEmpty(String string){
        if(string == null){
            return true;
        }
        if(string.trim().length() == 0){
            return true;
        }
        return false;
    }

    public static boolean areEmpty(String... strings){
        for (String string: strings) {
            if(isEmpty(string)){
                return true;
            }
        }
        return false;
    }
}
