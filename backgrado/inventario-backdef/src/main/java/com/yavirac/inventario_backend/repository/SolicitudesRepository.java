package com.yavirac.inventario_backend.repository;

import java.util.List;

import com.yavirac.inventario_backend.entity.Solicitudes;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SolicitudesRepository extends JpaRepository<Solicitudes, Long> {
    List<Solicitudes> findByBienIdBienIn(List<Long> idsBienes);
}

