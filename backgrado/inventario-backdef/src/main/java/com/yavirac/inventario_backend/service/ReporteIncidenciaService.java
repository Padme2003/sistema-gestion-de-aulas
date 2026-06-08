package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.dto.ReporteIncidenciaRequest;
import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.BienesRepository;
import com.yavirac.inventario_backend.repository.NotificacionesRepository;
import com.yavirac.inventario_backend.repository.ReporteIncidenciaRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ReporteIncidenciaService {
    
    @Autowired
    private ReporteIncidenciaRepository reporteIncidenciaRepository;
    
    @Autowired
    private BienesRepository bienesRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private NotificacionesRepository notificacionesRepository;
    
    public List<ReporteIncidencia> findAll() {
        return reporteIncidenciaRepository.findAll();
    }
    
    public Optional<ReporteIncidencia> findById(Long id) {
        return reporteIncidenciaRepository.findById(id);
    }
    
    public List<ReporteIncidencia> findByUsuario(Long idUsuario) {
        return reporteIncidenciaRepository.findByUsuarioIdUsuario(idUsuario);
    }
    
    public List<ReporteIncidencia> findByBien(Long idBien) {
        return reporteIncidenciaRepository.findByBienIdBien(idBien);
    }
    
    public List<ReporteIncidencia> findByEstado(String estado) {
        return reporteIncidenciaRepository.findByEstado(estado);
    }
    
    public List<ReporteIncidencia> findByTipoIncidencia(String tipoIncidencia) {
        return reporteIncidenciaRepository.findByTipoIncidencia(tipoIncidencia);
    }
    
    @Transactional
    public ReporteIncidencia save(ReporteIncidenciaRequest request) {
        System.out.println("🔧 ReporteIncidenciaService.save() - Iniciando guardado...");
        
        if (request.getIdBien() == null) {
            throw new RuntimeException("El idBien es requerido");
        }
        if (request.getIdUsuario() == null) {
            throw new RuntimeException("El idUsuario es requerido");
        }
        if (request.getTipoIncidencia() == null || request.getTipoIncidencia().isEmpty()) {
            throw new RuntimeException("El tipoIncidencia es requerido");
        }
        if (request.getDetalleProblema() == null || request.getDetalleProblema().isEmpty()) {
            throw new RuntimeException("El detalleProblema es requerido");
        }
        
        System.out.println("   Buscando bien con ID: " + request.getIdBien());
        Bienes bien = bienesRepository.findById(request.getIdBien())
                .orElseThrow(() -> new RuntimeException("Bien no encontrado con ID: " + request.getIdBien()));
        System.out.println("   ✅ Bien encontrado: " + bien.getCodigoBien());
        
        System.out.println("   Buscando usuario con ID: " + request.getIdUsuario());
        Usuario usuario = usuarioRepository.findById(request.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + request.getIdUsuario()));
        System.out.println("   ✅ Usuario encontrado: " + usuario.getCedula());
        
        ReporteIncidencia reporte = new ReporteIncidencia();
        reporte.setBien(bien);
        reporte.setUsuario(usuario);
        reporte.setEstado(request.getEstado() != null ? request.getEstado() : "Pendiente");
        reporte.setTipoIncidencia(request.getTipoIncidencia());
        reporte.setDetalleProblema(request.getDetalleProblema());
        
        System.out.println("   Guardando reporte en base de datos...");
        ReporteIncidencia reporteGuardado = reporteIncidenciaRepository.save(reporte);
        System.out.println("   ✅ Reporte guardado exitosamente con ID: " + reporteGuardado.getIdReporte());
        
        // Crear notificación automáticamente cuando se crea un reporte
        System.out.println("   Creando notificación para el reporte...");
        Notificaciones notificacion = new Notificaciones();
        notificacion.setTipoNotificacion("REPORTE_INCIDENCIA");
        notificacion.setIdReporte(reporteGuardado.getIdReporte());
        notificacion.setEstado(reporteGuardado.getEstado());
        notificacion.setTipoIncidencia(reporteGuardado.getTipoIncidencia());
        notificacion.setDetalleProblema(reporteGuardado.getDetalleProblema());
        notificacion.setFechaHoraReporte(reporteGuardado.getFechaHora());
        
        // Obtener nombre del elemento afectado
        String elementoAfectado = bien.getCodigoBien() != null && !bien.getCodigoBien().isEmpty() 
            ? bien.getCodigoBien() 
            : (bien.getNombreBien() != null && !bien.getNombreBien().isEmpty() 
                ? bien.getNombreBien() 
                : bien.getClaseBien());
        notificacion.setElementoAfectado(elementoAfectado != null ? elementoAfectado : "Bien #" + bien.getIdBien());
        
        // Obtener nombre del usuario que reporta
        String reportadoPor = usuario.getNombresCompletos() != null && !usuario.getNombresCompletos().isEmpty() 
            ? usuario.getNombresCompletos() 
            : usuario.getCedula();
        notificacion.setReportadoPor(reportadoPor != null ? reportadoPor : "Usuario #" + usuario.getIdUsuario());
        
        notificacion.setLeida(false);
        
        Notificaciones notificacionGuardada = notificacionesRepository.save(notificacion);
        System.out.println("   ✅ Notificación guardada exitosamente con ID: " + notificacionGuardada.getIdNotificacion());
        
        return reporteGuardado;
    }
    
    @Transactional
    public ReporteIncidencia update(Long id, ReporteIncidenciaRequest request) {
        ReporteIncidencia reporte = reporteIncidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        
        Bienes bienActual = reporte.getBien();
        Usuario usuarioActual = reporte.getUsuario();
        
        if (request.getIdBien() != null && !request.getIdBien().equals(reporte.getBien().getIdBien())) {
            bienActual = bienesRepository.findById(request.getIdBien())
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
            reporte.setBien(bienActual);
        }
        
        if (request.getIdUsuario() != null && !request.getIdUsuario().equals(reporte.getUsuario().getIdUsuario())) {
            usuarioActual = usuarioRepository.findById(request.getIdUsuario())
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            reporte.setUsuario(usuarioActual);
        }
        
        if (request.getEstado() != null) {
            reporte.setEstado(request.getEstado());
        }
        
        if (request.getTipoIncidencia() != null) {
            reporte.setTipoIncidencia(request.getTipoIncidencia());
        }
        
        if (request.getDetalleProblema() != null) {
            reporte.setDetalleProblema(request.getDetalleProblema());
        }
        
        ReporteIncidencia reporteActualizado = reporteIncidenciaRepository.save(reporte);
        
        // Usar variables finales para la lambda
        final Bienes bienFinal = bienActual;
        final Usuario usuarioFinal = usuarioActual;
        
        // Actualizar también la notificación relacionada
        notificacionesRepository.findByTipoNotificacionAndIdReporte("REPORTE_INCIDENCIA", id)
                .ifPresent(notificacion -> {
                    notificacion.setEstado(reporteActualizado.getEstado());
                    notificacion.setTipoIncidencia(reporteActualizado.getTipoIncidencia());
                    notificacion.setDetalleProblema(reporteActualizado.getDetalleProblema());
                    
                    // Actualizar elemento afectado si cambió el bien
                    String elementoAfectado = bienFinal.getCodigoBien() != null && !bienFinal.getCodigoBien().isEmpty() 
                        ? bienFinal.getCodigoBien() 
                        : (bienFinal.getNombreBien() != null && !bienFinal.getNombreBien().isEmpty() 
                            ? bienFinal.getNombreBien() 
                            : bienFinal.getClaseBien());
                    notificacion.setElementoAfectado(elementoAfectado != null ? elementoAfectado : "Bien #" + bienFinal.getIdBien());
                    
                    // Actualizar reportado por si cambió el usuario
                    String reportadoPor = usuarioFinal.getNombresCompletos() != null && !usuarioFinal.getNombresCompletos().isEmpty() 
                        ? usuarioFinal.getNombresCompletos() 
                        : usuarioFinal.getCedula();
                    notificacion.setReportadoPor(reportadoPor != null ? reportadoPor : "Usuario #" + usuarioFinal.getIdUsuario());
                    
                    notificacionesRepository.save(notificacion);
                });
        
        return reporteActualizado;
    }
    
    @Transactional
    public ReporteIncidencia cambiarEstado(Long id, String nuevoEstado) {
        ReporteIncidencia reporte = reporteIncidenciaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reporte no encontrado"));
        reporte.setEstado(nuevoEstado);
        ReporteIncidencia reporteActualizado = reporteIncidenciaRepository.save(reporte);
        
        // Actualizar también la notificación relacionada
        notificacionesRepository.findByTipoNotificacionAndIdReporte("REPORTE_INCIDENCIA", id)
                .ifPresent(notificacion -> {
                    notificacion.setEstado(nuevoEstado);
                    notificacionesRepository.save(notificacion);
                });
        
        return reporteActualizado;
    }
    
    public void deleteById(Long id) {
        if (!reporteIncidenciaRepository.existsById(id)) {
            throw new RuntimeException("Reporte no encontrado");
        }
        reporteIncidenciaRepository.deleteById(id);
    }
}

