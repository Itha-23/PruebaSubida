package com.sirlopu.validator;

import com.sirlopu.entity.Carrera;
import com.sirlopu.exception.ValidateException;

public class CarreraValidator {
    public static void validate(Carrera carrera) {
        if (carrera.getNombre() == null || carrera.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre de la carrera es requerido");
        }
        if (carrera.getNombre().length() > 100) {
            throw new ValidateException("El nombre de la carrera no debe tener más de 100 caracteres");
        }
    }
}
