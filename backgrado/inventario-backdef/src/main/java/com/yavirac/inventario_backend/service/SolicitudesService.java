package com.yavirac.inventario_backend.service;

import com.yavirac.inventario_backend.entity.Bienes;
import com.yavirac.inventario_backend.entity.Solicitudes;
import com.yavirac.inventario_backend.repository.AsignacionRepository;
import com.yavirac.inventario_backend.repository.BienesRepository;
import com.yavirac.inventario_backend.repository.SolicitudesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SolicitudesService {

    @Autowired
    private SolicitudesRepository solicitudesRepository;

    @Autowired
    private BienesRepository bienesRepository;

    @Autowired
    private AsignacionRepository asignacionRepository;

    // Listar todas las solicitudes
    public List<Solicitudes> findAll() {
        return solicitudesRepository.findAll();
    }

    // Obtener solicitud por ID
    public Optional<Solicitudes> findById(Long id) {
        return solicitudesRepository.findById(id);
    }

    // Guardar nueva solicitud
    public Solicitudes save(Long idBien, String descripcion, String estado) {

        Bienes bien = bienesRepository.findById(idBien)
                .orElseThrow(() -> new RuntimeException("Bien no encontrado"));

        Solicitudes solicitud = new Solicitudes();
        solicitud.setBien(bien);
        solicitud.setDescripcion(descripcion);
        solicitud.setEstado(estado);

        return solicitudesRepository.save(solicitud);
    }

    // Actualizar solicitud
    public Solicitudes update(Long id, Long idBien, Long idSolicitud, String estado) {
        Solicitudes solicitud = solicitudesRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

        if (idBien != null) {
            Bienes bien = bienesRepository.findById(idBien)
                    .orElseThrow(() -> new RuntimeException("Bien no encontrado"));
            solicitud.setBien(bien);
        }

        if (idSolicitud != null) {
            solicitud.setIdSolicitud(idSolicitud);
        }

        if (estado != null) {
            solicitud.setEstado(estado);
        }

        return solicitudesRepository.save(solicitud);
    }

    // Eliminar solicitud
    public void deleteById(Long id) {
        if (!solicitudesRepository.existsById(id)) {
            throw new RuntimeException("Solicitud no encontrada");
        }
        solicitudesRepository.deleteById(id);
    }

    // Listar solicitudes correspondientes a un docente
    public List<Solicitudes> findByDocente(Long idUsuario) {

        // 1️⃣ Obtener aulas asignadas al docente
        List<Long> aulasIds = asignacionRepository
                .findByUsuarioIdUsuarioAndEstadoTrue(idUsuario) // <-- Método que debes crear en AsignacionRepository
                .stream()
                .map(a -> a.getAula().getIdAula()) // acceder al id de Aula
                .collect(Collectors.toList());

        // 2️⃣ Obtener bienes de esas aulas
        List<Long> bienesIds = bienesRepository.findByAulaIdAulaIn(aulasIds)
        .stream()
        .map(b -> b.getIdBien())
        .toList();

        // 3️⃣ Filtrar solicitudes de esos bienes
        return solicitudesRepository.findByBienIdBienIn(bienesIds);
    }
    public Solicitudes cambiarEstado(Long id, String estado) {
    Solicitudes solicitud = solicitudesRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Solicitud no encontrada"));

    solicitud.setEstado(estado);
    return solicitudesRepository.save(solicitud);
}

}
