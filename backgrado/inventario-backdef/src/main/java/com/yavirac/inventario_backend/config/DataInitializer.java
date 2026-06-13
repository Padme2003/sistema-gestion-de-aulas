package com.yavirac.inventario_backend.config;

import com.yavirac.inventario_backend.entity.Aula;
import com.yavirac.inventario_backend.entity.Categoria;
import com.yavirac.inventario_backend.entity.Notificaciones;
import com.yavirac.inventario_backend.entity.ReporteIncidencia;
import com.yavirac.inventario_backend.entity.Rol;
import com.yavirac.inventario_backend.entity.Usuario;
import com.yavirac.inventario_backend.repository.AulaRepository;
import com.yavirac.inventario_backend.repository.CategoriaRepository;
import com.yavirac.inventario_backend.repository.NotificacionesRepository;
import com.yavirac.inventario_backend.repository.ReporteIncidenciaRepository;
import com.yavirac.inventario_backend.repository.RolRepository;
import com.yavirac.inventario_backend.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.ClassPathResource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {
    
    @Autowired
    private RolRepository rolRepository;
    
    @Autowired
    private UsuarioRepository usuarioRepository;
    
    @Autowired
    private AulaRepository aulaRepository;
    
    @Autowired
    private CategoriaRepository categoriaRepository;
    
    @Autowired
    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ReporteIncidenciaRepository reporteIncidenciaRepository;
    
    @Autowired
    private NotificacionesRepository notificacionesRepository;
    
    @Override
    public void run(String... args) throws Exception {
        try {
            // Actualizar tabla usuarios para cambio de email -> cédula y nombre -> nombres_completos
            actualizarTablaUsuarios();

            // Actualizar tabla notificaciones para agregar campos de reportes de incidencias
            actualizarTablaNotificaciones();
            
            // Limpiar columnas antiguas que no se usan para reportes de incidencias
            limpiarColumnasAntiguas();
            
            // Migrar reportes existentes a notificaciones
            migrarReportesExistentesANotificaciones();
            
            // Crear roles necesarios si no existen
            String[] rolesNecesarios = {"Administrador", "Docente", "Coordinador"};
            
            System.out.println("🔧 Inicializando roles del sistema...");
            for (String nombreRol : rolesNecesarios) {
                Rol rol = rolRepository.findAll().stream()
                        .filter(r -> nombreRol.equalsIgnoreCase(r.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (rol == null) {
                    rol = new Rol();
                    rol.setNombre(nombreRol);
                    rol = rolRepository.save(rol);
                    System.out.println("✅ Rol '" + nombreRol + "' creado con ID: " + rol.getIdRol());
                } else {
                    System.out.println("ℹ️  Rol '" + nombreRol + "' ya existe con ID: " + rol.getIdRol());
                }
            }
            
            // Mostrar todos los roles disponibles
            List<Rol> todosLosRoles = rolRepository.findAll();
            System.out.println("📋 Roles disponibles en el sistema:");
            for (Rol r : todosLosRoles) {
                System.out.println("   - ID: " + r.getIdRol() + " | Nombre: " + r.getNombre());
            }
            
            // Crear aulas necesarias si no existen
            String[] aulasNecesarias = {"Aula Xian", "Aula Sarsota", "Aula Gori", "Lab Inf 2", "Lab Idiomas"};
            
            System.out.println("🔧 Inicializando aulas del sistema...");
            for (String nombreAula : aulasNecesarias) {
                Aula aula = aulaRepository.findAll().stream()
                        .filter(a -> nombreAula.equalsIgnoreCase(a.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (aula == null) {
                    aula = new Aula();
                    aula.setNombre(nombreAula);
                    // Determinar tipo según el nombre
                    if (nombreAula.toLowerCase().contains("lab")) {
                        aula.setTipo("Laboratorio");
                        aula.setCapacidad(25);
                    } else {
                        aula.setTipo("Aula");
                        aula.setCapacidad(30);
                    }
                    aula.setEstado(true);
                    aula = aulaRepository.save(aula);
                    System.out.println("✅ Aula '" + nombreAula + "' creada con ID: " + aula.getIdAula());
                } else {
                    System.out.println("ℹ️  Aula '" + nombreAula + "' ya existe con ID: " + aula.getIdAula());
                }
            }
            
            // Mostrar todas las aulas disponibles
            List<Aula> todasLasAulas = aulaRepository.findAll();
            System.out.println("📋 Aulas disponibles en el sistema:");
            for (Aula a : todasLasAulas) {
                System.out.println("   - ID: " + a.getIdAula() + " | Nombre: " + a.getNombre() + 
                                 " | Tipo: " + (a.getTipo() != null ? a.getTipo() : "N/A"));
            }
            
            // Crear categorías necesarias si no existen
            String[] categoriasNecesarias = {"Bienes Muebles", "Bienes Secap", "Bienes Consumo"};
            
            System.out.println("🔧 Inicializando categorías del sistema...");
            for (String nombreCategoria : categoriasNecesarias) {
                Categoria categoria = categoriaRepository.findAll().stream()
                        .filter(c -> nombreCategoria.equalsIgnoreCase(c.getNombre()))
                        .findFirst()
                        .orElse(null);
                
                if (categoria == null) {
                    categoria = new Categoria();
                    categoria.setNombre(nombreCategoria);
                    categoria = categoriaRepository.save(categoria);
                    System.out.println("✅ Categoría '" + nombreCategoria + "' creada con ID: " + categoria.getIdCategoria());
                } else {
                    System.out.println("ℹ️  Categoría '" + nombreCategoria + "' ya existe con ID: " + categoria.getIdCategoria());
                }
            }
            
            // Mostrar todas las categorías disponibles
            List<Categoria> todasLasCategorias = categoriaRepository.findAll();
            System.out.println("📋 Categorías disponibles en el sistema:");
            for (Categoria c : todasLasCategorias) {
                System.out.println("   - ID: " + c.getIdCategoria() + " | Nombre: " + c.getNombre());
            }
            
            // Cargar usuarios base desde CSV (sin contraseña)
            cargarUsuariosDesdeCsv();
        } catch (Exception e) {
            System.err.println("❌ Error al inicializar datos: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Actualiza la tabla notificaciones agregando columnas para reportes de incidencias
     */
    private void actualizarTablaNotificaciones() {
        try {
            System.out.println("🔧 Actualizando tabla notificaciones para soportar reportes de incidencias...");
            
            // Verificar y agregar columna id_reporte
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS id_reporte BIGINT");
                System.out.println("   ✅ Columna 'id_reporte' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'id_reporte': " + e.getMessage());
            }
            
            // Verificar y agregar columna estado
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS estado VARCHAR(50)");
                System.out.println("   ✅ Columna 'estado' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'estado': " + e.getMessage());
            }
            
            // Verificar y agregar columna tipo_incidencia
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS tipo_incidencia VARCHAR(100)");
                System.out.println("   ✅ Columna 'tipo_incidencia' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'tipo_incidencia': " + e.getMessage());
            }
            
            // Verificar y agregar columna detalle_problema
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS detalle_problema TEXT");
                System.out.println("   ✅ Columna 'detalle_problema' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'detalle_problema': " + e.getMessage());
            }
            
            // Verificar y agregar columna elemento_afectado
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS elemento_afectado VARCHAR(200)");
                System.out.println("   ✅ Columna 'elemento_afectado' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'elemento_afectado': " + e.getMessage());
            }
            
            // Verificar y agregar columna reportado_por
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS reportado_por VARCHAR(200)");
                System.out.println("   ✅ Columna 'reportado_por' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'reportado_por': " + e.getMessage());
            }
            
            // Verificar y agregar columna fecha_hora_reporte
            try {
                jdbcTemplate.execute("ALTER TABLE public.notificaciones ADD COLUMN IF NOT EXISTS fecha_hora_reporte TIMESTAMP(6) WITHOUT TIME ZONE");
                System.out.println("   ✅ Columna 'fecha_hora_reporte' agregada o ya existe");
            } catch (Exception e) {
                System.out.println("   ⚠️  Columna 'fecha_hora_reporte': " + e.getMessage());
            }
            
            System.out.println("✅ Tabla 'notificaciones' actualizada correctamente");
            
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar tabla notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Migra los reportes de incidencias existentes a notificaciones
     */
    private void migrarReportesExistentesANotificaciones() {
        try {
            System.out.println("🔧 Migrando reportes existentes a notificaciones...");
            
            List<ReporteIncidencia> reportesExistentes = reporteIncidenciaRepository.findAll();
            int reportesMigrados = 0;
            
            for (ReporteIncidencia reporte : reportesExistentes) {
                // Verificar si ya existe una notificación para este reporte
                boolean existeNotificacion = notificacionesRepository
                    .findByTipoNotificacionAndIdReporte("REPORTE_INCIDENCIA", reporte.getIdReporte())
                    .isPresent();
                
                if (!existeNotificacion) {
                    // Crear notificación para este reporte
                    Notificaciones notificacion = new Notificaciones();
                    notificacion.setTipoNotificacion("REPORTE_INCIDENCIA");
                    notificacion.setIdReporte(reporte.getIdReporte());
                    notificacion.setEstado(reporte.getEstado());
                    notificacion.setTipoIncidencia(reporte.getTipoIncidencia());
                    notificacion.setDetalleProblema(reporte.getDetalleProblema());
                    notificacion.setFechaHoraReporte(reporte.getFechaHora());
                    
                    // Obtener nombre del elemento afectado
                    if (reporte.getBien() != null) {
                        String elementoAfectado = reporte.getBien().getCodigoBien() != null && !reporte.getBien().getCodigoBien().isEmpty() 
                            ? reporte.getBien().getCodigoBien() 
                            : (reporte.getBien().getNombreBien() != null && !reporte.getBien().getNombreBien().isEmpty() 
                                ? reporte.getBien().getNombreBien() 
                                : reporte.getBien().getClaseBien());
                        notificacion.setElementoAfectado(elementoAfectado != null ? elementoAfectado : "Bien #" + reporte.getBien().getIdBien());
                    } else {
                        notificacion.setElementoAfectado("No disponible");
                    }
                    
                    // Obtener nombre del usuario que reporta
                    if (reporte.getUsuario() != null) {
                        String reportadoPor = reporte.getUsuario().getNombresCompletos() != null && !reporte.getUsuario().getNombresCompletos().isEmpty() 
                            ? reporte.getUsuario().getNombresCompletos() 
                            : reporte.getUsuario().getCedula();
                        notificacion.setReportadoPor(reportadoPor != null ? reportadoPor : "Usuario #" + reporte.getUsuario().getIdUsuario());
                    } else {
                        notificacion.setReportadoPor("No disponible");
                    }
                    
                    notificacion.setLeida(false);
                    
                    notificacionesRepository.save(notificacion);
                    reportesMigrados++;
                    System.out.println("   ✅ Notificación creada para reporte ID: " + reporte.getIdReporte());
                }
            }
            
            if (reportesMigrados > 0) {
                System.out.println("✅ " + reportesMigrados + " reporte(s) migrado(s) a notificaciones");
            } else {
                System.out.println("ℹ️  Todos los reportes ya tienen sus notificaciones correspondientes");
            }
            
        } catch (Exception e) {
            System.err.println("❌ Error al migrar reportes a notificaciones: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Actualiza la estructura de la tabla usuarios:
     * - email -> cedula
     * - nombre -> nombres_completos
     * - contraseña opcional (sin NOT NULL)
     */
    private void actualizarTablaUsuarios() {
        try {
            System.out.println("🔧 Actualizando tabla usuarios...");
            if (columnaExiste("usuarios", "email") && !columnaExiste("usuarios", "cedula")) {
                jdbcTemplate.execute("ALTER TABLE public.usuarios RENAME COLUMN email TO cedula");
                System.out.println("   ✅ Columna 'email' renombrada a 'cedula'");
            } else {
                System.out.println("   ℹ️  Columna 'email' ya migrada o no existe");
            }
            if (columnaExiste("usuarios", "nombre") && !columnaExiste("usuarios", "nombres_completos")) {
                jdbcTemplate.execute("ALTER TABLE public.usuarios RENAME COLUMN nombre TO nombres_completos");
                System.out.println("   ✅ Columna 'nombre' renombrada a 'nombres_completos'");
            } else {
                System.out.println("   ℹ️  Columna 'nombre' ya migrada o no existe");
            }
            try {
                jdbcTemplate.execute("ALTER TABLE public.usuarios ALTER COLUMN cedula TYPE VARCHAR(20)");
                System.out.println("   ✅ Columna 'cedula' ajustada a VARCHAR(20)");
            } catch (Exception e) {
                System.out.println("   ℹ️  No se pudo ajustar 'cedula': " + e.getMessage());
            }
            try {
                jdbcTemplate.execute("ALTER TABLE public.usuarios ALTER COLUMN nombres_completos TYPE VARCHAR(255)");
                System.out.println("   ✅ Columna 'nombres_completos' ajustada a VARCHAR(255)");
            } catch (Exception e) {
                System.out.println("   ℹ️  No se pudo ajustar 'nombres_completos': " + e.getMessage());
            }
            try {
                jdbcTemplate.execute("ALTER TABLE public.usuarios ALTER COLUMN \"contraseña\" DROP NOT NULL");
                System.out.println("   ✅ Columna 'contraseña' ahora permite NULL");
            } catch (Exception e) {
                System.out.println("   ℹ️  No se pudo ajustar 'contraseña': " + e.getMessage());
            }
            try {
                jdbcTemplate.execute("ALTER TABLE public.usuarios ALTER COLUMN id_rol DROP NOT NULL");
                System.out.println("   ✅ Columna 'id_rol' ahora permite NULL");
            } catch (Exception e) {
                System.out.println("   ℹ️  No se pudo ajustar 'id_rol': " + e.getMessage());
            }
        } catch (Exception e) {
            System.err.println("❌ Error al actualizar tabla usuarios: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Carga usuarios desde un CSV en resources/data/usuarios_base.csv.
     * Columnas esperadas: cedula y nombres_completos (o apellidos_y_nombres).
     * Opcional: rol (si no existe, se usa "Docente").
     */
    private void cargarUsuariosDesdeCsv() {
        try {
            System.out.println("🔧 Cargando usuarios base desde CSV...");
            ClassPathResource resource = new ClassPathResource("data/usuarios_base.csv");
            if (!resource.exists()) {
                System.out.println("⚠️  Archivo data/usuarios_base.csv no encontrado, se omite carga.");
                return;
            }

            try (BufferedReader reader = new BufferedReader(
                    new InputStreamReader(resource.getInputStream(), StandardCharsets.UTF_8))) {
                String header = reader.readLine();
                if (header == null) {
                    System.out.println("⚠️  Archivo CSV vacío");
                    return;
                }

                String delimiter = header.contains(";") ? ";" : ",";
                String[] headerParts = header.split(java.util.regex.Pattern.quote(delimiter), -1);
                Map<String, Integer> indices = new HashMap<>();
                for (int i = 0; i < headerParts.length; i++) {
                    String key = normalizarColumna(headerParts[i]);
                    indices.put(key, i);
                }

                int idxCedula = buscarIndice(indices, "cedula");
                int idxNombre = buscarIndice(indices, "nombres_completos", "apellidos_y_nombres", "nombres", "nombre");
                int idxRol = buscarIndice(indices, "rol");
                int idxPassword = buscarIndice(indices, "password", "contrasena", "contraseña", "clave");

                if (idxCedula < 0 || idxNombre < 0) {
                    System.out.println("⚠️  El CSV debe tener columnas 'cedula' y 'nombres_completos' (o 'apellidos_y_nombres')");
                    return;
                }

                Map<String, Rol> rolesCache = new HashMap<>();
                Set<String> cedulasProcesadas = new HashSet<>();
                int insertados = 0;

                String line;
                while ((line = reader.readLine()) != null) {
                    if (line.trim().isEmpty()) {
                        continue;
                    }
                    String[] parts = line.split(java.util.regex.Pattern.quote(delimiter), -1);
                    String cedula = obtenerValor(parts, idxCedula);
                    String nombres = obtenerValor(parts, idxNombre);
                    String rolNombre = idxRol >= 0 ? obtenerValor(parts, idxRol) : "";
                    String password = idxPassword >= 0 ? obtenerValor(parts, idxPassword) : "";

                    if (cedula.isEmpty() || nombres.isEmpty()) {
                        continue;
                    }
                    if (!cedulasProcesadas.add(cedula)) {
                        continue;
                    }
                    if (usuarioRepository.existsByCedula(cedula)) {
                        continue;
                    }

                    Rol rolAsignado = null;
                    if (!rolNombre.isEmpty()) {
                        String keyRol = rolNombre.trim().toLowerCase();
                        rolAsignado = rolesCache.computeIfAbsent(keyRol, key -> rolRepository.findAll().stream()
                                .filter(r -> key.equalsIgnoreCase(r.getNombre()))
                                .findFirst()
                                .orElse(null));
                    }

                    Usuario usuario = new Usuario();
                    usuario.setCedula(cedula);
                    usuario.setNombresCompletos(nombres);
                    usuario.setRol(rolAsignado);
                    usuario.setEstado(true);
                    if (!password.isEmpty()) {
                        usuario.setContraseña(passwordEncoder.encode(password));
                    } else {
                        usuario.setContraseña(null);
                    }
                    usuarioRepository.save(usuario);
                    insertados++;
                }

                System.out.println("✅ Usuarios cargados: " + insertados);
            }
        } catch (Exception e) {
            System.err.println("❌ Error al cargar usuarios desde CSV: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String normalizarColumna(String columna) {
        if (columna == null) {
            return "";
        }
        return columna.trim()
                .toLowerCase()
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace("ñ", "n")
                .replace(" ", "_");
    }

    private int buscarIndice(Map<String, Integer> indices, String... posibles) {
        for (String posible : posibles) {
            Integer idx = indices.get(posible);
            if (idx != null) {
                return idx;
            }
        }
        return -1;
    }

    private String obtenerValor(String[] parts, int idx) {
        if (idx < 0 || idx >= parts.length) {
            return "";
        }
        return parts[idx] != null ? parts[idx].trim() : "";
    }

    private boolean columnaExiste(String tabla, String columna) {
        try {
            Integer count = jdbcTemplate.queryForObject(
                    "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema = 'public' AND table_name = ? AND column_name = ?",
                    Integer.class,
                    tabla,
                    columna
            );
            return count != null && count > 0;
        } catch (Exception e) {
            System.out.println("   ℹ️  No se pudo verificar columna '" + columna + "' en " + tabla + ": " + e.getMessage());
            return false;
        }
    }
    
    /**
     * Elimina las columnas antiguas que no se usan para reportes de incidencias
     */
    private void limpiarColumnasAntiguas() {
        try {
            System.out.println("🔧 Eliminando columnas antiguas de notificaciones que están en null...");
            
            // Lista de columnas a eliminar (solo para reportes de incidencias)
            String[] columnasAntiguas = {
                "bienes_constatados",
                "bienes_no_identificados",
                "cantidad_olympo_2015",
                "cantidad_olympo_senescyt",
                "cantidad_otro_sistema",
                "cuenta_contable",
                "nombre_cuenta",
                "sistema_control",
                "total",
                "total_sistema",
                "control"
            };
            
            for (String columna : columnasAntiguas) {
                try {
                    // Verificar si la columna existe antes de eliminarla
                    String checkColumnQuery = "SELECT COUNT(*) FROM information_schema.columns " +
                        "WHERE table_schema = 'public' AND table_name = 'notificaciones' AND column_name = ?";
                    
                    Integer count = jdbcTemplate.queryForObject(checkColumnQuery, Integer.class, columna);
                    
                    if (count != null && count > 0) {
                        // Eliminar la columna
                        jdbcTemplate.execute("ALTER TABLE public.notificaciones DROP COLUMN IF EXISTS " + columna + " CASCADE");
                        System.out.println("   ✅ Columna '" + columna + "' eliminada");
                    } else {
                        System.out.println("   ℹ️  Columna '" + columna + "' no existe");
                    }
                } catch (Exception e) {
                    System.out.println("   ⚠️  Error al eliminar columna '" + columna + "': " + e.getMessage());
                }
            }
            
            System.out.println("✅ Limpieza de columnas completada");
            
        } catch (Exception e) {
            System.err.println("❌ Error al limpiar columnas antiguas: " + e.getMessage());
            e.printStackTrace();
        }
    }
}

