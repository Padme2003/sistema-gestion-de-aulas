package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.UsuarioRequest;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "*")
public class UsuarioController {
    
    @Autowired
    private UsuarioService usuarioService;
    
    // Método para enmascarar cédulas en logs
    private String maskCedula(String cedula) {
        if (cedula == null || cedula.isEmpty()) {
            return "***";
        }
        if (cedula.length() <= 4) {
            return "***";
        }
        return cedula.substring(0, 2) + "***" + cedula.substring(cedula.length() - 2);
    }
    
    @GetMapping
    public ResponseEntity<List<Usuario>> findAll() {
        return ResponseEntity.ok(usuarioService.findAll());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Usuario> findById(@PathVariable Long id) {
        Optional<Usuario> usuario = usuarioService.findById(id);
        return usuario.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/cedula/{cedula}")
    public ResponseEntity<Usuario> findByCedula(@PathVariable String cedula) {
        return usuarioService.findByCedula(cedula)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    
    @PostMapping
    public ResponseEntity<?> save(@RequestBody Map<String, Object> requestMap) {
        try {
            String cedula = valueOrNull(requestMap, "cedula", "ci", "cedula_usuario", "cedulaUsuario", "identificacion");
            String nombres = valueOrNull(requestMap, "nombres_completos", "nombresCompletos", "nombre", "nombres", "apellidos_y_nombres");
            String contrasena = valueOrNull(requestMap, "contraseña", "contrasena", "password", "clave");
            Long idRol = longOrNull(requestMap, "idRol", "id_rol", "rol");

            UsuarioRequest request = new UsuarioRequest();
            request.setCedula(cedula);
            request.setNombresCompletos(nombres);
            request.setContraseña(contrasena);
            request.setIdRol(idRol);

            System.out.println("📝 Creando usuario: " + maskCedula(request.getCedula()));
            System.out.println("   Nombres completos: " + (request.getNombresCompletos() != null ? request.getNombresCompletos() : "N/A"));
            System.out.println("   Cédula: " + maskCedula(request.getCedula()));
            System.out.println("   Rol ID: " + request.getIdRol());
            System.out.println("   Contraseña: " + (request.getContraseña() != null && !request.getContraseña().isEmpty() ? "***" : "No proporcionada"));
            
            // Validar campos requeridos
            if (request.getNombresCompletos() == null || request.getNombresCompletos().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Los nombres completos son requeridos");
            }
            if (request.getCedula() == null || request.getCedula().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La cédula es requerida");
            }
            if (request.getIdRol() == null) {
                return ResponseEntity.badRequest().body("El rol es requerido");
            }
            
            Usuario usuario = usuarioService.save(request);
            System.out.println("✅ Usuario creado exitosamente: " + maskCedula(usuario.getCedula()));
            return ResponseEntity.status(HttpStatus.CREATED).body(usuario);
        } catch (RuntimeException e) {
            System.err.println("❌ Error al crear usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.badRequest().body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al crear usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al crear el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }

    private String valueOrNull(Map<String, Object> map, String... keys) {
        for (String key : keys) {
            Object value = map.get(key);
            if (value != null) {
                String text = value.toString().trim();
                if (!text.isEmpty()) {
                    return text;
                }
            }
        }
        return null;
    }

    private Long longOrNull(Map<String, Object> map, String... keys) {
        String value = valueOrNull(map, keys);
        if (value == null) {
            return null;
        }
        try {
            return Long.valueOf(value);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Long id, @RequestBody UsuarioRequest request) {
        try {
            System.out.println("✏️  Actualizando usuario ID: " + id);
            
            // Validar campos requeridos
            if (request.getNombresCompletos() == null || request.getNombresCompletos().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Los nombres completos son requeridos");
            }
            if (request.getCedula() == null || request.getCedula().trim().isEmpty()) {
                return ResponseEntity.badRequest().body("La cédula es requerida");
            }
            if (request.getIdRol() == null) {
                return ResponseEntity.badRequest().body("El rol es requerido");
            }
            
            Usuario usuario = usuarioService.update(id, request);
            System.out.println("✅ Usuario actualizado exitosamente: " + maskCedula(usuario.getCedula()));
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            System.err.println("❌ Error al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado al actualizar usuario: " + e.getMessage());
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al actualizar el usuario: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable Long id) {
        try {
            usuarioService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
    
    @PutMapping("/{id}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Long id, @RequestBody Map<String, Boolean> request) {
        try {
            Boolean estado = request.get("estado");
            if (estado == null) {
                return ResponseEntity.badRequest().body("El campo 'estado' es requerido");
            }
            Usuario usuario = usuarioService.cambiarEstado(id, estado);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el estado: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/desactivar")
    public ResponseEntity<?> desactivar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.desactivar(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al desactivar el usuario: " + e.getMessage());
        }
    }
    
    @PutMapping("/{id}/activar")
    public ResponseEntity<?> activar(@PathVariable Long id) {
        try {
            Usuario usuario = usuarioService.activar(id);
            return ResponseEntity.ok(usuario);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al activar el usuario: " + e.getMessage());
        }
    }
}

