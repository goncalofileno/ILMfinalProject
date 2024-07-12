package com.ilm.projecto_ilm_backend.utilities;

import jakarta.ejb.Stateless;
import jakarta.mail.*;
import jakarta.mail.internet.*;

import java.io.UnsupportedEncodingException;
import java.util.Properties;
/**
 * EmailService is a stateless EJB that provides methods to send confirmation and password reset emails.
 * It uses the JavaMail API to send emails.
 */
@Stateless
public class EmailService {
    /**
     * The username for the email account from which the emails are sent.
     */
    private final String username = "innovationlabmanagementcs@gmail.com";
    /**
     * The password for the email account from which the emails are sent.
     */
    private final String password = "xnog bvud syvq rpcv"; // Sua senha de email
    /**
     * This method creates and returns a mail Session with the necessary properties and authenticator.
     *
     * @return a mail Session with the necessary properties and authenticator
     */
    private Session createSession() {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.debug", "true");
        props.put("mail.smtp.ssl.trust", "*");

        return Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });
    }
    /**
     * This method sends a confirmation email to the provided email address with the provided verification link.
     *
     * @param to the email address to which the confirmation email is to be sent
     * @param verificationLink the verification link to be included in the confirmation email
     * @throws MessagingException if there is a problem with the creation or sending of the message
     * @throws UnsupportedEncodingException if the character encoding is not supported
     */
    public void sendConfirmationEmail(String to, String verificationLink) throws MessagingException, UnsupportedEncodingException {
        Session session = createSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Confirm Your Account Registration");

        String emailContent = "<html>"
                + "<body style='font-family: Arial, sans-serif; line-height: 1.6;'>"
                + "<h2>Welcome to Innovation Lab Management!</h2>"
                + "<p>Thank you for registering with AgileFlow!</p>"
                + "<p>To complete the registration process and gain full access to your account, please click the button below to confirm your email address:</p>"
                + "<p style='text-align: center;'>"
                + "<a href='https://localhost:8443/projeto_ilm_final/rest/user/confirmEmail/" + verificationLink + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #4CAF50; text-decoration: none; border-radius: 5px;'>Confirm Email</a>"
                + "</p>"
                + "<p>If you did not request this registration, please ignore this email.</p>"
                + "<p>Thank you,</p>"
                + "<p>Innovation Lab Management Team</p>"
                + "</body>"
                + "</html>";

        message.setContent(emailContent, "text/html");
        Transport.send(message);
    }


    /**
     * This method sends a password reset email to the provided email address with the provided verification link.
     *
     * @param to the email address to which the password reset email is to be sent
     * @param verificationLink the verification link to be included in the password reset email
     * @throws MessagingException if there is a problem with the creation or sending of the message
     * @throws UnsupportedEncodingException if the character encoding is not supported
     */

    public void sendResetPasswordEmail(String to, String verificationLink) throws MessagingException, UnsupportedEncodingException {
        Session session = createSession();

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(username));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
        message.setSubject("Reset Password");

        String emailContent = "<p>Dear user</p>"
                + "<p>To reset your password, please click on the link below:</p>"
                + "<p style='text-align: center;'>"
                + "<a href='https://localhost:8443/projeto_ilm_final/rest/user/resetPasswordCheck/" + verificationLink + "' style='display: inline-block; padding: 10px 20px; font-size: 16px; color: white; background-color: #4CAF50; text-decoration: none; border-radius: 5px;'>Reset Password here</a>"
                + "</p>"
                + "<p>Thank you,</p>"
                + "<p>Innovation Lab Management Team</p>";

        message.setContent(emailContent, "text/html");
        Transport.send(message);
    }
}
