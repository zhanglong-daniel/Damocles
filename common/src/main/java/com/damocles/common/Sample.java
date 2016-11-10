package com.damocles.common;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;

/**
 * Created by zhanglong02 on 16/10/24.
 */
public class Sample {

    public static void main(String[] args) {
        System.out.println("^"+removeBlank("")+"$");
        System.out.println("^"+removeBlank(" ")+"$");
        System.out.println("^"+removeBlank("    ")+"$");
        System.out.println("^"+removeBlank("\t")+"$");
        System.out.println("^"+removeBlank("\n")+"$");
        System.out.println("^"+removeBlank("\r")+"$");
        System.out.println("^"+removeBlank("   你   好   ")+"$");

    }

    public static String removeBlank(String str) {
        if (str != null) {
            Pattern p = Pattern.compile("\\s*|\t|\r|\n");
            Matcher m = p.matcher(str);
            str = m.replaceAll("");
        }
        return str;

    }

}
