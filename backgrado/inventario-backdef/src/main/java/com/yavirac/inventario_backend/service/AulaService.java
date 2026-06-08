package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.repository.AulaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AulaService {
    
    @Autowired
    private AulaRepository aulaRepository;
    
    public List<Aula> findAll() {
        return aulaRepository.findAll();
    }
    
    public Optional<Aula> findById(Long id) {
        return aulaRepository.findById(id);
    }
    
    public Aula save(Aula aula) {
        return aulaRepository.save(aula);
    }
    
    public Aula update(Long id, Aula aula) {
        Aula existingAula = aulaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
        existingAula.setNombre(aula.getNombre());
        existingAula.setTipo(aula.getTipo());
        existingAula.setCapacidad(aula.getCapacidad());
        existingAula.setUbicacion(aula.getUbicacion());
        existingAula.setEstado(aula.getEstado());
        return aulaRepository.save(existingAula);
    }
    
    public void deleteById(Long id) {
        if (!aulaRepository.existsById(id)) {
            throw new RuntimeException("Aula no encontrada");
        }
        aulaRepository.deleteById(id);
    }
}

