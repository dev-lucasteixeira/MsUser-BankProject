package com.lucasteixeira.bank.application.constraints;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CpfValidatorTest {

    private final CpfValidator validator = new CpfValidator();

    @Test
    @DisplayName("Deve validar CPF com máscara corretamente")
    void isValid_WithMask() {
        assertTrue(validator.isValid("705.484.450-52", null));
    }

    @Test
    @DisplayName("Deve validar CPF sem máscara corretamente")
    void isValid_WithoutMask() {
        assertTrue(validator.isValid("70548445052", null));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com todos os dígitos iguais")
    void isValid_AllDigitsEqual() {
        assertFalse(validator.isValid("11111111111", null));
    }

    @Test
    @DisplayName("Deve rejeitar CPF com tamanho inválido")
    void isValid_WrongLength() {
        assertFalse(validator.isValid("123", null));
    }
}