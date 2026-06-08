package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.repository.NotificacionesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class NotificacionesService {
    
    @Autowired
    private NotificacionesRepository notificacionesRepository;
    
    public List<Notificaciones> findAll() {
        return notificacionesRepository.findAll();
    }
    
    public List<Notificaciones> findNoLeidas() {
        return notificacionesRepository.findByLeidaFalse();
    }
    
    public List<Notificaciones> findByTipo(String tipoNotificacion) {
        return notificacionesRepository.findByTipoNotificacion(tipoNotificacion);
    }
    
    public Optional<Notificaciones> findById(Long id) {
        return notificacionesRepository.findById(id);
    }
    
    public Notificaciones save(Notificaciones notificacion) {
        return notificacionesRepository.save(notificacion);
    }
    
    public Notificaciones update(Long id, Notificaciones notificacion) {
        Notificaciones existingNotificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        
        // Actualizar solo los campos que existen ahora
        if (notificacion.getTipoNotificacion() != null) existingNotificacion.setTipoNotificacion(notificacion.getTipoNotificacion());
        if (notificacion.getIdReporte() != null) existingNotificacion.setIdReporte(notificacion.getIdReporte());
        if (notificacion.getEstado() != null) existingNotificacion.setEstado(notificacion.getEstado());
        if (notificacion.getTipoIncidencia() != null) existingNotificacion.setTipoIncidencia(notificacion.getTipoIncidencia());
        if (notificacion.getDetalleProblema() != null) existingNotificacion.setDetalleProblema(notificacion.getDetalleProblema());
        if (notificacion.getElementoAfectado() != null) existingNotificacion.setElementoAfectado(notificacion.getElementoAfectado());
        if (notificacion.getReportadoPor() != null) existingNotificacion.setReportadoPor(notificacion.getReportadoPor());
        if (notificacion.getFechaHoraReporte() != null) existingNotificacion.setFechaHoraReporte(notificacion.getFechaHoraReporte());
        if (notificacion.getLeida() != null) existingNotificacion.setLeida(notificacion.getLeida());
        
        return notificacionesRepository.save(existingNotificacion);
    }
    
    public Notificaciones marcarComoLeida(Long id) {
        Notificaciones notificacion = notificacionesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Notificación no encontrada"));
        notificacion.setLeida(true);
        return notificacionesRepository.save(notificacion);
    }
    
    public void deleteById(Long id) {
        if (!notificacionesRepository.existsById(id)) {
            throw new RuntimeException("Notificación no encontrada");
        }
        notificacionesRepository.deleteById(id);
    }
}

