package com.yavirac.inventario_backend.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Long idUsuario;
    
    @Column(name = "nombres_completos", nullable = false, length = 255)
    private String nombresCompletos;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_rol")
    private Rol rol;
    
    @Column(name = "cedula", unique = true, nullable = false, length = 20)
    private String cedula;
    
    @Column(name = "contraseña")
    private String contraseña;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
    
    @CreationTimestamp
    @Column(name = "fecha_registro", nullable = false, updatable = false)
    private LocalDateTime fechaRegistro;

    @JsonProperty("nombre")
    public String getNombre() {
        return nombresCompletos;
    }

    @JsonProperty("nombres_completos")
    public String getNombres_completos() {
        return nombresCompletos;
    }
}

