package com.sunxin.quickindex;

/**
 * Created by sunxin on 2016/10/3.
 */
public class Friend implements Comparable<Friend>{

    public String name;
    public String pinYin;


    public Friend(String name) {
        this.name = name;
        //一开始就转化好拼音
        pinYin = PinYinUtil.getPinYin(name);
    }



    @Override
    public int compareTo(Friend another) {
        return pinYin.compareTo(another.pinYin);
    }
}
