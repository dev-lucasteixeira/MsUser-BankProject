package com.lucasteixeira.bank.dtos;

import com.lucasteixeira.bank.constraints.annotations.Cpf;
import com.lucasteixeira.bank.enums.AccessEnum;
import com.lucasteixeira.bank.enums.ActivityEnum;
import com.lucasteixeira.bank.enums.MaritalStatusEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {

    private UUID id;

    @NotNull(message = "O CPF é obrigatório")
    @Cpf(message = "O formato do CPF é inválido")
    private String cpf;

    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @NotNull
    @Email(message = "Insira um email válido")
    @Size(min = 3, max = 255)
    private String email;

    @NotNull
    @Size(min = 3, max = 255)
    private String password;

    @NotNull
    private Integer age;

    @NotNull
    private MaritalStatusEnum maritalStatus;

    @NotNull
    private AccessEnum access;

    @NotNull
    private ActivityEnum active;


}
