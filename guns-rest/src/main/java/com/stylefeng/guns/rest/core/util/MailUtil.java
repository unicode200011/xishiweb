package com.stylefeng.guns.rest.core.util;

import com.baomidou.mybatisplus.toolkit.IOUtils;
import com.stylefent.guns.entity.vo.MailVo;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.InputStreamReader;
import java.io.StringWriter;


/**
 * @description: 邮件发送类
 * @author: lx
 */
@Slf4j
@Component
public class MailUtil {
    private final static String ENCODING = "UTF-8";
    private final static String TEMPLATE_NAME = "mail";
    private final static String FROM = "<577746010@qq.com>";
    private final static String TEXT = "西施小助手";
    private final static String TEMPLATE_PATH = "mail/mail.ftl";
    private static String SUBJECT = "直播鉴黄提醒,主播【{}】疑似涉嫌违规";
    private static Template MAIL_TEMPLATE;

    @Autowired
    private JavaMailSender sender;

//    static {
//        log.info("######  初始化邮件模板...  ######");
//        Configuration cfg = new Configuration(Configuration.VERSION_2_3_21);
//        cfg.setDefaultEncoding(ENCODING);
//        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
//        cfg.setTemplateLoader(stringTemplateLoader);
//        InputStreamReader isr = null;
//        try {
//            isr = new InputStreamReader(new ClassPathResource(TEMPLATE_PATH).getInputStream(), ENCODING);
//            MAIL_TEMPLATE = new Template(TEMPLATE_NAME, isr, cfg);
//        } catch (Exception e) {
//            MAIL_TEMPLATE = null;
//            log.error("######  初始化邮件模板错误...  ######", e);
//        } finally {
//            IOUtils.closeQuietly(isr);
//        }
//        log.info("######  初始化邮件完成...  ######");
//    }

    public static String merge(MailVo mailVo) {
        StringWriter out = null;
        try {
            out = new StringWriter();
            MAIL_TEMPLATE.process(mailVo, out);
            out.flush();
            return out.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        } finally {
            IOUtils.closeQuietly(out);
        }
    }

    public boolean sendMail(MailVo mailVo, String... to) {
        try {
            log.info("####### 开始发送邮件,{}... #######", mailVo);
            MimeMessage mailMessage = this.sender.createMimeMessage();
            MimeMessageHelper message = new MimeMessageHelper(mailMessage, true, ENCODING);
            message.setFrom(new InternetAddress(MimeUtility.encodeText(TEXT, ENCODING, null) + FROM));
            message.setTo(to);
            message.setSubject(SUBJECT.replace("{}", mailVo.getName()));
            String content = merge(mailVo);
            log.info("####### 邮件内容:{} ######", content);
            message.setText(content, true);
            this.sender.send(mailMessage);
            log.info("####### 邮件发送成功... #######");
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
