package com.multiservice.AppService.services;



import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.apache.commons.lang3.text.StrSubstitutor;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Map;

@Service
public class EmailService {
    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    public String loadEmailTemplate(String templatePath, Map<String, String> values) throws Exception {

        String content = Files.readString(new ClassPathResource(templatePath).getFile().toPath(), StandardCharsets.UTF_8);


        StrSubstitutor substitutor = new StrSubstitutor(values);
        return substitutor.replace(content);
    }

    public void sendEmail(String to, String subject, String content) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(content, true);

        mailSender.send(message);
    }
}
