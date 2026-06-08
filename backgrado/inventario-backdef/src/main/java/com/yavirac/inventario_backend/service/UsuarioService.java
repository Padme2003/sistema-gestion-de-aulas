package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.dto.UsuarioRequest;
import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;

@Service
public class UsuarioService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final Pattern PASSWORD_PATTERN = Pattern.compile("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).{8,}$");
    
    public List<Usuario> findAll() {
        return usuarioRepository.findAllWithRol();
    }
    
    public Optional<Usuario> findById(Long id) {
        Optional<Usuario> usuario = usuarioRepository.findById(id);
        // Asegurar que el rol esté cargado
        if (usuario.isPresent() && usuario.get().getRol() == null) {
            // Si el rol no está cargado, recargar con JOIN FETCH
            return usuarioRepository.findByCedulaWithRol(usuario.get().getCedula());
        }
        return usuario;
    }

    public Optional<Usuario> findByCedula(String cedula) {
        return usuarioRepository.findByCedulaWithRol(cedula);
    }
    
    @Transactional
    public Usuario save(UsuarioRequest request) {
        // Validar que la cédula no esté vacía
        if (request.getCedula() == null || request.getCedula().trim().isEmpty()) {
            throw new RuntimeException("La cédula es requerida");
        }
        
        String cedula = request.getCedula().trim();
        if (!usuarioRepository.existsByCedula(cedula)) {
            throw new RuntimeException("La cédula no existe en la base de datos");
        }

        // Validar que los nombres no estén vacíos
        if (request.getNombresCompletos() == null || request.getNombresCompletos().trim().isEmpty()) {
            throw new RuntimeException("Los nombres completos son requeridos");
        }

        // Validar que el rol exista
        if (request.getIdRol() == null) {
            throw new RuntimeException("El rol es requerido");
        }

        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> {
                    List<Rol> rolesDisponibles = rolRepository.findAll();
                    String rolesStr = rolesDisponibles.stream()
                            .map(r -> r.getIdRol() + "=" + r.getNombre())
                            .reduce((a, b) -> a + ", " + b)
                            .orElse("No hay roles disponibles");
                    return new RuntimeException("Rol no encontrado con ID: " + request.getIdRol() + 
                            ". Roles disponibles: " + rolesStr);
                });

        String contrasena = null;
        if (request.getContraseña() != null && !request.getContraseña().trim().isEmpty()) {
            validarPassword(request.getContraseña().trim());
            contrasena = passwordEncoder.encode(request.getContraseña());
        } else {
            throw new RuntimeException("La contraseña es requerida");
        }
        Boolean estado = request.getEstado() != null ? request.getEstado() : true;

        int updated = usuarioRepository.updateByCedula(
                cedula,
                request.getNombresCompletos().trim(),
                rol,
                contrasena,
                estado
        );
        if (updated == 0) {
            throw new RuntimeException("No se pudo actualizar el usuario");
        }

        return usuarioRepository.findByCedulaWithRol(cedula)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado después de actualizar"));
    }

    private void validarPassword(String password) {
        if (!PASSWORD_PATTERN.matcher(password).matches()) {
            throw new RuntimeException("La contraseña debe tener mínimo 8 caracteres e incluir mayúscula, minúscula y número");
        }
    }
    
    public Usuario update(Long id, UsuarioRequest request) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        if (request.getCedula() != null && !usuario.getCedula().equals(request.getCedula()) && 
            usuarioRepository.existsByCedula(request.getCedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        
        Rol rol = rolRepository.findById(request.getIdRol())
                .orElseThrow(() -> new RuntimeException("Rol no encontrado"));
        
        if (request.getNombresCompletos() != null) {
            usuario.setNombresCompletos(request.getNombresCompletos());
        }
        usuario.setRol(rol);
        if (request.getCedula() != null) {
            usuario.setCedula(request.getCedula());
        }
        if (request.getContraseña() != null && !request.getContraseña().isEmpty()) {
            usuario.setContraseña(passwordEncoder.encode(request.getContraseña()));
        }
        usuario.setEstado(request.getEstado() != null ? request.getEstado() : usuario.getEstado());
        
        return usuarioRepository.save(usuario);
    }
    
    public void deleteById(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
    
    public Usuario cambiarEstado(Long id, Boolean estado) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        usuario.setEstado(estado);
        return usuarioRepository.save(usuario);
    }
    
    public Usuario desactivar(Long id) {
        return cambiarEstado(id, false);
    }
    
    public Usuario activar(Long id) {
        return cambiarEstado(id, true);
    }
}

