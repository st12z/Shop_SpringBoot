package com.thucjava.shopapp.utils;

import java.util.Random;

public class TokenRandom {
    public static String randomToken(int len) {
        String chars="0123456789";
        Random random=new Random();
        StringBuilder sb=new StringBuilder();
        for(int i=0;i<len;i++){
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
