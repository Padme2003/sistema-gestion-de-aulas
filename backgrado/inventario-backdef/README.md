# Backend de Inventario - Spring Boot

Backend completo para sistema de inventario con autenticación JWT, CRUDs completos y conexión a PostgreSQL.

## Características

- ✅ Autenticación con JWT
- ✅ Seguridad con BCrypt para contraseñas
- ✅ CRUDs completos para todas las entidades
- ✅ Generación automática de tablas al iniciar la aplicación
- ✅ Conexión a PostgreSQL (pgAdmin4)
- ✅ CORS configurado

## Estructura de Base de Datos

### Entidades Principales

1. **Rol**: Roles de usuario del sistema
2. **Usuario**: Usuarios del sistema con autenticación
3. **Docente**: Docentes (sin teléfono, con REGIMEN y OBSERVACIONES)
4. **Categoria**: Categorías para clasificar bienes
5. **Aula**: Aulas del instituto
6. **Asignacion**: Asignaciones de aulas a usuarios
7. **Bienes**: Bienes unificados (datos de INVENTARIO BIEN MUEBLES y BIENES SECAP)
8. **Solicitudes**: Solicitudes de bienes
9. **Notificaciones**: Notificaciones/Resúmenes del sistema

## Configuración

### 1. Base de Datos PostgreSQL

Edita el archivo `src/main/resources/application.properties` y configura:

```properties
# Cambia estos valores según tu configuración
spring.datasource.url=jdbc:postgresql://localhost:5432/inventario_db
spring.datasource.username=postgres
spring.datasource.password=tu_contraseña
```

**Pasos para crear la base de datos:**

1. Abre pgAdmin4
2. Crea una nueva base de datos llamada `inventario_db`
3. Las tablas se crearán automáticamente al ejecutar la aplicación (gracias a `spring.jpa.hibernate.ddl-auto=update`)

### 2. Clave Secreta JWT

La clave secreta JWT está configurada en `application.properties`:

```properties
jwt.secret=bWlDbGF2ZVNlY3JldGFTdXBlclNlZ3VyYVBhcmFKV1QxMjM0NTY3ODkwMTIzNDU2Nzg5MDEyMzQ1Njc4OTAxMjM0NTY3ODkw
```

**⚠️ IMPORTANTE:** En producción, genera una clave secreta segura:

```bash
openssl rand -base64 32
```

Y reemplaza el valor de `jwt.secret` en `application.properties`.

### 3. Puerto del Servidor

Por defecto, la aplicación corre en el puerto `8080`. Puedes cambiarlo en `application.properties`:

```properties
server.port=8080
```

## Ejecución

### Requisitos Previos

- Java 17 o superior
- Maven 3.6+
- PostgreSQL instalado y corriendo
- Base de datos `inventario_db` creada en PostgreSQL

### Ejecutar la Aplicación

```bash
# Compilar y ejecutar
mvn spring-boot:run

# O compilar primero y luego ejecutar el JAR
mvn clean package
java -jar target/inventario-backend-0.0.1-SNAPSHOT.jar
```

La aplicación estará disponible en: `http://localhost:8080`

## Endpoints de la API

### Autenticación

- `POST /api/auth/login` - Iniciar sesión
- `POST /api/auth/register` - Registrar nuevo usuario

### Roles

- `GET /api/roles` - Listar todos los roles
- `GET /api/roles/{id}` - Obtener rol por ID
- `POST /api/roles` - Crear nuevo rol
- `PUT /api/roles/{id}` - Actualizar rol
- `DELETE /api/roles/{id}` - Eliminar rol

### Usuarios

- `GET /api/usuarios` - Listar todos los usuarios
- `GET /api/usuarios/{id}` - Obtener usuario por ID
- `POST /api/usuarios` - Crear nuevo usuario
- `PUT /api/usuarios/{id}` - Actualizar usuario
- `DELETE /api/usuarios/{id}` - Eliminar usuario

### Docentes

- `GET /api/docentes` - Listar todos los docentes
- `GET /api/docentes/{id}` - Obtener docente por ID
- `GET /api/docentes/cedula/{cedula}` - Obtener docente por cédula
- `POST /api/docentes` - Crear nuevo docente
- `PUT /api/docentes/{id}` - Actualizar docente
- `DELETE /api/docentes/{id}` - Eliminar docente

### Categorías

- `GET /api/categorias` - Listar todas las categorías
- `GET /api/categorias/{id}` - Obtener categoría por ID
- `POST /api/categorias` - Crear nueva categoría
- `PUT /api/categorias/{id}` - Actualizar categoría
- `DELETE /api/categorias/{id}` - Eliminar categoría

### Aulas

- `GET /api/aulas` - Listar todas las aulas
- `GET /api/aulas/{id}` - Obtener aula por ID
- `POST /api/aulas` - Crear nueva aula
- `PUT /api/aulas/{id}` - Actualizar aula
- `DELETE /api/aulas/{id}` - Eliminar aula

### Asignaciones

- `GET /api/asignaciones` - Listar todas las asignaciones
- `GET /api/asignaciones/{id}` - Obtener asignación por ID
- `POST /api/asignaciones` - Crear nueva asignación
- `PUT /api/asignaciones/{id}` - Actualizar asignación
- `DELETE /api/asignaciones/{id}` - Eliminar asignación

### Bienes

- `GET /api/bienes` - Listar todos los bienes
- `GET /api/bienes/{id}` - Obtener bien por ID
- `GET /api/bienes/codigo/{codigoBien}` - Obtener bien por código
- `POST /api/bienes` - Crear nuevo bien
- `PUT /api/bienes/{id}` - Actualizar bien
- `DELETE /api/bienes/{id}` - Eliminar bien

### Solicitudes

- `GET /api/solicitudes` - Listar todas las solicitudes
- `GET /api/solicitudes/{id}` - Obtener solicitud por ID
- `POST /api/solicitudes` - Crear nueva solicitud
- `PUT /api/solicitudes/{id}` - Actualizar solicitud
- `DELETE /api/solicitudes/{id}` - Eliminar solicitud

### Notificaciones

- `GET /api/notificaciones` - Listar todas las notificaciones
- `GET /api/notificaciones/no-leidas` - Listar notificaciones no leídas
- `GET /api/notificaciones/tipo/{tipo}` - Listar notificaciones por tipo
- `GET /api/notificaciones/{id}` - Obtener notificación por ID
- `POST /api/notificaciones` - Crear nueva notificación
- `PUT /api/notificaciones/{id}` - Actualizar notificación
- `PUT /api/notificaciones/{id}/marcar-leida` - Marcar notificación como leída
- `DELETE /api/notificaciones/{id}` - Eliminar notificación

## Autenticación JWT

Para acceder a los endpoints protegidos, incluye el token JWT en el header:

```
Authorization: Bearer {token}
```

### Ejemplo de Login

```json
POST /api/auth/login
{
  "email": "usuario@example.com",
  "password": "contraseña123"
}
```

Respuesta:
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "email": "usuario@example.com",
  "rol": "ADMIN"
}
```

## Notas Importantes

1. **Generación Automática de Tablas**: Las tablas se crean automáticamente al iniciar la aplicación gracias a `spring.jpa.hibernate.ddl-auto=update`. En producción, considera usar `validate` o `none`.

2. **Seguridad**: 
   - Las contraseñas se encriptan automáticamente con BCrypt
   - El token JWT expira después de 24 horas (86400000 ms)

3. **CORS**: Está configurado para permitir todas las solicitudes. En producción, restringe los orígenes permitidos.

4. **Validaciones**:
   - La cédula de docente debe ser única
   - El email de usuario debe ser único
   - El código de bien debe ser único

## Estructura del Proyecto

```
src/main/java/com/yavirac/inventario_backend/
├── controller/      # Controladores REST
├── dto/            # Data Transfer Objects
├── entity/         # Entidades JPA
├── repository/     # Repositorios JPA
├── security/       # Configuración de seguridad y JWT
└── service/        # Lógica de negocio
```

## Desarrollo

Para desarrollo, puedes usar:

```bash
# Ver logs SQL
# Ya está configurado en application.properties:
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

## Soporte

Si encuentras algún problema, verifica:

1. ✅ PostgreSQL está corriendo
2. ✅ La base de datos `inventario_db` existe
3. ✅ Las credenciales en `application.properties` son correctas
4. ✅ El puerto 8080 no está en uso
5. ✅ Java 17+ está instalado

