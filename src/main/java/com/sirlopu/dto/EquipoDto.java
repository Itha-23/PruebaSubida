package com.sirlopu.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipoDto {
    private int id;
    private String nombre;
    private String ciudad;
    private String fechaCreacion;
    private int numeroJugadores;
}
