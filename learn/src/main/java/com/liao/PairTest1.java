package com.liao;

public class PairTest1 {

    public static void main(String[] args) {
        String[] words={"mary","has","a","little","cat"};
        Pair<String> mm=ArrayAlg.minmax(words);
        System.out.println("min="+mm.getFrist()+" max="+mm.getSecond());
    }

}

class ArrayAlg{
    public static Pair<String> minmax(String[] a){
        if(a == null || a.length==0){
            return  null;
        }

        String min=a[0];
        String max=a[0];
        for(String s: a){
            if(min.compareTo(s)>0){
                min=s;
            }
            if(max.compareTo(s)<0){
                max=s;
            }
        }

        return new Pair<>(min,max);
    }

    public static <T> T getMiddle(T... a){
        return a[a.length / 2];
    }
}
