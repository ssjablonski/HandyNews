package com.seba.handy_news.auth;

import com.seba.handy_news.user.Address;
import com.seba.handy_news.user.Role;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String dateOfBirth;
    private String phoneNumber;
    private Address address;
    private Role role;
}
