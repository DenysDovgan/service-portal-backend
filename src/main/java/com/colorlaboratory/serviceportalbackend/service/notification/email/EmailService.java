package com.colorlaboratory.serviceportalbackend.service.notification.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    @Value("${spring.mail.username}")
    private String noreplyEmail;
    private final JavaMailSender mailSender;

    public void sendHtmlEmail(String to, String subject, String templateName, Map<String, String> placeholders) throws MessagingException {
        String htmlContent = null;
        try {
            htmlContent = loadHtmlTemplate(templateName, placeholders);
        } catch (IOException e) {
            log.error("Failed to load email HTML template {}", templateName, e);
            throw new RuntimeException(e);
        }

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(htmlContent, true);
        helper.setFrom(noreplyEmail);

        Resource logoResource = new ClassPathResource("static/logo.png");
        helper.addInline("logo", logoResource);

        mailSender.send(message);
    }

    private String loadHtmlTemplate(String templateName, Map<String, String> placeholders) throws IOException {
        ClassPathResource resource = new ClassPathResource("email-templates/" + templateName);
        String content = Files.readString(resource.getFile().toPath());

        for (Map.Entry<String, String> entry : placeholders.entrySet()) {
            content = content.replace("{{" + entry.getKey() + "}}", entry.getValue());
        }

        return content;
    }
}
