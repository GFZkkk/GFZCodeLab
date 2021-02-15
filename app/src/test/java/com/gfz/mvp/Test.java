package com.gfz.mvp;

import java.util.Arrays;

/**
 * Created by gaofengze on 2020/7/14.
 */
public class Test {
    @org.junit.Test
    public void main(){
//        System.out.println(",a,".split(",").length);
//        float n1 = Float.parseFloat("10.3");
//        float n2 = Float.parseFloat("1.1");
//        System.out.println(getMoneyNotZero((n1 * 100 - n2 * 100) /100.0f));
        System.out.println(Arrays.asList("2012.19.11".split("\\.")));
    }

    public String getMoneyNotZero(double money){
        money = ((int)(money * 1000.0) + 5) / 10 / 100.0;
        if((int)money - money == 0) {
            return String.valueOf((int)money);
        }
        return String.valueOf(money);
    }
}
