package com.aiintegration.aiintegrationrecap.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDTO {
    private Long id;
    @NotBlank(message = "Username cant be blank")
    private String userName;
    @NotBlank(message = "Password cant be blank")
    private String password;
    @Email(message = "email Cant be Blank!!")
    private String email;
}
