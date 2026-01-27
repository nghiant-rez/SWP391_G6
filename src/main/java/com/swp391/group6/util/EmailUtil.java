package com.swp391.group6.util;

import java.util.Properties;
import jakarta.mail.internet.*;
import jakarta.mail.*;

/**
*Email Utility - Gửi email qua Mailtrap (fake SMTP server)
*Cách setup Mailtrap:
*1. Đăng ký tài khoản miễn phí tại: https://mailtrap.io/
*2. Vào Email Testing → Inboxes → My Inbox → Show Credentials
*3. Copy Username và Password vào đây
**/
public class EmailUtil {
    //chuyen sang chay JDK21.
    private static final String SMTP_HOST = "sandbox.smtp.mailtrap.io";
    private static final String SMTP_PORT = "587";
    private static final String SMTP_USERNAME = "214f27c6290ca9";//chỉnh lại username trong sanbox của mailtrail
    private static final String SMTP_PASSWORD = "d7ce509ff3c094";////chỉnh lại username trong sanbox của mailtrail
    private static final String FROM_EMAIL = "admin@gmail.com";

    //send email with new password
    public static boolean sendPasswordResetEmail(String toEmail, String fullName, String newPassword) {
        try {
            //cau hinh SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");

            // Tao session
            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USERNAME, SMTP_PASSWORD);
                }
            });

            // Tạo message
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(FROM_EMAIL));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Mật khẩu mới của bạn - SWP391 System");

            // Nội dung email (HTML)
            String htmlContent = buildEmailContent(fullName, newPassword);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);

            System.out.println("✅ Email sent successfully to: " + toEmail);
            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    //Build HTML content of email
    private static String buildEmailContent(String fullName, String newPassword) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "  <h2 style='color: #333;'>Xin chào " + fullName + ",</h2>" +
                "  <p>Yêu cầu đặt lại mật khẩu của bạn đã được <strong>chấp nhận</strong>.</p>" +
                "  <p>Mật khẩu mới của bạn là:</p>" +
                "  <div style='background: #f4f4f4; padding: 15px; border-radius: 5px; font-size: 18px; font-weight: bold; color: #d9534f;'>" +
                "    " + newPassword +
                "  </div>" +
                "  <p style='color: #d9534f;'><strong>⚠️ Lưu ý:</strong> Vui lòng đổi mật khẩu sau khi đăng nhập.</p>" +
                "  <hr style='margin: 30px 0;'>" +
                "  <p style='color: #888; font-size: 12px;'>Email này được gửi tự động từ hệ thống SWP391 Group 6.</p>" +
                "</body>" +
                "</html>";
    }

    //Test method - run to check email
    public static void main(String[] args) {
        System.out.println("Testing email...");
        boolean result = sendPasswordResetEmail(
                "test@example.com",
                "Test User",
                "NewPass123"
        );

        if (result) {
            System.out.println("✅ Email sent! Check Mailtrap inbox.");
        } else {
            System.out.println("❌ Email failed. Check SMTP credentials.");
        }
    }
}