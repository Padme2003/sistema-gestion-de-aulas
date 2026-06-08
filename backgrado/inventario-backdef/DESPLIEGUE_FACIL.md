# 🎯 DESPLIEGUE SÚPER FÁCIL - Paso a Paso

Te voy a guiar con el método MÁS SIMPLE posible. Solo sigue los pasos uno por uno.

---

## 🚀 MÉTODO MÁS FÁCIL: Render con Docker (Ya está todo preparado)

### ✅ Lo que YA tienes listo:
- ✅ Dockerfile creado
- ✅ Todo configurado en tu código
- ✅ Solo necesitas seguir estos pasos

---

## PASO 1: Subir Dockerfile a GitHub (2 minutos)

Abre PowerShell o Terminal en la carpeta de tu proyecto y ejecuta:

```powershell
cd inventario-backdef
git add Dockerfile .dockerignore
git commit -m "Agregar Dockerfile para Render"
git push
```

**¡Listo!** Tu Dockerfile ya está en GitHub.

---

## PASO 2: Ir a Render y crear servicio (5 minutos)

### 2.1. Ve a Render
1. Abre: https://render.com
2. Si no tienes cuenta, créala (es gratis, con GitHub)

### 2.2. Crear nuevo servicio
1. Haz clic en **"New +"** (botón verde arriba)
2. Selecciona **"Web Service"**
3. Conecta tu repositorio:
   - Si aparece una lista, busca `inventario-backdef`
   - Selecciónalo
   - Haz clic en **"Connect"**

### 2.3. Configurar (TODO lo que necesitas llenar):

| Campo | Valor Exacto |
|-------|--------------|
| **Name** | `inventario-backend` |
| **Idioma/Language** | `Docker` (o "Estibador") |
| **Dockerfile Path** | `inventario-backdef/Dockerfile` |
| **Rama/Branch** | `principal` (o `main` si es tu rama) |
| **Directorio raíz/Root Directory** | `inventario-backdef` |
| **Plan** | `Free` |

### 2.4. NO toques nada más, solo haz clic en:
**"Create Web Service"** o **"Crear servicio web"**

**¡Listo!** Render empezará a construir tu aplicación.

---

## PASO 3: Agregar Variables de Entorno (5 minutos)

### 3.1. Espera a que termine el primer build
- Puede tardar 5-10 minutos la primera vez
- Ve a la pestaña **"Logs"** para ver el progreso

### 3.2. Cuando termine (o incluso antes), ve a "Environment":

1. En Render, ve a tu servicio
2. Haz clic en la pestaña **"Environment"**
3. Haz clic en **"Add Environment Variable"**
4. Agrega estas variables UNA POR UNA:

#### Variable 1: SPRING_PROFILES_ACTIVE
- **Key**: `SPRING_PROFILES_ACTIVE`
- **Value**: `prod`
- Haz clic en **"Save Changes"**

#### Variable 2: SPRING_DATASOURCE_URL
- **Key**: `SPRING_DATASOURCE_URL`
- **Value**: La URL de tu base de datos PostgreSQL
  - Si usas Supabase: `jdbc:postgresql://db.xxxxx.supabase.co:5432/postgres?sslmode=require`
  - Si usas Neon: Similar formato
- Haz clic en **"Save Changes"**

#### Variable 3: SPRING_DATASOURCE_USERNAME
- **Key**: `SPRING_DATASOURCE_USERNAME`
- **Value**: `postgres`
- Haz clic en **"Save Changes"**

#### Variable 4: SPRING_DATASOURCE_PASSWORD
- **Key**: `SPRING_DATASOURCE_PASSWORD`
- **Value**: Tu contraseña de la base de datos
- Haz clic en **"Save Changes"**

#### Variable 5: JWT_SECRET
- **Key**: `JWT_SECRET`
- **Value**: Genera una clave aquí: https://generate-secret.vercel.app/32
  - Copia el texto que genera
  - Pégalo como valor
- Haz clic en **"Save Changes"**

#### Variable 6: CORS_ALLOWED_ORIGINS
- **Key**: `CORS_ALLOWED_ORIGINS`
- **Value**: `*` (un asterisco, temporalmente)
- Haz clic en **"Save Changes"**

### 3.3. Reiniciar servicio
- Después de agregar todas las variables
- Ve a la pestaña **"Manual Deploy"**
- Haz clic en **"Clear build cache & deploy"**

---

## PASO 4: Crear Base de Datos PostgreSQL (5 minutos)

### Opción A: Supabase (Recomendado - Gratis permanente)

1. Ve a: https://supabase.com
2. **"Start your project"**
3. Inicia sesión con GitHub
4. **"New Project"**:
   - Name: `inventario-yavirac`
   - Password: Crea una contraseña (GUÁRDALA)
   - Region: La más cercana
5. Espera 2 minutos
6. Ve a **Settings** → **Database**
7. Busca **"Connection string"** o **"Connection info"**
8. Copia:
   - **Host**: `db.xxxxx.supabase.co`
   - **Port**: `5432`
   - **Database**: `postgres`
   - **User**: `postgres`
   - **Password**: La que creaste

**Usa estos datos en la Variable 2 de arriba**

---

## PASO 5: ¡PROBAR! (2 minutos)

1. En Render, ve a tu servicio
2. Copia la **URL** que aparece arriba (algo como: `https://inventario-backend.onrender.com`)
3. Abre esa URL en tu navegador
4. Deberías ver algo (aunque sea un error, significa que está funcionando)

---

## ✅ CHECKLIST RÁPIDO

Sigue este orden:

- [ ] Paso 1: Subí Dockerfile a GitHub
- [ ] Paso 2: Creé servicio en Render (con Docker)
- [ ] Paso 3: Agregué las 6 variables de entorno
- [ ] Paso 4: Creé base de datos en Supabase
- [ ] Paso 5: Probé la URL y funciona

---

## 🆘 SI ALGO FALLA

### Error: "Build failed"
- Ve a **"Logs"** en Render
- Copia el error y me lo pasas

### Error: "Cannot connect to database"
- Verifica que la URL de la base de datos tenga `?sslmode=require` al final
- Verifica que la contraseña sea correcta

### La aplicación "duerme"
- Es normal en el plan gratis
- La primera petición después de dormir tarda ~30 segundos

---

## 🎉 ¡ESO ES TODO!

Solo son 5 pasos simples. Si te atascas en alguno, dime en cuál y te ayudo específicamente.

**¿Quieres que empecemos por el Paso 1? Es solo ejecutar 3 comandos en PowerShell.**

