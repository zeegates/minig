package org.minig.server.service.submission;

import java.io.IOException;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.minig.MailAuthentication;
import org.minig.server.service.impl.MailContext;
import org.minig.server.service.impl.helper.MessageMapper;
import org.minig.server.service.impl.helper.mime.Mime4jMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Component;

@Component
public class Submission {

    private static final String DSN0 = "mail.smtp.dsn.notify";
    private static final String DSN1 = "mail.smtp.dsn.ret";

    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MailContext mailContext;

    @Autowired
    private MailAuthentication authentication;

    public void submitWithDSN(Mime4jMessage message) {
        JavaMailSenderImpl mailHelper = new JavaMailSenderImpl();
        Session session = mailContext.getSession();
        Properties properties = session.getProperties();

        properties.put(DSN0, "SUCCESS,FAILURE,DELAY ORCPT=rfc822;" + authentication.getEmailAddress());
        properties.put(DSN1, "FULL");

        mailHelper.setSession(session);

        // always set current authenticated user as sender for security reasons
        message.setSender(authentication.getEmailAddress());
        MimeMessage target = messageMapper.toMimeMessage(message);

        try {
            mailHelper.send(target);
        } finally {
            properties.remove(DSN0);
            properties.remove(DSN1);
        }
    }

    public void submit(Mime4jMessage message) {
        JavaMailSenderImpl mailHelper = new JavaMailSenderImpl();
        Session session = mailContext.getSession();
        mailHelper.setSession(session);

        // always set current authenticated user as sender for security reasons
        message.setSender(authentication.getEmailAddress());
        MimeMessage target = messageMapper.toMimeMessage(message);

        mailHelper.send(target);
    }

    public void submit(MimeMessage message) {
        JavaMailSenderImpl mailHelper = new JavaMailSenderImpl();
        Session session = mailContext.getSession();
        mailHelper.setSession(session);

        try {
            // always set current authenticated user as sender for security
            // reasons
            InternetAddress internetAddress = new InternetAddress(authentication.getEmailAddress());
            message.setFrom(internetAddress);

            mailHelper.send(message);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }
}
