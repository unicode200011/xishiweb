package com.stylefeng.guns.admin.common.exception;

import java.io.*;

public class HtmlToPdfInterceptor extends Thread {
    private InputStream is;
    private OutputStream out;

    public HtmlToPdfInterceptor(InputStream is){
        this.is = is;
    }
    public HtmlToPdfInterceptor(InputStream is,OutputStream out){
        this.is = is;
        this.out = out;
    }

    @Override
    public void run(){
        try{
            InputStreamReader isr = new InputStreamReader(is, "utf-8");
            BufferedReader br = new BufferedReader(isr);
            String line = null;
            while ((line = br.readLine()) != null) {
                System.out.println(line.toString()); //输出内容
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
