package com.yavirac.inventario_backend.service;

import java.time.format.DateTimeFormatter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.yavirac.inventario_backend.dto.AuthRequest;
import com.yavirac.inventario_backend.dto.AuthResponse;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import com.yavirac.inventario_backend.security.JwtService;
@Service
public class AuthService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtService jwtService;
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
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
    
    @Transactional(readOnly = true)
    public AuthResponse login(AuthRequest request) {
        try {
            System.out.println("🔍 Verificando credenciales para: " + maskCedula(request.getCedula()));
            
            // Autenticar usuario (esto internamente llama a loadUserByUsername)
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getCedula(),
                            request.getPassword()
                    )
            );
            
            System.out.println("✅ Autenticación exitosa, obteniendo usuario...");
            
            // Obtener el usuario con su rol en una sola consulta
            Usuario usuario = usuarioRepository.findByCedulaWithRol(request.getCedula())
                    .orElseGet(() -> {
                        System.out.println("⚠️  Usando método fallback para obtener usuario...");
                        // Fallback: intentar con el método normal si el JOIN FETCH falla
                        return usuarioRepository.findByCedula(request.getCedula())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
                    });
            
            System.out.println("✅ Usuario encontrado: " + maskCedula(usuario.getCedula()));
            
            // Verificar que el usuario tenga rol
            if (usuario.getRol() == null) {
                System.err.println("❌ Usuario sin rol asignado");
                throw new RuntimeException("El usuario no tiene un rol asignado");
            }
            
            // Verificar que el usuario esté activo
            if (usuario.getEstado() == null || !usuario.getEstado()) {
                System.err.println("❌ Usuario inactivo");
                throw new RuntimeException("El usuario está inactivo");
            }

            if (usuario.getContraseña() == null || usuario.getContraseña().isEmpty()) {
                System.err.println("❌ Usuario sin contraseña asignada");
                throw new RuntimeException("El usuario no tiene contraseña asignada");
            }
            
            System.out.println("✅ Usuario válido, generando token...");
            
            // Crear UserDetails desde el usuario ya obtenido
            UserDetails userDetails = org.springframework.security.core.userdetails.User
                    .withUsername(usuario.getCedula())
                    .password(usuario.getContraseña())
                    .authorities(usuario.getRol().getNombre())
                    .build();
            
            String token = jwtService.generateToken(userDetails);
            
            System.out.println("✅ Token generado exitosamente");
          DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
  
return new AuthResponse(
    token,
    usuario.getCedula(),
    usuario.getRol().getNombre(),
    usuario.getIdUsuario(),
    usuario.getNombre(),
    usuario.getEstado(),
    usuario.getFechaRegistro().format(formatter) // <-- aquí convertimos a String
);
        } catch (BadCredentialsException e) {
            System.err.println("❌ Error de autenticación para: " + maskCedula(request.getCedula()));
            throw new RuntimeException("Credenciales incorrectas. Verifica tu cédula y contraseña.", e);
        } catch (RuntimeException e) {
            System.err.println("❌ Error: " + e.getMessage());
            throw e; // Re-lanzar RuntimeException tal cual
        } catch (Exception e) {
            System.err.println("❌ Error inesperado: " + e.getMessage());
            throw new RuntimeException("Error al iniciar sesión: " + e.getMessage(), e);
        }
    }
    
    public Usuario register(Usuario usuario) {
        if (usuarioRepository.existsByCedula(usuario.getCedula())) {
            throw new RuntimeException("La cédula ya está registrada");
        }
        usuario.setContraseña(passwordEncoder.encode(usuario.getContraseña()));
        return usuarioRepository.save(usuario);
    }
}

