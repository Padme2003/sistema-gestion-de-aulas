package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.service.AulaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/aulas")
@CrossOrigin(origins = "*")
public class AulaController {
    
    @Autowired
    private AulaService aulaService;
    
    @GetMapping
    public ResponseEntity<List<Aula>> findAll() {
        return ResponseEntity.ok(aulaService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Aula> findById(@PathVariable Long id) {
        Optional<Aula> aula = aulaService.findById(id);
        return aula.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<Aula> save(@RequestBody Aula aula) {
        return ResponseEntity.status(HttpStatus.CREATED).body(aulaService.save(aula));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<Aula> update(@PathVariable Long id, @RequestBody Aula aula) {
        try {
            return ResponseEntity.ok(aulaService.update(id, aula));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            aulaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}

