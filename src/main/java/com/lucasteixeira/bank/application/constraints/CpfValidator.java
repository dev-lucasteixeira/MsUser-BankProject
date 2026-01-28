package com.lucasteixeira.bank.application.constraints;

import com.lucasteixeira.bank.application.constraints.annotations.Cpf;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String cpf, ConstraintValidatorContext context) {
        if (cpf == null) {
            return true;
        }

        String cpfLimpo = cpf.replaceAll("\\D", "");

        if (cpfLimpo.length() != 11 || isAllDigitsEqual(cpfLimpo)) {
            return false;
        }

        return calcularDigitos(cpfLimpo);
    }

    private boolean isAllDigitsEqual(String cpf) {
        char firstDigit = cpf.charAt(0);
        for (int i = 1; i < cpf.length(); i++) {
            if (cpf.charAt(i) != firstDigit) {
                return false;
            }
        }
        return true;
    }

    private boolean calcularDigitos(String cpf) {
        try {
            int soma = 0;
            int peso = 10;
            for (int i = 0; i < 9; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }
            int resto = soma % 11;
            int d1 = (resto < 2) ? 0 : 11 - resto;

            soma = 0;
            peso = 11;
            for (int i = 0; i < 10; i++) {
                soma += Character.getNumericValue(cpf.charAt(i)) * peso--;
            }
            resto = soma % 11;
            int d2 = (resto < 2) ? 0 : 11 - resto;

            return d1 == Character.getNumericValue(cpf.charAt(9)) &&
                    d2 == Character.getNumericValue(cpf.charAt(10));

        } catch (Exception e) {
            return false;
        }
    }
}
