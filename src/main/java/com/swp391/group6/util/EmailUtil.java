package com.swp391.group6.util;

import java.util.Properties;
import jakarta.mail.internet.*;
import jakarta.mail.*;

/**
 * Email Utility - Gửi email qua Mailtrap
 *
 * QUAN TRỌNG: Đổi port 2525 → 587 để tránh firewall
 */
public class EmailUtil {

    private static final String SMTP_HOST = "sandbox.smtp.mailtrap.io";
    private static final String SMTP_PORT = "587";  // ✅ Đổi từ 2525 → 587
    private static final String SMTP_USERNAME = "214f27c6290ca9";
    private static final String SMTP_PASSWORD = "d7ce509ff3c094";
    private static final String FROM_EMAIL = "admin@swp391.com";

    /**
     * Gửi email password mới (APPROVE)
     */
    public static boolean sendPasswordResetEmail(String toEmail, String fullName, String newPassword) {
        System.out.println("=== Sending APPROVE Email ===");
        System.out.println("To: " + toEmail);

        try {
            // Cấu hình SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", "*");

            // Tạo session
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
            String htmlContent = buildApproveEmailContent(fullName, newPassword);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);

            System.out.println("✅ Email sent successfully to: " + toEmail);

            // Log vào file
            logToFile("APPROVED", toEmail, fullName, newPassword, null);

            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Gửi email từ chối (REJECT)
     */
    public static boolean sendPasswordRejectEmail(String toEmail, String fullName, String reason) {
        System.out.println("=== Sending REJECT Email ===");
        System.out.println("To: " + toEmail);
        System.out.println("Reason: " + reason);

        try {
            // Cấu hình SMTP
            Properties props = new Properties();
            props.put("mail.smtp.host", SMTP_HOST);
            props.put("mail.smtp.port", SMTP_PORT);
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.ssl.protocols", "TLSv1.2");
            props.put("mail.smtp.ssl.trust", "*");

            // Tạo session
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
            message.setSubject("Yêu cầu đặt lại mật khẩu bị từ chối - SWP391 System");

            // Nội dung email (HTML)
            String htmlContent = buildRejectEmailContent(fullName, reason);
            message.setContent(htmlContent, "text/html; charset=UTF-8");

            // Gửi email
            Transport.send(message);

            System.out.println("✅ Reject email sent successfully to: " + toEmail);

            // Log vào file
            logToFile("REJECTED", toEmail, fullName, null, reason);

            return true;

        } catch (MessagingException e) {
            System.err.println("❌ Failed to send reject email: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Build HTML content cho email APPROVE
     */
    private static String buildApproveEmailContent(String fullName, String newPassword) {
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

    /**
     * Build HTML content cho email REJECT
     */
    private static String buildRejectEmailContent(String fullName, String reason) {
        return "<!DOCTYPE html>" +
                "<html>" +
                "<head><meta charset='UTF-8'></head>" +
                "<body style='font-family: Arial, sans-serif; padding: 20px;'>" +
                "  <h2 style='color: #333;'>Xin chào " + fullName + ",</h2>" +
                "  <p>Rất tiếc, yêu cầu đặt lại mật khẩu của bạn đã bị <strong style='color: #dc3545;'>từ chối</strong>.</p>" +
                "  <div style='background: #ffe6e6; border-left: 4px solid #dc3545; padding: 15px; margin: 20px 0;'>" +
                "    <strong style='color: #dc3545;'>Lý do từ chối:</strong><br/>" +
                "    <p style='margin: 10px 0 0 0; color: #333;'>" + reason + "</p>" +
                "  </div>" +
                "  <p>Nếu bạn cho rằng đây là nhầm lẫn, vui lòng liên hệ bộ phận hỗ trợ:</p>" +
                "  <ul>" +
                "    <li>Email: support@swp391.com</li>" +
                "    <li>Hotline: 0901234567</li>" +
                "  </ul>" +
                "  <hr style='margin: 30px 0;'>" +
                "  <p style='color: #888; font-size: 12px;'>Email này được gửi tự động từ hệ thống SWP391 Group 6.</p>" +
                "</body>" +
                "</html>";
    }

    /**
     * Log vào file
     */
    private static void logToFile(String type, String toEmail, String fullName, String password, String reason) {
        try {
            java.io.FileWriter fw = new java.io.FileWriter("password_reset_log.txt", true);
            fw.write("=========================================\n");
            fw.write("Date: " + new java.util.Date() + "\n");
            fw.write("Type: " + type + "\n");
            fw.write("Email: " + toEmail + "\n");
            fw.write("Name: " + fullName + "\n");
            if (password != null) {
                fw.write("New Password: " + password + "\n");
            }
            if (reason != null) {
                fw.write("Reason: " + reason + "\n");
            }
            fw.write("=========================================\n\n");
            fw.close();
            System.out.println("✅ Logged to: password_reset_log.txt");
        } catch (Exception e) {
            System.err.println("⚠️ Could not save to file: " + e.getMessage());
        }
    }

    /**
     * Test method
     */
    public static void main(String[] args) {
        System.out.println("=== Testing Email Utility ===\n");

        // Test 1: Approve email
        System.out.println("1. Testing APPROVE email:");
        boolean approveResult = sendPasswordResetEmail(
                "test@example.com",
                "Test User",
                "NewPass123"
        );
        System.out.println(approveResult ? "✅ Approve email OK\n" : "❌ Approve email FAILED\n");

        // Test 2: Reject email
        System.out.println("2. Testing REJECT email:");
        boolean rejectResult = sendPasswordRejectEmail(
                "test@example.com",
                "Test User",
                "Không xác minh được danh tính"
        );
        System.out.println(rejectResult ? "✅ Reject email OK\n" : "❌ Reject email FAILED\n");

        System.out.println("=== Test Complete ===");
        System.out.println("Check Mailtrap inbox: https://mailtrap.io/inboxes");
    }
}