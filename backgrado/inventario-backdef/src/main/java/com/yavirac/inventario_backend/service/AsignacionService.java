package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Asignacion;
import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.AsignacionRepository;
import com.yavirac.inventario_backend.repository.AulaRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AsignacionService {
    
    @Autowired
    private AsignacionRepository asignacionRepository;
    
    @Autowired
    private AulaRepository aulaRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    public List<Asignacion> findAll() {
        return asignacionRepository.findAll();
    }
    
    public Optional<Asignacion> findById(Long id) {
        return asignacionRepository.findById(id);
    }
    
    public Asignacion save(Long idAula, Long idUsuario) {
        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
        Usuario usuario = usuarioRepository.findById(idUsuario)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        Asignacion asignacion = new Asignacion();
        asignacion.setAula(aula);
        asignacion.setUsuario(usuario);
        asignacion.setEstado(true);
        
        return asignacionRepository.save(asignacion);
    }
    
    public Asignacion update(Long id, Long idAula, Long idUsuario, Boolean estado) {
        Asignacion asignacion = asignacionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Asignación no encontrada"));
        
        if (idAula != null) {
            Aula aula = aulaRepository.findById(idAula)
                    .orElseThrow(() -> new RuntimeException("Aula no encontrada"));
            asignacion.setAula(aula);
        }
        
        if (idUsuario != null) {
            Usuario usuario = usuarioRepository.findById(idUsuario)
                    .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
            asignacion.setUsuario(usuario);
        }
        
        if (estado != null) {
            asignacion.setEstado(estado);
        }
        
        return asignacionRepository.save(asignacion);
    }
    
    public void deleteById(Long id) {
        if (!asignacionRepository.existsById(id)) {
            throw new RuntimeException("Asignación no encontrada");
        }
        asignacionRepository.deleteById(id);
    }
    
    public List<Asignacion> findByUsuarioId(Long idUsuario) {
        return asignacionRepository.findByUsuarioIdUsuarioAndEstadoTrueWithRelations(idUsuario);
    }
    
    public List<Aula> findAulasByUsuarioId(Long idUsuario) {
        List<Asignacion> asignaciones = findByUsuarioId(idUsuario);
        return asignaciones.stream()
                .map(Asignacion::getAula)
                .distinct()
                .collect(Collectors.toList());
    }
}

