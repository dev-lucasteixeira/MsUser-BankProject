package com.lucasteixeira.bank.entities;


import com.lucasteixeira.bank.enums.AccessEnum;
import com.lucasteixeira.bank.enums.ActivityEnum;
import com.lucasteixeira.bank.enums.MaritalStatusEnum;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.UUID;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "tb_user")
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    // Use 'length' para definir o tamanho no banco (VARCHAR)
    @Column(name = "Cpf", unique = true, nullable = false, length = 11)
    @NotNull
    @Size(min = 11, max = 11)
    private String cpf;

    @Column(name = "Name", nullable = false, length = 255)
    @NotNull
    @Size(min = 3, max = 255)
    private String name;

    @Column(name = "Email", unique = true, nullable = false, length = 255)
    @NotNull
    @Size(min = 5, max = 255)
    private String email;

    @Column(name = "Password", nullable = false)
    @NotNull
    private String password;

    @Column(name = "Age", nullable = false)
    @NotNull
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(name = "Marital_Status", nullable = false)
    @NotNull
    private MaritalStatusEnum maritalStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "Access", nullable = false)
    @NotNull
    private AccessEnum access;

    @Enumerated(EnumType.STRING)
    @Column(name = "Active", nullable = false)
    @NotNull
    private ActivityEnum active;
}
