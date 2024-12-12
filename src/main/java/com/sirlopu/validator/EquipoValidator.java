package com.sirlopu.validator;

import com.sirlopu.entity.Equipo;
import com.sirlopu.exception.ValidateException;

public class EquipoValidator {

    public static void validate(Equipo equipo) {
        if (equipo.getNombre() == null || equipo.getNombre().trim().isEmpty()) {
            throw new ValidateException("El nombre del equipo es requerido");
        }
        if (equipo.getNombre().length() > 100) {
            throw new ValidateException("El nombre del equipo no debe tener más de 100 caracteres");
        }
        if (equipo.getCiudad() == null || equipo.getCiudad().trim().isEmpty()) {
            throw new ValidateException("La ciudad del equipo es requerida");
        }
        if (equipo.getCiudad().length() > 100) {
            throw new ValidateException("La ciudad del equipo no debe tener más de 100 caracteres");
        }
        if (equipo.getFechaCreacion() == null) {
            throw new ValidateException("La fecha de creación del equipo es requerida");
        }
        if (equipo.getNumeroJugadores() <= 0) {
            throw new ValidateException("El número de jugadores debe ser mayor a 0");
        }
    }
}
