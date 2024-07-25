package org.java.javamailjaf.service;
import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.AllArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@AllArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    // Envoi d'un e-mail simple
    public void sendSimpleEmail(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail HTML
    public void sendHtmlEmail(String to, String subject, String htmlContent) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);
            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail avec une pièce jointe
    public void sendEmailWithAttachment(String to, String subject, String text, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            helper.addAttachment(file.getOriginalFilename(), file);

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail avec une image inline
    public void sendEmailWithInlineImage(String to, String subject, String htmlContent, MultipartFile inlineImage, String contentId) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            InputStreamResource resource = new InputStreamResource(inlineImage.getInputStream());
            helper.addInline(contentId, resource, inlineImage.getContentType());

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail avec une pièce jointe JAF
    public void sendEmailWithJafAttachment(String to, String subject, String text, MultipartFile file) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            MimeBodyPart messageBodyPart = new MimeBodyPart();
            InputStream inputStream = file.getInputStream();
            DataSource source = new InputStreamDataSource(inputStream, file.getContentType());
            messageBodyPart.setDataHandler(new DataHandler(source));
            messageBodyPart.setFileName(file.getOriginalFilename());

            MimeMultipart multipart = new MimeMultipart();
            multipart.addBodyPart(messageBodyPart);

            message.setContent(multipart);

            mailSender.send(message);
        } catch (IOException | MessagingException e) {
            System.err.println("Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail avec plusieurs pièces jointes
    public void sendEmailWithMultipleAttachments(String to, String subject, String text, List<MultipartFile> files) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            for (MultipartFile file : files) {
                helper.addAttachment(file.getOriginalFilename(), file);
            }

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Envoi d'un e-mail avec des en-têtes personnalisés
    public void sendEmailWithCustomHeaders(String to, String subject, String text) {
        try {
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, false);

            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(text);

            message.addHeader("X-Custom-Header", "CustomHeaderValue");

            mailSender.send(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static class InputStreamDataSource implements DataSource {

        private final ByteArrayInputStream inputStream;
        private final String contentType;

        public InputStreamDataSource(InputStream inputStream, String contentType) throws IOException {
            this.inputStream = new ByteArrayInputStream(inputStream.readAllBytes());
            this.contentType = contentType;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public OutputStream getOutputStream() throws IOException {
            throw new UnsupportedOperationException("Output stream not supported.");
        }

        @Override
        public String getContentType() {
            return contentType;
        }

        @Override
        public String getName() {
            return "InputStreamDataSource";
        }
    }
}
