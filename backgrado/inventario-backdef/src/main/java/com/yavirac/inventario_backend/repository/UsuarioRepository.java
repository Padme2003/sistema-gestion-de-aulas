package com.yavirac.inventario_backend.repository;

import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByCedula(String cedula);
    boolean existsByCedula(String cedula);
    
    @Query("SELECT u FROM Usuario u LEFT JOIN FETCH u.rol WHERE u.cedula = :cedula")
    Optional<Usuario> findByCedulaWithRol(@Param("cedula") String cedula);
    
    @Query("SELECT DISTINCT u FROM Usuario u LEFT JOIN FETCH u.rol")
    List<Usuario> findAllWithRol();

    @Modifying
    @Query("UPDATE Usuario u SET u.nombresCompletos = :nombres, u.rol = :rol, u.contraseña = :contrasena, u.estado = :estado WHERE u.cedula = :cedula")
    int updateByCedula(@Param("cedula") String cedula,
                       @Param("nombres") String nombres,
                       @Param("rol") Rol rol,
                       @Param("contrasena") String contrasena,
                       @Param("estado") Boolean estado);

}

