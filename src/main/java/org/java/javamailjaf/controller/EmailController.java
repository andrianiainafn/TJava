package org.java.javamailjaf.controller;

import lombok.AllArgsConstructor;
import org.java.javamailjaf.service.EmailService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@RestController
@AllArgsConstructor
@RequestMapping("api")
public class EmailController {

    private final EmailService emailService;

    @PostMapping("/send-simple-email")
    public String sendSimpleEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        emailService.sendSimpleEmail(to, subject, text);
        return "Simple Email sent successfully";
    }

    @PostMapping("/send-html-email")
    public String sendHtmlEmail(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String htmlContent) {
        emailService.sendHtmlEmail(to, subject, htmlContent);
        return "HTML Email sent successfully";
    }

    @PostMapping("/send-email-with-attachment")
    public String sendEmailWithAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            @RequestParam("file") MultipartFile file) {
        emailService.sendEmailWithAttachment(to, subject, text, file);
        return "Email with attachment sent successfully";
    }

    @PostMapping("/send-email-with-inline-image")
    public String sendEmailWithInlineImage(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String htmlContent,
            @RequestParam("inlineImage") MultipartFile inlineImage,
            @RequestParam String contentId) {
        emailService.sendEmailWithInlineImage(to, subject, htmlContent, inlineImage, contentId);
        return "Email with inline image sent successfully";
    }

    @PostMapping("/send-email-with-jaf-attachment")
    public String sendEmailWithJafAttachment(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            @RequestParam("file") MultipartFile file) {
        emailService.sendEmailWithJafAttachment(to, subject, text, file);
        return "Email with JAF attachment sent successfully";
    }

    @PostMapping("/send-email-with-multiple-attachments")
    public String sendEmailWithMultipleAttachments(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text,
            @RequestParam("files") List<MultipartFile> files) {
        emailService.sendEmailWithMultipleAttachments(to, subject, text, files);
        return "Email with multiple attachments sent successfully";
    }

    @PostMapping("/send-email-with-custom-headers")
    public String sendEmailWithCustomHeaders(
            @RequestParam String to,
            @RequestParam String subject,
            @RequestParam String text) {
        emailService.sendEmailWithCustomHeaders(to, subject, text);
        return "Email with custom headers sent successfully";
    }

}
