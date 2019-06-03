package utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

/**
 * Description:
 * Date: 2019-06-03
 * @author: Eylaine
 */
public class EmailUtil {

    private static Logger LOGGER = LoggerFactory.getLogger(EmailUtil.class);

    private Properties properties;
    private MimeMessage message;
    private String host;
    private String port;
    private String username;
    private String password;
    private String from;
    private String to;
    private String cc;
    private String timeout;
    private String personal;
    private String subject;

    /**
     * 默认构造方法，初始化邮件配置
     */
    public EmailUtil() {
        properties = new Properties();
        InputStream is = EmailUtil.class.getResourceAsStream("email.properties");
        try {
            properties.load(is);
            host = properties.getProperty("host");
            port = properties.getProperty("port");
            username = properties.getProperty("username");
            password = properties.getProperty("password");
            from = properties.getProperty("from");
            to = properties.getProperty("to");
            cc = properties.getProperty("cc");
            timeout = properties.getProperty("timeout");
            personal = properties.getProperty("personal");
            subject = properties.getProperty("subject");
        } catch (Exception e) {
            LOGGER.error("读取邮件配置失败！！！");
        }
    }

    public void sendHtmlMail(String content) throws MessagingException {
        initMessage();
        Multipart multipart = new MimeMultipart();

        BodyPart html = new MimeBodyPart();
        html.setContent(content, "text/html;charset=utf-8");
        multipart.addBodyPart(html);
        message.setContent(multipart);
        Transport.send(message);
    }

    public void sendTextMail(String text) throws MessagingException {
        initMessage();
        message.setText(text);
        Transport.send(message);
    }

    /**
     *
     */
    private void initProperties() {
        properties.setProperty("mail.smtp.host", host);
        properties.setProperty("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.user", username);
        properties.setProperty("mail.smtp.pass", password);
    }

    /**
     *
     */
    private void initMessage() throws MessagingException {
        initProperties();
        Session session = Session.getInstance(properties);

        message = new MimeMessage(session);
        message.setSubject(subject);
        message.setFrom(from);
        message.setRecipient(MimeMessage.RecipientType.TO, new InternetAddress(to));
        message.setRecipient(MimeMessage.RecipientType.CC, new InternetAddress(cc));
        message.setSentDate(new Date());
    }

}
