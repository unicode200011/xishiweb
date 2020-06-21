package com.stylefeng.guns.test;

import org.junit.Test;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Project: &nbspguns
 * @Package: com.stylefeng.guns.test
 * @Author: 无痕无边
 * @CreateDate: 2019/02/26 14:51
 * @Version: 1.0
 * @Description:
 * @Company:
 */
public class QuartzTest {

    @Test
    public void testQuartz(){
        long current=System.currentTimeMillis();//当前时间毫秒数
        long zero=current/(1000*3600*24)*(1000*3600*24)- TimeZone.getDefault().getRawOffset();//今天零点零分零秒的毫秒数
        long twelve=zero+24*60*60*1000-1;//今天23点59分59秒的毫秒数
        long yesterday=System.currentTimeMillis()-24*60*60*1000;//昨天的这一时间的毫秒数
        System.out.println(new Timestamp(current));//当前时间
        System.out.println(new Timestamp(yesterday));//昨天这一时间点
        System.out.println(new Timestamp(zero));//今天零点零分零秒
        System.out.println(new Timestamp(twelve));//今天23点59分59秒
    }

    @Test
    public void testArray() {
        String[] strs = {"1","2","3","4","5"};
        List<String> list = new ArrayList<String>();
        Collections.addAll(list,strs);
        boolean contains = list.contains("1");
        System.out.println("contains = " + contains);
    }

    @Test
    public void testCount(){
        long a = this.f(12);
        System.out.println("a = " + a);
    }

    private long f(int a){
        if (a == 0){
            return 2L;
        }else {
            return f(a - 1) + (long)(f(a - 1) / 2);
        }
    }


    @Test
    public void testInt(){
        int a = 100;
        int b = 100;
        System.out.println(a == b);
        System.out.println(" ================================= ");
        Integer c = new Integer(100);
        Integer d = new Integer(100);
        System.out.println(c == d);
    }

    @Test
    public void testDate(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        String format = sdf.format(date);
        try {
            Date parse = sdf.parse(format);
            System.out.println(parse.getTime() < date.getTime());
            System.out.println("=====================================");
            System.out.println(date.getTime());
            System.out.println(parse.getTime());
            System.out.println("=====================================");
            System.out.println(date);
            System.out.println(parse);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testltgt(){
        int a = 5;
        if (a < 6 && a > 4){
            System.out.println("是的");
        }
    }

}
