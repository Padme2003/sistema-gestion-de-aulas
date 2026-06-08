package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "aulas")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Aula {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_aula")
    private Long idAula;
    
    @Column(name = "nombre", length = 50, nullable = false)
    private String nombre;
    
    @Column(name = "tipo", length = 50)
    private String tipo;
    
    @Column(name = "capacidad")
    private Integer capacidad;
    
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    
    @Column(name = "estado", nullable = false)
    private Boolean estado = true;
}

