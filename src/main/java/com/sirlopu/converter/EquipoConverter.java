package com.sirlopu.converter;

import org.springframework.stereotype.Component;

import com.sirlopu.dto.EquipoDto;
import com.sirlopu.entity.Equipo;

@Component
public class EquipoConverter extends AbstractConverter<Equipo, EquipoDto> {

    @Override
    public EquipoDto fromEntity(Equipo entity) {
        if (entity == null) return null;
        return EquipoDto.builder()
                .id(entity.getId())
                .nombre(entity.getNombre())
                .ciudad(entity.getCiudad())
                .fechaCreacion(entity.getFechaCreacion() != null ? entity.getFechaCreacion().toString() : null)
                .numeroJugadores(entity.getNumeroJugadores())
                .build();
    }

    @Override
    public Equipo fromDTO(EquipoDto dto) {
        if (dto == null) return null;
        return Equipo.builder()
                .id(dto.getId())
                .nombre(dto.getNombre())
                .ciudad(dto.getCiudad())
                .fechaCreacion(dto.getFechaCreacion() != null ? java.sql.Date.valueOf(dto.getFechaCreacion()) : null)
                .numeroJugadores(dto.getNumeroJugadores())
                .build();
    }
}
