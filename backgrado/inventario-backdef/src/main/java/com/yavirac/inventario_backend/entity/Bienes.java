package com.yavirac.inventario_backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "bienes")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Bienes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_bien")
    private Long idBien;
    
    // Código único del bien (de INVENTARIO BIEN MUEBLES - CODIGO DEL BIEN)
    @Column(name = "codigo_bien", length = 50, unique = true)
    private String codigoBien;
    
    // Datos de INVENTARIO BIEN MUEBLES
    @Column(name = "cuenta_tipo_bien", length = 50)
    private String cuentaTipoBien; // Ej: "141.01.03", "141.01.07"
    
    @Column(name = "tipo_bien", length = 100)
    private String tipoBien; // Ej: "Mobiliario", "Maquinaria y Equipos"
    
    @Column(name = "clase_bien", length = 150)
    private String claseBien; // Ej: "MESA DE COMPUTADOR", "COMPUTADORAS PORTATILES"
    
    @Column(name = "descripcion", columnDefinition = "TEXT")
    private String descripcion;
    
    @Column(name = "marca", length = 50)
    private String marca;
    
    @Column(name = "modelo", length = 50)
    private String modelo;
    
    @Column(name = "serie", length = 50)
    private String serie;
    
    @Column(name = "valor_con_iva", precision = 10, scale = 2)
    private BigDecimal valorConIva;
    
    @Column(name = "estado", length = 50)
    private String estado; // Ej: "BUENO", "REGULAR"
    
    @Column(name = "custodio", length = 150)
    private String custodio;
    
    @Column(name = "ubicacion", length = 100)
    private String ubicacion;
    
    @Column(name = "provincia", length = 50)
    private String provincia;
    
    @Column(name = "observaciones", columnDefinition = "TEXT")
    private String observaciones;
    
    @Column(name = "observaciones2", columnDefinition = "TEXT")
    private String observaciones2;
    
    // Códigos adicionales
    @Column(name = "codigo_secap", length = 50)
    private String codigoSecap; // Código SECAP (ej: "QTS-01025")
    
    @Column(name = "codigo_inventario", length = 50)
    private String codigoInventario;
    
    // Datos de BIENES SECAP
    @Column(name = "nombre_bien", length = 200)
    private String nombreBien; // Nombre del bien desde SECAP
    
    @Column(name = "especificaciones", columnDefinition = "TEXT")
    private String especificaciones; // Especificaciones desde SECAP
    
    @Column(name = "valor_compra_inicial", precision = 10, scale = 2)
    private BigDecimal valorCompraInicial;
    
    @Column(name = "detalle_estado", columnDefinition = "TEXT")
    private String detalleEstado; // Detalle del estado desde SECAP
    
    // Relación con categoría
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
    
    // Campo adicional para identificar si es de INVENTARIO o SECAP
    @Column(name = "origen", length = 20)
    private String origen; // "INVENTARIO" o "SECAP"

    // Relación con Aula
@ManyToOne(fetch = FetchType.EAGER)
@JoinColumn(name = "id_aula")
private Aula aula;

}


