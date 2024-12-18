package com.ronanvcjunior.taskmaster.enums.converters;

import com.ronanvcjunior.taskmaster.enums.Authority;
import com.ronanvcjunior.taskmaster.exceptions.ApiException;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.stream.Stream;

@Converter(autoApply = true)
public class RoleConverter implements AttributeConverter<Authority, String> {

    @Override
    public String convertToDatabaseColumn(Authority authority) {
        if(authority == null) {
            return null;
        }

        return authority.getValue();
    }

    @Override
    public Authority convertToEntityAttribute(String code) {
        if(code == null) {
            return null;
        }

        return Stream.of(Authority.values())
                .filter(authority -> authority.getValue().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() ->
                        new ApiException("Código de autoridade inválido: " + code + " encontrado no banco de dados"));
    }
}
