package com.yavirac.inventario_backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRequest {
    @JsonAlias({"nombre", "nombres", "nombres_completos", "apellidos_y_nombres", "apellidosYNombres", "apellidos_nombres"})
    private String nombresCompletos;
    @JsonAlias({"idRol", "id_rol", "rol"})
    private Long idRol;
    @JsonAlias({"email", "cedula"})
    private String cedula;
    @JsonAlias({"contraseña", "contrasena", "password", "clave"})
    private String contraseña;
    private Boolean estado;
}

