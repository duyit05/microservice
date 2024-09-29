package com.microservices.profileuser.dto.request;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Builder
public class UserProfileRequest {
    String userId;
    String firstName;
    String lastName;
    LocalDate dob;
    String city;
}
