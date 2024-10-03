package com.notification.service.service;

import com.notification.service.dto.request.EmailRequest;
import com.notification.service.dto.request.SendEmailRequest;
import com.notification.service.dto.request.Sender;
import com.notification.service.dto.response.EmailResponse;
import com.notification.service.exception.AppException;
import com.notification.service.exception.ErrorCode;
import com.notification.service.repository.EmailClient;
import feign.FeignException;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class EmailService {
    EmailClient emailClient;


    public EmailResponse sendEmail(SendEmailRequest request) {
        EmailRequest emailRequest = EmailRequest.builder()
                .sender(Sender.builder()
                        .name("Duy Coder")
                        .email("duynv.it.052@gmail.com")
                        .build())
                .to(List.of(request.getTo()))
                .subject(request.getSubject())
                .htmlContent(request.getHtmlContent())
                .build();
        try {
            return emailClient.sendEmail(Your-Key, emailRequest);
        } catch (FeignException.FeignClientException e) {
            throw new AppException(ErrorCode.CANNOT_SEND_EMAIL);
        }
    }
}
