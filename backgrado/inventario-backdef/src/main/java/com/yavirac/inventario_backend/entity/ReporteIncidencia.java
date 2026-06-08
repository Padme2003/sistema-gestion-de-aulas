package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "reportes_incidencias")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteIncidencia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reporte")
    private Long idReporte;
    
    @Column(name = "estado", nullable = false, length = 50)
    private String estado = "Pendiente"; // "Pendiente", "En revisión", "Resuelto", "Cerrado"
    
    @Column(name = "tipo_incidencia", nullable = false, length = 100)
    private String tipoIncidencia; // "Daño", "Fallo técnico", "Pérdida", etc.
    
    @Column(name = "detalle_problema", nullable = false, columnDefinition = "TEXT")
    private String detalleProblema;
    
    @CreationTimestamp
    @Column(name = "fecha_hora", nullable = false, updatable = false)
    private LocalDateTime fechaHora;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_bien", nullable = false)
    private Bienes bien; // ELEMENTO AFECTADO
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario; // REPORTADO POR
}
