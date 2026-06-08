package com.yavirac.inventario_backend.repository;

import java.util.List;

import com.yavirac.inventario_backend.entity.Asignacion;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AsignacionRepository extends JpaRepository<Asignacion, Long> {
    List<Asignacion> findByUsuarioIdUsuarioAndEstadoTrue(Long idUsuario);
    
    @org.springframework.data.jpa.repository.Query("SELECT a FROM Asignacion a JOIN FETCH a.aula JOIN FETCH a.usuario WHERE a.usuario.idUsuario = :idUsuario AND a.estado = true")
    List<Asignacion> findByUsuarioIdUsuarioAndEstadoTrueWithRelations(@org.springframework.data.repository.query.Param("idUsuario") Long idUsuario);
}

