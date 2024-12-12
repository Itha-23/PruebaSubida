package com.sirlopu.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import com.sirlopu.entity.Equipo;

public interface EquipoService {

    List<Equipo> findAll(Pageable page);

    List<Equipo> findAll();

    List<Equipo> findByNombre(String nombre, Pageable page);

    Equipo findById(int id);

    Equipo save(Equipo equipo);

    void delete(int id);
}