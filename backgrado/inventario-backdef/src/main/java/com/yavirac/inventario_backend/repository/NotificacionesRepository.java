package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Notificaciones;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface NotificacionesRepository extends JpaRepository<Notificaciones, Long> {
    List<Notificaciones> findByLeidaFalse();
    List<Notificaciones> findByTipoNotificacion(String tipoNotificacion);
    Optional<Notificaciones> findByTipoNotificacionAndIdReporte(String tipoNotificacion, Long idReporte);
    List<Notificaciones> findByIdReporte(Long idReporte);
}

