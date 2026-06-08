package com.yavirac.inventario_backend.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.yavirac.inventario_backend.dto.AsignacionRequest;
import com.yavirac.inventario_backend.entity.Asignacion;
import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.service.AsignacionService;

@RestController
@RequestMapping("/api/asignaciones")
@CrossOrigin(origins = "*")
public class AsignacionController {
    
    @Autowired
    private AsignacionService asignacionService;
    
    @GetMapping
    public ResponseEntity<List<Asignacion>> findAll() {
        return ResponseEntity.ok(asignacionService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Asignacion> findById(@PathVariable Long id) {
        Optional<Asignacion> asignacion = asignacionService.findById(id);
        return asignacion.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
public ResponseEntity<Asignacion> save(@RequestBody AsignacionRequest request) {

    if (request.getIdAula() == null || request.getIdUsuario() == null) {
        return ResponseEntity.badRequest().build();
    }

    return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(asignacionService.save(
                    request.getIdAula(),
                    request.getIdUsuario()
            ));
}


    
    @PutMapping("/{id}")
    public ResponseEntity<Asignacion> update(@PathVariable Long id, @RequestBody Map<String, Object> request) {
        Long idAula = request.get("idAula") != null ? Long.valueOf(request.get("idAula").toString()) : null;
        Long idUsuario = request.get("idUsuario") != null ? Long.valueOf(request.get("idUsuario").toString()) : null;
        Boolean estado = request.get("estado") != null ? Boolean.valueOf(request.get("estado").toString()) : null;
        try {
            return ResponseEntity.ok(asignacionService.update(id, idAula, idUsuario, estado));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            asignacionService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<Asignacion>> findByUsuarioId(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(asignacionService.findByUsuarioId(idUsuario));
    }
    
    @GetMapping("/usuario/{idUsuario}/aulas")
    public ResponseEntity<List<Aula>> findAulasByUsuarioId(@PathVariable Long idUsuario) {
        return ResponseEntity.ok(asignacionService.findAulasByUsuarioId(idUsuario));
    }
}

