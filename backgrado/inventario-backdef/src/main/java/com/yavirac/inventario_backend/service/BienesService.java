package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.entity.Categoria;
import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.repository.BienesRepository;
import com.yavirac.inventario_backend.repository.CategoriaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.yavirac.inventario_backend.repository.AulaRepository;

import java.util.List;
import java.util.Optional;

@Service
public class BienesService {
    
    @Autowired
    private BienesRepository bienesRepository;
    
    @Autowired
    private AulaRepository aulaRepository;


    @Autowired
    private CategoriaRepository categoriaRepository;
    
    public List<Bienes> findAll() {
        return bienesRepository.findAll();
    }
    
    public Optional<Bienes> findById(Long id) {
        return bienesRepository.findById(id);
    }
    
    public Optional<Bienes> findByCodigoBien(String codigoBien) {
        return bienesRepository.findByCodigoBien(codigoBien);
    }
    
public Bienes save(Bienes bienes) {

    // 1️⃣ Validar código duplicado
    if (bienes.getCodigoBien() != null &&
        bienesRepository.existsByCodigoBien(bienes.getCodigoBien())) {
        throw new RuntimeException("El código de bien ya está registrado");
    }

    // 2️⃣ VALIDAR Y CARGAR CATEGORÍA (ESTO FALTABA BIEN HECHO)
    if (bienes.getCategoria() == null || bienes.getCategoria().getIdCategoria() == null) {
        throw new RuntimeException("Debe seleccionar una categoría válida");
    }

    Long idCategoria = bienes.getCategoria().getIdCategoria();
    if (idCategoria <= 0) {
        throw new RuntimeException("Debe seleccionar una categoría válida");
    }

    Categoria categoria = categoriaRepository.findById(idCategoria)
            .orElseThrow(() -> new RuntimeException("Categoría no encontrada con ID: " + idCategoria));

    bienes.setCategoria(categoria); // 🔥 CLAVE


    // 3️⃣ VALIDAR Y CARGAR AULA (ESTA PARTE YA LA TENÍAS BIEN)
    if (bienes.getAula() != null && bienes.getAula().getIdAula() != null) {

        Long idAula = bienes.getAula().getIdAula();
        if (idAula <= 0) {
            throw new RuntimeException("Debe seleccionar un aula válida");
        }

        Aula aula = aulaRepository.findById(idAula)
                .orElseThrow(() -> new RuntimeException("Aula no encontrada con ID: " + idAula));

        bienes.setAula(aula);
    } else {
        bienes.setAula(null); // permitido
    }

    return bienesRepository.save(bienes);
}

    
    public Bienes update(Long id, Bienes bienes) {
        Bienes existingBienes = bienesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
        
        if (bienes.getCodigoBien() != null && 
            !existingBienes.getCodigoBien().equals(bienes.getCodigoBien()) &&
            bienesRepository.existsByCodigoBien(bienes.getCodigoBien())) {
            throw new RuntimeException("El código de bien ya está registrado");
        }
        
        // Actualizar campos
        if (bienes.getCodigoBien() != null) existingBienes.setCodigoBien(bienes.getCodigoBien());
        if (bienes.getCuentaTipoBien() != null) existingBienes.setCuentaTipoBien(bienes.getCuentaTipoBien());
        if (bienes.getTipoBien() != null) existingBienes.setTipoBien(bienes.getTipoBien());
        if (bienes.getClaseBien() != null) existingBienes.setClaseBien(bienes.getClaseBien());
        if (bienes.getDescripcion() != null) existingBienes.setDescripcion(bienes.getDescripcion());
        if (bienes.getMarca() != null) existingBienes.setMarca(bienes.getMarca());
        if (bienes.getModelo() != null) existingBienes.setModelo(bienes.getModelo());
        if (bienes.getSerie() != null) existingBienes.setSerie(bienes.getSerie());
        if (bienes.getValorConIva() != null) existingBienes.setValorConIva(bienes.getValorConIva());
        if (bienes.getEstado() != null) existingBienes.setEstado(bienes.getEstado());
        if (bienes.getCustodio() != null) existingBienes.setCustodio(bienes.getCustodio());
        if (bienes.getUbicacion() != null) existingBienes.setUbicacion(bienes.getUbicacion());
        if (bienes.getProvincia() != null) existingBienes.setProvincia(bienes.getProvincia());
        if (bienes.getObservaciones() != null) existingBienes.setObservaciones(bienes.getObservaciones());
        if (bienes.getObservaciones2() != null) existingBienes.setObservaciones2(bienes.getObservaciones2());
        if (bienes.getCodigoSecap() != null) existingBienes.setCodigoSecap(bienes.getCodigoSecap());
        if (bienes.getCodigoInventario() != null) existingBienes.setCodigoInventario(bienes.getCodigoInventario());
        if (bienes.getNombreBien() != null) existingBienes.setNombreBien(bienes.getNombreBien());
        if (bienes.getEspecificaciones() != null) existingBienes.setEspecificaciones(bienes.getEspecificaciones());
        if (bienes.getValorCompraInicial() != null) existingBienes.setValorCompraInicial(bienes.getValorCompraInicial());
        if (bienes.getDetalleEstado() != null) existingBienes.setDetalleEstado(bienes.getDetalleEstado());
        if (bienes.getOrigen() != null) existingBienes.setOrigen(bienes.getOrigen());
        
        // Validar categoría si se proporciona
        if (bienes.getCategoria() != null && bienes.getCategoria().getIdCategoria() != null) {
            Long idCategoria = bienes.getCategoria().getIdCategoria();
            if (idCategoria <= 0) {
                throw new RuntimeException("Debe seleccionar una categoría válida");
            }
            Categoria categoria = categoriaRepository.findById(idCategoria)
                    .orElseThrow(() -> {
                        List<Categoria> todasLasCategorias = categoriaRepository.findAll();
                        String categoriasDisponibles = todasLasCategorias.stream()
                                .map(c -> "ID: " + c.getIdCategoria() + " - " + c.getNombre())
                                .reduce((a, b) -> a + ", " + b)
                                .orElse("No hay categorías disponibles");
                        return new RuntimeException("Categoría no encontrada con ID: " + idCategoria + 
                                ". Categorías disponibles: " + categoriasDisponibles);
                    });
            existingBienes.setCategoria(categoria);
        }
        
        // Validar aula si se proporciona
        if (bienes.getAula() != null && bienes.getAula().getIdAula() != null) {
            Long idAula = bienes.getAula().getIdAula();
            if (idAula <= 0) {
                throw new RuntimeException("Debe seleccionar un aula válida");
            }
            Aula aula = aulaRepository.findById(idAula)
                    .orElseThrow(() -> new RuntimeException("Aula no encontrada con ID: " + idAula));
            existingBienes.setAula(aula);
        }

        
        return bienesRepository.save(existingBienes);
    }
    
    public void deleteById(Long id) {
        if (!bienesRepository.existsById(id)) {
            throw new RuntimeException("Bien no encontrado");
        }
        bienesRepository.deleteById(id);
    }
}

