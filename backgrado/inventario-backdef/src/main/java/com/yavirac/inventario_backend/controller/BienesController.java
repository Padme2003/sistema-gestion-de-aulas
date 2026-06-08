package com.yavirac.inventario_backend.controller;

import java.util.HashMap;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.service.BienesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.yavirac.inventario_backend.service.AulaService;
import com.yavirac.inventario_backend.service.CategoriaService;

@RestController
@RequestMapping("/api/bienes")
@CrossOrigin(origins = "*")
public class BienesController {
    
    @Autowired
    private BienesService bienesService;
    @Autowired
private CategoriaService categoriaService;

@Autowired
private AulaService aulaService;
    @GetMapping
    public ResponseEntity<List<Bienes>> findAll() {
        return ResponseEntity.ok(bienesService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Bienes> findById(@PathVariable Long id) {
        Optional<Bienes> bien = bienesService.findById(id);
        return bien.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @GetMapping("/codigo/{codigoBien}")
    public ResponseEntity<Bienes> findByCodigoBien(@PathVariable String codigoBien) {
        Optional<Bienes> bien = bienesService.findByCodigoBien(codigoBien);
        return bien.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/editar/{id}")
public ResponseEntity<?> obtenerBienParaEditar(@PathVariable Long id) {

    Optional<Bienes> bienOpt = bienesService.findById(id);

    if (bienOpt.isEmpty()) {
        return ResponseEntity.notFound().build();
    }

    Map<String, Object> response = new HashMap<>();
    response.put("bien", bienOpt.get());
    response.put("categorias", categoriaService.findAll());
    response.put("aulas", aulaService.findAll());

    return ResponseEntity.ok(response);
}
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Bienes bienes) {
        try {
            return ResponseEntity.status(HttpStatus.CREATED).body(bienesService.save(bienes));
        } catch (RuntimeException e) {
            System.err.println("❌ Error al crear bien: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al crear bien: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody Bienes bienes) {
        try {
            return ResponseEntity.ok(bienesService.update(id, bienes));
        } catch (RuntimeException e) {
            System.err.println("❌ Error al actualizar bien: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al actualizar bien: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            bienesService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

