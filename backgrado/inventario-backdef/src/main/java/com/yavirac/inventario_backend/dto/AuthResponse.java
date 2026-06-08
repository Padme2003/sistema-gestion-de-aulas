package com.yavirac.inventario_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String cedula;
    private String rol;
    private Long idUsuario;       // ✅ ID real
    private String nombre;        // ✅ Nombre del usuario
    private Boolean estado;       // ✅ Estado del usuario
    private String fechaRegistro; // ✅ Fecha de registro
}

