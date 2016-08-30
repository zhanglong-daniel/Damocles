package com.damocles.common;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zhanglong02 on 16/7/15.
 */
public class Sample {

    public static void main(String... args) {
        String str;
        str = "张龙daniel";
        str = str.replaceAll("[a-zA-Z]*", "");
        System.out.println(str);
        str = "张龙daniel哈哈";
        str = str.replaceAll("[a-zA-Z]*", "");
        System.out.println(str);
        str = "raymond";
        str = str.replaceAll("[a-zA-Z]*", "");
        System.out.println(str);
    }

}
