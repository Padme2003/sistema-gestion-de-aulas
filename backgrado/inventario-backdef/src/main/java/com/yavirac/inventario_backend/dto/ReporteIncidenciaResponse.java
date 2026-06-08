package com.yavirac.inventario_backend.dto;

import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteIncidenciaResponse {
    private Long idReporte;
    private Long id_reporte; // Para compatibilidad con frontend
    private String estado;
    private String tipoIncidencia;
    private String tipo_incidencia; // Para compatibilidad con frontend
    private String detalleProblema;
    private String detalle_problema; // Para compatibilidad con frontend
    private LocalDateTime fechaHora;
    private String fecha_hora; // Para compatibilidad con frontend (formateado)
    
    // Datos del bien (ELEMENTO AFECTADO)
    private Long idBien;
    private Long id_bien; // Para compatibilidad con frontend
    private String codigoBien;
    private String codigo_bien; // Para compatibilidad con frontend
    private String nombreBien;
    private String nombre_bien; // Para compatibilidad con frontend
    private String claseBien;
    private String clase_bien; // Para compatibilidad con frontend
    private String elementoAfectado; // Campo combinado para mostrar
    
    // Datos del usuario (REPORTADO POR)
    private Long idUsuario;
    private Long id_usuario; // Para compatibilidad con frontend
    private String nombreUsuario;
    private String nombre_usuario; // Para compatibilidad con frontend
    private String cedulaUsuario;
    private String cedula_usuario; // Para compatibilidad con frontend
    private String reportadoPor; // Campo combinado para mostrar
    
    // Constructor desde entidad
    public ReporteIncidenciaResponse(ReporteIncidencia reporte) {
        this.idReporte = reporte.getIdReporte();
        this.id_reporte = reporte.getIdReporte();
        this.estado = reporte.getEstado();
        this.tipoIncidencia = reporte.getTipoIncidencia();
        this.tipo_incidencia = reporte.getTipoIncidencia();
        this.detalleProblema = reporte.getDetalleProblema();
        this.detalle_problema = reporte.getDetalleProblema();
        this.fechaHora = reporte.getFechaHora();
        this.fecha_hora = reporte.getFechaHora() != null ? 
            reporte.getFechaHora().toString() : null;
        
        // Datos del bien
        if (reporte.getBien() != null) {
            this.idBien = reporte.getBien().getIdBien();
            this.id_bien = reporte.getBien().getIdBien();
            this.codigoBien = reporte.getBien().getCodigoBien();
            this.codigo_bien = reporte.getBien().getCodigoBien();
            this.nombreBien = reporte.getBien().getNombreBien();
            this.nombre_bien = reporte.getBien().getNombreBien();
            this.claseBien = reporte.getBien().getClaseBien();
            this.clase_bien = reporte.getBien().getClaseBien();
            
            // Crear campo combinado para "elemento afectado"
            if (reporte.getBien().getCodigoBien() != null && !reporte.getBien().getCodigoBien().isEmpty()) {
                this.elementoAfectado = reporte.getBien().getCodigoBien();
            } else if (reporte.getBien().getNombreBien() != null && !reporte.getBien().getNombreBien().isEmpty()) {
                this.elementoAfectado = reporte.getBien().getNombreBien();
            } else if (reporte.getBien().getClaseBien() != null && !reporte.getBien().getClaseBien().isEmpty()) {
                this.elementoAfectado = reporte.getBien().getClaseBien();
            } else {
                this.elementoAfectado = "Bien #" + reporte.getBien().getIdBien();
            }
        } else {
            this.elementoAfectado = "No disponible";
        }
        
        // Datos del usuario
        if (reporte.getUsuario() != null) {
            this.idUsuario = reporte.getUsuario().getIdUsuario();
            this.id_usuario = reporte.getUsuario().getIdUsuario();
            this.nombreUsuario = reporte.getUsuario().getNombresCompletos();
            this.nombre_usuario = reporte.getUsuario().getNombresCompletos();
            this.cedulaUsuario = reporte.getUsuario().getCedula();
            this.cedula_usuario = reporte.getUsuario().getCedula();
            
            // Crear campo combinado para "reportado por"
            if (reporte.getUsuario().getNombresCompletos() != null && !reporte.getUsuario().getNombresCompletos().isEmpty()) {
                this.reportadoPor = reporte.getUsuario().getNombresCompletos();
            } else {
                this.reportadoPor = reporte.getUsuario().getCedula() != null ? 
                    reporte.getUsuario().getCedula() : "Usuario #" + reporte.getUsuario().getIdUsuario();
            }
        } else {
            this.reportadoPor = "No disponible";
        }
    }
}

