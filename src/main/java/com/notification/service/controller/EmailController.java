package com.notification.service.controller;

import com.notification.service.dto.request.SendEmailRequest;
import com.notification.service.dto.response.ApiRespone;
import com.notification.service.dto.response.EmailResponse;
import com.notification.service.service.EmailService;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@FieldDefaults(level = AccessLevel.PRIVATE , makeFinal = true )
public class EmailController {

    EmailService emailService;

    @PostMapping("/email/send")
    ApiRespone<EmailResponse> sendEmail (@RequestBody SendEmailRequest request){
            return ApiRespone.<EmailResponse>builder()
                    .result(emailService.sendEmail(request))
                    .build();
    }

}
