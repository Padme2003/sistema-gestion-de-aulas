# 🚀 Guía Completa de Despliegue en Render

Esta guía te llevará paso a paso para desplegar tu backend Spring Boot en Render y conectarlo con una base de datos PostgreSQL en la nube.

---

## 📋 PREREQUISITOS

- ✅ Cuenta de GitHub (gratis)
- ✅ Código subido a un repositorio de GitHub
- ✅ Cuenta en Render (gratis)
- ✅ Cuenta en Supabase o Neon (para PostgreSQL gratuito)

---

## PASO 1: Preparar el Repositorio en GitHub

### 1.1. Si ya tienes tu código en GitHub
- ✅ Salta al Paso 2

### 1.2. Si NO tienes tu código en GitHub

```bash
# En la carpeta de tu proyecto
cd inventario-backdef

# Inicializar git (si no lo has hecho)
git init

# Agregar todos los archivos
git add .

# Hacer commit
git commit -m "Preparando para despliegue en Render"

# Conectar con GitHub y subir
# (Reemplaza TU_USUARIO y TU_REPOSITORIO con tus datos)
git remote add origin https://github.com/TU_USUARIO/TU_REPOSITORIO.git
git branch -M main
git push -u origin main
```

---

## PASO 2: Crear Base de Datos PostgreSQL en la Nube

Tienes 3 opciones gratuitas:

### Opción A: Supabase (Recomendado) 🌟

1. Ve a https://supabase.com
2. Haz clic en "Start your project"
3. Inicia sesión con GitHub
4. Crea un nuevo proyecto:
   - **Name**: `inventario-yavirac`
   - **Database Password**: Genera una contraseña segura (GUÁRDALA)
   - **Region**: Elige la más cercana (por ejemplo, South America)
   - **Pricing Plan**: Free
5. Espera ~2 minutos a que se cree
6. Ve a **Settings** → **Database**
7. Copia los siguientes datos (los necesitarás después):
   - **Host**: Algo como `db.xxxxx.supabase.co`
   - **Port**: `5432`
   - **Database name**: `postgres`
   - **User**: `postgres`
   - **Password**: La que creaste
   - **Connection string**: Lo verás más abajo

### Opción B: Neon 🌟

1. Ve a https://neon.tech
2. Haz clic en "Sign Up" y crea cuenta con GitHub
3. Crea un nuevo proyecto:
   - **Project name**: `inventario-yavirac`
   - **Region**: Elige la más cercana
4. Una vez creado, copia:
   - La **Connection String** completa
   - O los datos individuales: Host, Database, User, Password

### Opción C: Render PostgreSQL (90 días gratis)

1. En Render, ve a **Dashboard** → **New** → **PostgreSQL**
2. Configura:
   - **Name**: `inventario-db`
   - **Database**: `inventario_yavirac1`
   - **User**: Se genera automáticamente
   - **Region**: Elige la más cercana
   - **PostgreSQL Version**: 16
   - **Plan**: Free
3. Copia los datos de conexión que te muestra

---

## PASO 3: Crear Aplicación Web en Render

### 3.1. Crear cuenta en Render

1. Ve a https://render.com
2. Haz clic en "Get Started for Free"
3. Inicia sesión con GitHub

### 3.2. Crear nuevo servicio Web

1. En el Dashboard, haz clic en **"New +"** → **"Web Service"**
2. Conecta tu repositorio de GitHub:
   - Selecciona tu cuenta de GitHub
   - Busca y selecciona tu repositorio
   - Haz clic en **"Connect"**

### 3.3. Configurar el servicio

Completa el formulario con estos datos:

- **Name**: `inventario-backend` (o el nombre que prefieras)
- **Region**: Elige la más cercana a tu ubicación
- **Branch**: `main` (o `master` si es tu rama principal)
- **Root Directory**: `inventario-backdef` (si tu proyecto está en una subcarpeta)
- **Runtime**: `Java`
- **Build Command**: 
  ```
  ./mvnw clean package -DskipTests
  ```
- **Start Command**: 
  ```
  java -jar target/*.jar --spring.profiles.active=prod
  ```
- **Plan**: **Free** (para empezar)

### 3.4. Configurar Variables de Entorno

Haz clic en **"Advanced"** → **"Add Environment Variable"** y agrega:

#### Variables Requeridas:

1. **SPRING_PROFILES_ACTIVE**
   - Valor: `prod`

2. **SPRING_DATASOURCE_URL**
   - Para Supabase/Neon, formato:
     ```
     jdbc:postgresql://TU_HOST:5432/postgres?sslmode=require
     ```
   - Ejemplo real de Supabase:
     ```
     jdbc:postgresql://db.abcdefghijklmnop.supabase.co:5432/postgres?sslmode=require
     ```

3. **SPRING_DATASOURCE_USERNAME**
   - Valor: `postgres` (generalmente)

4. **SPRING_DATASOURCE_PASSWORD**
   - Valor: La contraseña que creaste en Supabase/Neon

5. **JWT_SECRET**
   - Genera una clave segura:
     ```bash
     # En Windows PowerShell:
     [Convert]::ToBase64String((1..32 | ForEach-Object { Get-Random -Minimum 0 -Maximum 256 }))
     ```
   - O usa un generador online: https://generate-secret.vercel.app/32
   - Copia el valor generado (debe tener al menos 32 caracteres)

6. **JWT_EXPIRATION**
   - Valor: `1800000` (30 minutos en milisegundos)

7. **CORS_ALLOWED_ORIGINS**
   - Si aún no tienes frontend desplegado, usa temporalmente:
     ```
     *
     ```
   - Cuando despliegues tu frontend, cámbialo a:
     ```
     https://tu-frontend.com,https://www.tu-frontend.com
     ```

8. **SERVER_PORT** (Opcional)
   - Render asigna el puerto automáticamente, pero puedes poner:
   - Valor: `8080`

### 3.5. Crear el servicio

1. Haz clic en **"Create Web Service"**
2. Render comenzará a construir tu aplicación (tarda ~5-10 minutos la primera vez)

---

## PASO 4: Verificar que todo funciona

### 4.1. Ver los logs

1. En Render, ve a tu servicio
2. Haz clic en la pestaña **"Logs"**
3. Busca mensajes como:
   - ✅ "Started InventarioBackendApplication"
   - ✅ "Tomcat started on port(s)"
   - ⚠️ Si ves errores, léelos y corrígelos

### 4.2. Obtener la URL de tu API

1. En Render, ve a tu servicio
2. Copia la URL que aparece en la parte superior (algo como):
   ```
   https://inventario-backend.onrender.com
   ```

### 4.3. Probar la API

Abre tu navegador o usa Postman/Thunder Client y prueba:

```
GET https://tu-url.onrender.com/api/auth/login
```

O prueba desde PowerShell:
```powershell
Invoke-WebRequest -Uri "https://tu-url.onrender.com/api/auth/login" -Method POST -ContentType "application/json" -Body '{"email":"edison@gmail.com","password":"admin123"}'
```

---

## PASO 5: Importar datos a la base de datos

### 5.1. Conectar a PostgreSQL desde tu computadora

#### Opción A: Usando pgAdmin

1. Abre pgAdmin
2. Crea una nueva conexión:
   - **Name**: `Supabase Inventario`
   - **Host**: El host de Supabase/Neon
   - **Port**: `5432`
   - **Database**: `postgres`
   - **Username**: `postgres`
   - **Password**: Tu contraseña
   - **SSL Mode**: `Require`
3. Conecta y ejecuta tus scripts SQL para crear las tablas

#### Opción B: Usando psql (línea de comandos)

```bash
# Instala PostgreSQL client si no lo tienes
# Luego:
psql "postgresql://postgres:TU_PASSWORD@TU_HOST:5432/postgres?sslmode=require"
```

### 5.2. Ejecutar scripts SQL

Ejecuta todos los scripts necesarios para crear tus tablas:
- `reportes_incidencias`
- `notificaciones` (con las columnas actualizadas)
- `usuarios`, `rol`, `bienes`, `aulas`, etc.

### 5.3. Crear usuario administrador

Tu aplicación creará automáticamente el usuario admin si ejecutas la app, o puedes insertarlo manualmente.

---

## PASO 6: Configurar el Frontend

### 6.1. Actualizar URL del backend

En tu frontend, cambia la URL base del API:

**Antes:**
```typescript
const API_URL = 'http://localhost:8080/api';
```

**Después:**
```typescript
const API_URL = 'https://tu-backend.onrender.com/api';
```

### 6.2. Actualizar CORS en Render

Una vez que despliegues tu frontend:

1. Ve a tu servicio en Render
2. **Environment** → Edita `CORS_ALLOWED_ORIGINS`
3. Cambia a:
   ```
   https://tu-frontend.com,https://www.tu-frontend.com
   ```
4. Guarda y Render reiniciará automáticamente

---

## PASO 7: Monitoreo y Mantenimiento

### 7.1. Ver logs en tiempo real

- Render → Tu servicio → **Logs**
- Ahí verás todos los errores y mensajes

### 7.2. Reiniciar la aplicación

- Render → Tu servicio → **Manual Deploy** → **Clear build cache & deploy**

### 7.3. Actualizar código

1. Haz cambios en tu código
2. Haz commit y push a GitHub:
   ```bash
   git add .
   git commit -m "Mi cambio"
   git push
   ```
3. Render detectará los cambios automáticamente y redesplegará

---

## 🔒 SEGURIDAD IMPORTANTE

### ✅ Lo que YA está configurado:

- ✅ JWT habilitado (rutas protegidas)
- ✅ Solo `/api/auth/**` es público
- ✅ CORS configurado
- ✅ Variables de entorno para datos sensibles

### ⚠️ Qué hacer AHORA:

1. **Cambia el JWT_SECRET** inmediatamente después del primer despliegue
2. **No compartas** tus variables de entorno
3. **Usa HTTPS** siempre (Render lo hace automáticamente)
4. **Mantén** tus contraseñas de base de datos seguras

---

## 🐛 SOLUCIÓN DE PROBLEMAS

### Error: "Cannot connect to database"

**Solución:**
- Verifica que `SPRING_DATASOURCE_URL` tenga `?sslmode=require` al final
- Verifica que la contraseña sea correcta
- Asegúrate de que la base de datos esté activa

### Error: "Build failed"

**Solución:**
- Verifica los logs en Render
- Asegúrate de que `pom.xml` esté correcto
- Verifica que Maven pueda descargar las dependencias

### Error: "Application failed to start"

**Solución:**
- Ve a los logs en Render
- Busca errores específicos
- Verifica que todas las variables de entorno estén configuradas

### La app "duerme" después de inactividad

**Solución:**
- Esto es normal en el plan gratuito de Render
- La primera petición después de dormir tarda ~30 segundos
- Para evitar esto, necesitarías un plan de pago

---

## 📞 PRÓXIMOS PASOS

1. ✅ Despliega el backend en Render
2. ✅ Configura la base de datos
3. ✅ Prueba los endpoints
4. ✅ Despliega el frontend (Netlify/Vercel)
5. ✅ Conecta frontend con backend
6. ✅ Prueba todo el flujo completo

---

## 📝 CHECKLIST FINAL

- [ ] Código subido a GitHub
- [ ] Base de datos PostgreSQL creada (Supabase/Neon)
- [ ] Servicio creado en Render
- [ ] Todas las variables de entorno configuradas
- [ ] Aplicación desplegada y funcionando
- [ ] Tablas creadas en la base de datos
- [ ] URL de la API funcionando
- [ ] Frontend configurado con la nueva URL
- [ ] CORS configurado correctamente
- [ ] Todo probado y funcionando

---

**¡Éxito con tu despliegue! 🎉**

Si tienes problemas, revisa los logs en Render o consulta la documentación oficial:
- Render: https://render.com/docs
- Supabase: https://supabase.com/docs

