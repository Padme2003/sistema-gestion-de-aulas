package com.yavirac.inventario_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReporteIncidenciaRequest {
    private Long idBien; // ELEMENTO AFECTADO
    private Long idUsuario; // REPORTADO POR
    private String estado; // ESTADO
    private String tipoIncidencia; // TIPO DE INCIDENCIA
    private String detalleProblema; // DETALLE DEL PROBLEMA
}

