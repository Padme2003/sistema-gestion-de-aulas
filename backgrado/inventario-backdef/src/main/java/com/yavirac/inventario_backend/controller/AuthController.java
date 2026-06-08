package com.yavirac.inventario_backend.controller;

import com.yavirac.inventario_backend.dto.AuthRequest;
import com.yavirac.inventario_backend.dto.AuthResponse;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private AuthService authService;
    
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
    
    // Método para enmascarar tokens en logs
    private String maskToken(String token) {
        if (token == null || token.isEmpty()) {
            return "***";
        }
        if (token.length() <= 10) {
            return "***";
        }
        return token.substring(0, 6) + "..." + token.substring(token.length() - 4);
    }
    
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest request) {
        try {
            System.out.println("🔐 Intento de login para: " + maskCedula(request.getCedula()));
            AuthResponse response = authService.login(request);
            System.out.println("✅ Login exitoso para: " + maskCedula(response.getCedula()));
            System.out.println("📤 Enviando respuesta: token=" + maskToken(response.getToken()) + 
                             ", cedula=" + maskCedula(response.getCedula()) + ", rol=" + response.getRol());
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            System.err.println("❌ Error en login: " + e.getMessage());
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
        } catch (Exception e) {
            System.err.println("❌ Error inesperado en login");
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
    
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Usuario usuario) {
        try {
            Usuario nuevoUsuario = authService.register(usuario);
            return ResponseEntity.ok(nuevoUsuario);
        } catch (RuntimeException e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
        } catch (Exception e) {
            Map<String, String> error = new HashMap<>();
            error.put("error", "Error al procesar la solicitud: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
        }
    }
}

