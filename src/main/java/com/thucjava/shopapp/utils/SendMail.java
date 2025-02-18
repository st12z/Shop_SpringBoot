package com.thucjava.shopapp.utils;

import com.thucjava.shopapp.config.MailConfig;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.UnsupportedEncodingException;


@Slf4j
@RequiredArgsConstructor
@Component
public class SendMail {
    private final JavaMailSender mailSender;
    private final MailConfig mailConfig;
    public void sendMail(String email,String content,String subject) throws MessagingException, UnsupportedEncodingException {
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(mailConfig.getUsername(),"shopping");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);

    }
    @KafkaListener(topics = "confirm-email-topic",groupId = "confirm-email-group",containerFactory = "kafkaListenerContainerFactory")
    public void sendEmailUseKafka(String message) throws MessagingException, UnsupportedEncodingException {
        log.info("send email using kafka topic...");
        String[] arr=message.split(",");
        String email=arr[0];
        String content=arr[1];
        String subject=arr[2];
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage);
        helper.setFrom(mailConfig.getUsername(),"shopping");
        helper.setTo(email);
        helper.setSubject(subject);
        helper.setText(content, true);
        mailSender.send(mimeMessage);

    }

}
