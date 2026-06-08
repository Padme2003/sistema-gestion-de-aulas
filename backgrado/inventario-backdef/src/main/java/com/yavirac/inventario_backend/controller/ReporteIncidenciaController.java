package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.ReporteIncidenciaRequest;
import com.yavirac.inventario_backend.dto.ReporteIncidenciaResponse;
import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import com.yavirac.inventario_backend.service.ReporteIncidenciaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/reportes")
@CrossOrigin(origins = "*")
public class ReporteIncidenciaController {
    
    @Autowired
    private ReporteIncidenciaService reporteIncidenciaService;
    
    @GetMapping
    public ResponseEntity<List<ReporteIncidenciaResponse>> findAll() {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findAll();
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ReporteIncidenciaResponse> findById(@PathVariable Long id) {
        Optional<ReporteIncidencia> reporte = reporteIncidenciaService.findById(id);
        return reporte.map(r -> ResponseEntity.ok(new ReporteIncidenciaResponse(r)))
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByUsuario(@PathVariable Long idUsuario) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByUsuario(idUsuario);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/bien/{idBien}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByBien(@PathVariable Long idBien) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByBien(idBien);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/estado/{estado}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByEstado(@PathVariable String estado) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByEstado(estado);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @GetMapping("/tipo/{tipoIncidencia}")
    public ResponseEntity<List<ReporteIncidenciaResponse>> findByTipoIncidencia(@PathVariable String tipoIncidencia) {
        List<ReporteIncidencia> reportes = reporteIncidenciaService.findByTipoIncidencia(tipoIncidencia);
        List<ReporteIncidenciaResponse> responses = reportes.stream()
                .map(ReporteIncidenciaResponse::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(responses);
    }
    
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Map<String, Object> requestMap) {
        try {
            System.out.println("📝 Recibiendo request para crear reporte:");
            System.out.println("   Request completo: " + requestMap);

            // Validar fecha enviada por el frontend (si viene)
            Object fechaRaw = requestMap.get("fecha") != null ? requestMap.get("fecha") :
                (requestMap.get("fecha_hora") != null ? requestMap.get("fecha_hora") :
                    (requestMap.get("fechaHora") != null ? requestMap.get("fechaHora") :
                        (requestMap.get("fecha_reporte") != null ? requestMap.get("fecha_reporte") :
                            requestMap.get("fechaReporte"))));
            if (fechaRaw != null) {
                LocalDate fecha = parseFechaRequest(fechaRaw);
                LocalDate hoy = LocalDate.now();
                if (!hoy.equals(fecha)) {
                    throw new RuntimeException("La fecha debe ser la actual (no pasada ni futura).");
                }
            }
            
            // Mapear desde snake_case (frontend) a camelCase (backend)
            Long idBien = requestMap.get("id_bien") != null ? 
                Long.valueOf(requestMap.get("id_bien").toString()) : 
                (requestMap.get("idBien") != null ? Long.valueOf(requestMap.get("idBien").toString()) : null);
            
            Long idUsuario = requestMap.get("id_usuario") != null ? 
                Long.valueOf(requestMap.get("id_usuario").toString()) : 
                (requestMap.get("idUsuario") != null ? Long.valueOf(requestMap.get("idUsuario").toString()) : null);
            
            String tipoIncidencia = requestMap.get("tipo_incidencia") != null ? 
                requestMap.get("tipo_incidencia").toString() : 
                (requestMap.get("tipoIncidencia") != null ? requestMap.get("tipoIncidencia").toString() : null);
            
            String detalleProblema = requestMap.get("detalle_problema") != null ? 
                requestMap.get("detalle_problema").toString() : 
                (requestMap.get("detalleProblema") != null ? requestMap.get("detalleProblema").toString() : null);
            
            String estado = requestMap.get("estado") != null ? 
                requestMap.get("estado").toString() : "Pendiente";
            
            System.out.println("   idBien: " + idBien);
            System.out.println("   idUsuario: " + idUsuario);
            System.out.println("   tipoIncidencia: " + tipoIncidencia);
            System.out.println("   estado: " + estado);
            System.out.println("   detalleProblema: " + detalleProblema);
            
            // Crear el DTO
            ReporteIncidenciaRequest request = new ReporteIncidenciaRequest();
            request.setIdBien(idBien);
            request.setIdUsuario(idUsuario);
            request.setTipoIncidencia(tipoIncidencia);
            request.setDetalleProblema(detalleProblema);
            request.setEstado(estado);
            
            ReporteIncidencia reporteGuardado = reporteIncidenciaService.save(request);
            System.out.println("✅ Reporte guardado con ID: " + reporteGuardado.getIdReporte());
            
            // Convertir a Response con datos del bien y usuario
            ReporteIncidenciaResponse response = new ReporteIncidenciaResponse(reporteGuardado);
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            System.err.println("❌ Error al guardar reporte: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al guardar reporte: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ReporteIncidencia> update(@PathVariable Long id, 
                                                     @RequestBody ReporteIncidenciaRequest request) {
        try {
            return ResponseEntity.ok(reporteIncidenciaService.update(id, request));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<ReporteIncidencia> cambiarEstado(@PathVariable Long id, 
                                                            @RequestBody Map<String, String> request) {
        try {
            String nuevoEstado = request.get("estado");
            return ResponseEntity.ok(reporteIncidenciaService.cambiarEstado(id, nuevoEstado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            reporteIncidenciaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    private LocalDate parseFechaRequest(Object fechaRaw) {
        if (fechaRaw instanceof String) {
            String value = ((String) fechaRaw).trim();
            if (value.isEmpty()) {
                throw new RuntimeException("La fecha es requerida.");
            }
            try {
                if (value.length() == 10) {
                    return LocalDate.parse(value);
                }
                try {
                    return LocalDateTime.parse(value).toLocalDate();
                } catch (DateTimeParseException ignored) {
                    return OffsetDateTime.parse(value).toLocalDate();
                }
            } catch (DateTimeParseException e) {
                throw new RuntimeException("Formato de fecha inválido. Use YYYY-MM-DD.");
            }
        }
        if (fechaRaw instanceof Number) {
            Instant instant = Instant.ofEpochMilli(((Number) fechaRaw).longValue());
            return instant.atZone(ZoneId.systemDefault()).toLocalDate();
        }
        throw new RuntimeException("Formato de fecha inválido.");
    }
}

