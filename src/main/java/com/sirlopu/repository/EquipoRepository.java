package com.sirlopu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.sirlopu.entity.Equipo;

import java.util.List;

@Repository
public interface EquipoRepository extends JpaRepository<Equipo, Integer> {
    List<Equipo> findByNombreContaining(String nombre, org.springframework.data.domain.Pageable pageable);
}
