package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReporteIncidenciaRepository extends JpaRepository<ReporteIncidencia, Long> {
    List<ReporteIncidencia> findByUsuarioIdUsuario(Long idUsuario);
    List<ReporteIncidencia> findByBienIdBien(Long idBien);
    List<ReporteIncidencia> findByEstado(String estado);
    List<ReporteIncidencia> findByTipoIncidencia(String tipoIncidencia);
}

