package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.ReporteIncidenciaResponse;
import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import com.yavirac.inventario_backend.service.ReporteIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/notificaciones")
@CrossOrigin(origins = "*")
public class NotificacionesController {
    
    @Autowired
    private ReporteIncidenciaService reporteIncidenciaService;
    
    /**
     * Obtener todos los reportes (funciona como notificaciones)
     */
    @GetMapping
    public ResponseEntity<List<ReporteIncidenciaResponse>> findAll() {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findAll();
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtener reportes pendientes (equivalente a "no leídas")
     */
    @GetMapping("/no-leidas")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findNoLeidas() {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByEstado("Pendiente");
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtener reportes por estado (similar a Solicitudes - filtro por estado)
     */
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByEstado(@PathVariable String estado) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByEstado(estado);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    /**
     * Obtener reporte por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReporteIncidenciaResponse> findById(@PathVariable Long id) {
        Optional<ReporteIncidencia> reporte = reporteIncidenciaService.findById(id);
        return reporte.map(r -> ResponseEntity.ok(new ReporteIncidenciaResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    /**
     * Obtener reportes por tipo de incidencia
     */
    @GetMapping("/tipo/{tipo}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByTipo(@PathVariable String tipo) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByTipoIncidencia(tipo);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
}

