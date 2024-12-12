package com.sirlopu.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sirlopu.validator.EquipoValidator;
import com.sirlopu.entity.Equipo;
import com.sirlopu.repository.EquipoRepository;
import com.sirlopu.service.EquipoService;

@Service
public class EquipoServiceImpl implements EquipoService {

    @Autowired
    private EquipoRepository equipoRepository;

    @Override
    public List<Equipo> findAll(Pageable page) {
        return equipoRepository.findAll(page).getContent();
    }

    @Override
    public List<Equipo> findAll() {
        return equipoRepository.findAll();
    }

    @Override
    public List<Equipo> findByNombre(String nombre, Pageable page) {
        return equipoRepository.findByNombreContaining(nombre, page);
    }

    @Override
    public Equipo findById(int id) {
        return equipoRepository.findById(id).orElse(null);
    }

    @Override
    public Equipo save(Equipo equipo) {
    	if (equipo.getNombre() == null || equipo.getNombre().trim().isEmpty()) {
            EquipoValidator.validate(equipo);
        }
        return equipoRepository.save(equipo);
    }

    @Override
    public void delete(int id) {
        equipoRepository.deleteById(id);
    }
}
