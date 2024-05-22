package com.ilm.projecto_ilm_backend.emailService;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;


@Stateless
public class EmailService {
    private final String username= "innovationlabmanagementcs@gmail.com";// Your email username
    private final String password= "xnog bvud syvq rpcv";// Your email password
    // SMTP server port


    public void sendEmail(String to, String verificationLink, boolean confirmationAccount) throws MessagingException, UnsupportedEncodingException {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "*");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));


        String emailContent="";
        if(confirmationAccount) {
            message.setSubject("Confirm Your Account Registration");

            emailContent = "<p>Dear user</p>"
                    + "<p>Thank you for registering with AgileFlow!</p>"
                    + "<p>To complete the registration process and gain full access to your account, please click on the link below to confirm your email address:</p>"
                    + "<p><a href='http://localhost:3000/create-profile/" + verificationLink + "'>http://localhost:3000/create-profile/" + verificationLink + "</a></p>"
                    + "<p>If you did not request this registration, please ignore this email.</p>"
                    + "<p>Thank you,</p>"
                    + "<p>AgileFlow Team</p>";

        }
        else{
            message.setSubject("Reset Password");

            emailContent = "<p>Dear user</p>"

                    + "<p>To reset your password, please click on the link below:</p>"
                    + "<p><a href='http://localhost:5173/resetPassAfter/" + verificationLink + "'>http://localhost:5173/resetPassAfter/" + verificationLink + "</a></p>"
                    + "<p>Thank you,</p>"
                    + "<p>AgileFlow Team</p>";
        }
        message.setContent(emailContent, "text/html");

        Transport.send(message);
    }
}
