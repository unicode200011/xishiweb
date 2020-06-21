package com.stylefeng.guns.admin.core.util;

import com.itextpdf.text.pdf.BaseFont;
import org.xhtmlrenderer.pdf.ITextFontResolver;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;

/**
 * @Project: &nbspguns
 * @Package: com.stylefeng.guns.admin.core.util
 * @Author: 无痕无边
 * @CreateDate: 2019/05/04 14:50
 * @Version: 1.0
 * @Description:
 * @Company:
 */
public class FileTypeConvertUtil {
    /**
     * 将 HTML 转成 PD 格式的文件。html 文件的格式比较严格
     * @param htmlFile
     * @param pdfFile
     * @throws Exception
     */
    // <!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
    public static void html2pdf(String htmlFile, String pdfFile) throws Exception {
        // step 1
        String url = new File(htmlFile).toURI().toURL().toString();
        System.out.println(url);

        // step 2
        OutputStream os = new FileOutputStream(pdfFile);
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(url);

        // 图片为本地的绝对路径时, 如 http://www.baidu.com/a.jpg, 则为
        renderer.getSharedContext().setBaseURL("http://www.baidu.com/");
        // 图片为 HTTP 链接时，src 只需填写相对路径，如 D:/a.jpg, 则为
        renderer.getSharedContext().setBaseURL("file:/D:/");

        // step 3 解决中文支持
        ITextFontResolver fontResolver = renderer.getFontResolver();
        if("linux".equals(getCurrentOperatingSystem())){
            fontResolver.addFont("/usr/share/fonts/chiness/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        }else{
            fontResolver.addFont("c:/Windows/Fonts/simsun.ttc", BaseFont.IDENTITY_H, BaseFont.NOT_EMBEDDED);
        }

        renderer.layout();
        renderer.createPDF(os);
        os.close();

        System.out.println("create pdf done!!");

    }

    public static String getCurrentOperatingSystem(){
        String os = System.getProperty("os.name").toLowerCase();
        System.out.println("--------- 当前操作系统是 -----------" + os);
        return os;
    }


    public static void main(String[] args) {
        String htmlFile = "C:\\Users\\lixia\\Downloads\\login.html";
        String pdfFile = "C:\\Users\\lixia\\Desktop\\122.pdf";
        try {
            FileTypeConvertUtil.html2pdf(htmlFile, pdfFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
