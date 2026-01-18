package com.lucasteixeira.bank.exceptions.dto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorResponseDTO {


    private LocalDateTime timestamp;
    private int Status;
    private String message;
    private String path;
    private String error;
}
