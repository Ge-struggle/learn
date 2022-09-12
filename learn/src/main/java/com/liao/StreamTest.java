package com.liao;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class StreamTest {
    public static void main(String[] args) {
        List<String> ls=new ArrayList<>();
        String[] s=new String[]{"a","e","i","o","u"};
        Collections.addAll(ls,s);
        Stream<String> stringStream=ls.stream();

    }
}
