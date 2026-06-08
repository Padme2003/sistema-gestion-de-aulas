package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "notificaciones")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Notificaciones {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_notificacion")
    private Long idNotificacion;
    
    @Column(name = "tipo_notificacion", length = 50)
    private String tipoNotificacion; // "REPORTE_INCIDENCIA"
    
    // Campos para reportes de incidencias
    @Column(name = "id_reporte")
    private Long idReporte; // FK a reportes_incidencias
    
    @Column(name = "estado", length = 50)
    private String estado; // "Pendiente", "En reparación", "Resuelto", "Cerrado"
    
    @Column(name = "tipo_incidencia", length = 100)
    private String tipoIncidencia; // "Daño Físico", "Mantenimiento", "Pérdida/Ausencia", etc.
    
    @Column(name = "detalle_problema", columnDefinition = "TEXT")
    private String detalleProblema;
    
    @Column(name = "elemento_afectado", length = 200)
    private String elementoAfectado; // Nombre o código del bien afectado
    
    @Column(name = "reportado_por", length = 200)
    private String reportadoPor; // Nombre del usuario que reporta
    
    @Column(name = "fecha_hora_reporte")
    private LocalDateTime fechaHoraReporte;
    
    @CreationTimestamp
    @Column(name = "fecha_creacion", nullable = false, updatable = false)
    private LocalDateTime fechaCreacion;
    
    @Column(name = "leida", nullable = false)
    private Boolean leida = false;
}

