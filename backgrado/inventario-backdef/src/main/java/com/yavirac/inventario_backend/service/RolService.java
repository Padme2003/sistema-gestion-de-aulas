package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.repository.RolRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RolService {
    
    @Autowired
    private RolRepository rolRepository;
    
    public List<Rol> findAll() {
        return rolRepository.findAll();
    }
    
    public Optional<Rol> findById(Long id) {
        return rolRepository.findById(id);
    }
    
    public Rol save(Rol rol) {
        return rolRepository.save(rol);
    }
    
    public Rol update(Long id, Rol rol) {
        Rol existingRol = rolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        existingRol.setNombre(rol.getNombre());
        return rolRepository.save(existingRol);
    }
    
    public void deleteById(Long id) {
        if (!rolRepository.existsById(id)) {
            throw new RuntimeException("Rol no encontrado");
        }
        rolRepository.deleteById(id);
    }
}

