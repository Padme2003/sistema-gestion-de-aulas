package com.yavirac.inventario_backend.security;

import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Override
    public UserDetails loadUserByUsername(String cedula) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepository.findByCedulaWithRol(cedula)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con cédula: " + cedula));
        if (usuario.getContraseña() == null || usuario.getContraseña().isEmpty()) {
            throw new UsernameNotFoundException("Usuario sin contraseña asignada");
        }
        
        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getCedula())
                .password(usuario.getContraseña())
                .authorities(usuario.getRol().getNombre())
                .build();
    }
}

