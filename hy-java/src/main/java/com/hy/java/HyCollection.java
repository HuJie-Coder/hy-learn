package com.hy.java;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * HyCollection
 *
 * @author Jie.Hu
 * @date 7/11/21 3:09 AM
 */
public class HyCollection {

    public static void main(String[] args) {

        List<String> list = new ArrayList<>();
        list.add("Jayden");
        list.add("ShuWen");
        list.add("Help");
        list.add("Window");
        Iterator<String> iterator = list.iterator();

        while (iterator.hasNext()){
            String value = iterator.next();
            if ("ShuWen".equals(value)) {
                iterator.remove();
            }
        }
        list.stream().forEach(System.out::println);
    }

}
