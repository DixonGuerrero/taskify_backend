# Taskify Backend

![Taskify Logo](./assets/Logo.webp)

Taskify es una API REST para gestión de proyectos y tareas en equipo: usuarios y roles, proyectos con invitación por código, tareas con prioridad/estado/archivos adjuntos, imágenes de perfil y de proyecto, y notificaciones en tiempo real vía WebSocket.

Está construida con **arquitectura hexagonal (puertos y adaptadores)**, lo que la hace deliberadamente fácil de adaptar a distintos entornos: el mismo *core* de negocio puede correr con distintos backends de almacenamiento de archivos, distintos motores/instancias de base de datos, o expuesto detrás de HTTP con proveedores de login distintos — sin tocar la lógica de dominio.

## Tabla de contenidos

- [Qué hace la aplicación](#qué-hace-la-aplicación)
- [Arquitectura hexagonal](#arquitectura-hexagonal)
- [Desacoplamiento en la práctica](#desacoplamiento-en-la-práctica)
- [Stack tecnológico](#stack-tecnológico)
- [Cómo ejecutar el proyecto](#cómo-ejecutar-el-proyecto)
- [Configurar la base de datos](#configurar-la-base-de-datos)
- [Configurar el almacenamiento de archivos](#configurar-el-almacenamiento-de-archivos)
- [Autenticación](#autenticación)
- [Mapa de endpoints](#mapa-de-endpoints)
- [Docker](#docker)
- [Tests](#tests)
- [Consideraciones y limitaciones conocidas](#consideraciones-y-limitaciones-conocidas)
- [Licencia](#licencia)

## Qué hace la aplicación

- **Autenticación:** registro/login con usuario y contraseña (JWT), o login social con **Google** y **GitHub** (OAuth2). El token viaja como cookie `httpOnly` para clientes web o como header `Authorization: Bearer` para clientes de escritorio/móvil (ver [Autenticación](#autenticación)).
- **Usuarios y roles:** `ADMIN` y `USER`, con permisos distintos por endpoint (ver `SecurityConfig`).
- **Proyectos:** creación, edición, borrado, invitación a miembros por código único, límite de 6 miembros por proyecto, estados (`IN_PROGRESS`, `COMPLETED`, `CANCELLED`).
- **Tareas:** CRUD completo dentro de un proyecto, asignación a un usuario, prioridad (`HIGH`/`MEDIUM`/`LOW`), estado (`PENDING`/`IN_PROGRESS`/`COMPLETED`), archivos adjuntos.
- **Imágenes:** avatar de usuario e imagen de proyecto, con validación de formato (JPEG/PNG/JPG/WEBP) y tamaño máximo (5MB).
- **Archivos:** entidad genérica de archivo (usada por tareas e imágenes) con metadata propia (nombre, tamaño, extensión, dueño) y almacenamiento desacoplado del backend concreto.
- **Notificaciones en tiempo real:** al asignar una tarea se dispara una notificación que se persiste y se empuja por WebSocket (STOMP) al usuario asignado; también hay endpoints REST para consultarlas/marcarlas como leídas.

## Arquitectura hexagonal

El código está organizado en tres capas con una dirección de dependencia estricta (siempre hacia adentro):

```
src/main/java/com/taskify/taskifyApi/
├── domain/            # Núcleo: modelos, enums, excepciones. Cero dependencias de Spring/JPA.
├── application/       # Casos de uso (services) + puertos (interfaces)
│   ├── ports/input/    -> contratos que exponen los casos de uso ("qué puede pedir el exterior")
│   ├── ports/output/   -> contratos que el caso de uso necesita del exterior ("qué necesita el negocio")
│   └── service/        -> implementación de los casos de uso, solo habla con puertos
└── infrastructure/    # Adaptadores concretos
    ├── input/          -> controllers REST, seguridad, mappers de entrada/salida HTTP
    └── output/         -> JPA (Postgres), MinIO, Firebase, disco local, email, websocket
```

La regla de oro: **`application/service` nunca importa nada de `infrastructure`**, solo interfaces (`ports`). Esto es lo que permite que, por ejemplo, `TaskService` funcione exactamente igual sin importar si las tareas se guardan en Postgres o si las imágenes viven en Firebase, MinIO o el disco local — el service solo conoce `TaskPersistencePort` y `FileStoragePort`, nunca la implementación.

Ventajas concretas de este diseño, no solo teóricas:

- **Testear el negocio sin base de datos ni red:** un test de `LocalStorageAdapter` (`src/test/java/.../LocalStorageAdapterTest.java`) corre en milisegundos, sin Spring, sin Postgres, sin mocks de infraestructura pesada.
- **Cambiar de proveedor sin tocar el dominio:** pasar de MinIO a almacenamiento local fue, literalmente, añadir una clase nueva que implementa `FileStoragePort` — cero cambios en `TaskService`, `ImageService`, controllers o DTOs.
- **Cero acoplamiento a Spring en el dominio:** `domain/model/*.java` son POJOs planos; se podrían usar en un contexto no-Spring sin cambiar una línea.

## Desacoplamiento en la práctica

### Almacenamiento de archivos — 3 implementaciones intercambiables

El puerto de salida `application/ports/output/FileStoragePort.java` define el contrato mínimo que necesita el negocio:

```java
public interface FileStoragePort {
    String uploadFile(InputStream fileStream, String fileName, String contentType) throws IOException;
    String getFileUrl(String storageKey) throws Exception;
    void deleteFile(String storageKey);
}
```

Hoy existen **tres adaptadores** que lo implementan, seleccionables con una sola variable de entorno (`STORAGE_TYPE`), sin recompilar ni tocar código de negocio:

| `STORAGE_TYPE` | Adaptador | Cuándo usarlo | Infra externa requerida |
|---|---|---|---|
| `local` | `LocalStorageAdapter` | Desarrollo/pruebas, proyectos de práctica | Ninguna — guarda en disco y sirve por `/uploads/**` |
| `minio` (default) | `MinioStorageAdapter` | Self-hosted, staging, producción propia | Un servidor MinIO (o cualquier S3-compatible) |
| `firebase` | `FirebaseStorageAdapter` | Integración con ecosistema Google/Firebase | Proyecto de Firebase + credenciales de servicio |

Ver la guía paso a paso de cada uno en [Configurar el almacenamiento de archivos](#configurar-el-almacenamiento-de-archivos).

### Persistencia — misma idea, un adaptador hoy, extensible mañana

`application/ports/output/*PersistencePort.java` (uno por entidad: `UserPersistencePort`, `TaskPersistencePort`, etc.) desacopla igual la persistencia. Hoy hay un único adaptador implementado (JPA + Hibernate contra **PostgreSQL**), pero gracias al mismo patrón:

- Puedes apuntar la app a **cualquier instancia de Postgres** (local, un contenedor Docker, un servidor remoto/managed) solo cambiando `URL_DB`/`USER_DB`/`PASSWORD_DB` — no hace falta tocar código.
- Si en el futuro se necesitara otro motor (MySQL, MongoDB, etc.), el trabajo se limita a escribir un nuevo adaptador que implemente los mismos `*PersistencePort` — el dominio y los services no se enterarían del cambio.

## Stack tecnológico

- **Lenguaje / runtime:** Java 21
- **Framework:** Spring Boot 3.4
- **Persistencia:** Spring Data JPA + Hibernate, PostgreSQL
- **Seguridad:** Spring Security 6, JWT (`java-jwt` de Auth0), OAuth2 Client (Google, GitHub)
- **Tiempo real:** WebSocket + STOMP (`spring-boot-starter-websocket`)
- **Almacenamiento de archivos:** MinIO client, Firebase Admin SDK, filesystem local
- **Mapeo objeto-objeto:** MapStruct
- **Utilidades:** Lombok
- **Build:** Maven (wrapper incluido, `./mvnw`)
- **Contenerización:** Docker / Docker Compose

## Cómo ejecutar el proyecto

### Requisitos previos

- JDK 21
- Maven 3.x (o usar `./mvnw`, incluido)
- Una instancia de PostgreSQL accesible (local, Docker o remota)
- Docker (opcional, solo si prefieres correr todo en contenedor)

### 1. Variables de entorno

La app se configura **enteramente por variables de entorno** (ver `src/main/resources/application.yml`). Crea un archivo `.env` en la raíz del proyecto (está en `.gitignore`, nunca se commitea) con este contenido de referencia:

```dotenv
SPRING_PROFILES_ACTIVE=dev

# Base de datos (ver "Configurar la base de datos")
URL_DB=jdbc:postgresql://localhost:5432/taskify_db
USER_DB=tu_usuario
PASSWORD_DB=tu_password

# JWT
JWT_KEY_SECRET=una-clave-secreta-larga-y-aleatoria
JWT_EXPIRATION=86400000
ISSUER_GENERATOR=TASKIFY_APP_BACKEND

# Frontend (usado para CORS, redirect de OAuth2 y WebSocket)
APP_WEB_URL=http://localhost:4200

# Cookie del JWT
COOKIE_SECURE=false

# Email (solo binding de config; ver "Limitaciones conocidas")
EMAIL_USERNAME=...
EMAIL_PASSWORD=...

# Almacenamiento de archivos — elige uno, ver sección dedicada
STORAGE_TYPE=local
```

> ⚠️ **Nada carga `.env` automáticamente** al correr `mvn spring-boot:run` desde terminal (esto no es un proyecto Node con dotenv incluido). Antes de arrancar, expórtalas a tu shell:
> ```bash
> set -a && source .env && set +a
> ```
> Si usas Docker Compose, esto no es necesario: `docker-compose.yml` ya usa `env_file: .env`.

### 2. Construir

```bash
./mvnw clean install
```

### 3. Ejecutar

```bash
set -a && source .env && set +a
./mvnw spring-boot:run
```

o, tras construir el jar:

```bash
java -jar target/taskifyApi-*.jar
```

La API queda disponible en `http://localhost:8080`.

### 4. Primer arranque: datos semilla

En una base de datos vacía, el perfil `dev` incluye un `DataSeeder` (`infrastructure/output/config/DataSeeder.java`) que se ejecuta una sola vez al arrancar (es idempotente: si ya hay datos, no hace nada) y crea:

- Los roles `ADMIN` y `USER`.
- Un usuario administrador de arranque: usuario `admin`, contraseña `Admin123!`.
- Una imagen de perfil y una imagen de proyecto "placeholder", para que crear usuarios/proyectos funcione desde el primer request sin pasos manuales.

Esto existe porque, sin al menos un rol `USER` y una imagen de tipo `USER` en la base, **el registro de usuarios falla siempre** (el negocio asume que existen). En `prod` (`ddl-auto: validate`) el seeder no corre — la siembra inicial en ese entorno debe hacerse a propósito.

## Configurar la base de datos

Cualquier instancia de PostgreSQL sirve, siempre que quede expresada en estas tres variables:

```dotenv
URL_DB=jdbc:postgresql://<host>:<puerto>/<nombre_db>
USER_DB=<usuario>
PASSWORD_DB=<password>
```

Ejemplos:

| Escenario | `URL_DB` |
|---|---|
| Postgres local (instalado en tu máquina) | `jdbc:postgresql://localhost:5432/taskify_db` |
| Postgres en Docker (`docker run -p 5432:5432 postgres:17`) | `jdbc:postgresql://localhost:5432/taskify_db` |
| Postgres remoto/managed | `jdbc:postgresql://mi-host.provider.com:5432/taskify_db` |

El comportamiento del esquema depende del perfil (`SPRING_PROFILES_ACTIVE`):

- `dev` → `ddl-auto: update` (Hibernate crea/ajusta tablas automáticamente, cómodo para desarrollo).
- `prod` → `ddl-auto: validate` (nunca modifica el esquema; asume que ya existe, vía migraciones gestionadas aparte).

## Configurar el almacenamiento de archivos

Elige uno con `STORAGE_TYPE` en tu `.env`. Solo necesitas las variables del backend que elijas.

### Opción recomendada para desarrollo: `local`

Sin infraestructura externa — guarda los archivos en disco y los sirve por HTTP.

```dotenv
STORAGE_TYPE=local
LOCAL_STORAGE_DIR=./storage-data
LOCAL_STORAGE_BASE_URL=http://localhost:8080/uploads
```

Listo. Al arrancar, la carpeta se crea sola y los archivos subidos quedan accesibles en `http://localhost:8080/uploads/<clave>`.

### `minio` (default, self-hosted / producción propia)

1. Levanta un servidor MinIO, por ejemplo con Docker:
   ```bash
   docker run -p 9000:9000 -p 9001:9001 \
     -e MINIO_ROOT_USER=admin -e MINIO_ROOT_PASSWORD=admin12345 \
     minio/minio server /data --console-address ":9001"
   ```
2. Configura:
   ```dotenv
   STORAGE_TYPE=minio
   MINIO_URL=http://localhost:9000
   MINIO_ACCESS_KEY=admin
   MINIO_SECRET_KEY=admin12345
   MINIO_BUCKET=taskify-storage
   MINIO_URL_EXPIRATION=3600
   ```
   El bucket se crea solo si no existe. Las URLs de descarga son *presigned* y expiran según `MINIO_URL_EXPIRATION` (segundos).

### `firebase` (integración con Google Cloud)

1. Crea un proyecto de Firebase y un bucket de Cloud Storage.
2. Genera una clave de cuenta de servicio (Firebase Console → Configuración del proyecto → Cuentas de servicio) y guárdala como `src/main/resources/firebase/clave_privada.json` (esa ruta está en `.gitignore`, nunca se commitea).
3. Configura:
   ```dotenv
   STORAGE_TYPE=firebase
   FIREBASE_STORAGE_BUCKET=tu-proyecto.firebasestorage.app
   ```

## Autenticación

- **Password:** `POST /api/auth/v1/signup` y `POST /api/auth/v1/login` con `{ "username", "password" }`. Devuelven un JWT. Funciona siempre, sin configuración adicional.
- **OAuth2 (opcional):** login social con Google y GitHub en `/oauth2/authorization/google` y `/oauth2/authorization/github` — crea el usuario automáticamente en el primer login si no existe. Está desactivado por defecto; para activarlo:
  1. Agrega `GOOGLE_AUTH_ID_CLIENT`, `GOOGLE_AUTH_SECRET_CLIENT`, `GITHUB_AUTH_ID_CLIENT`, `GITHUB_AUTH_SECRET_CLIENT` a tu `.env`.
  2. Suma el perfil `oauth2` a `SPRING_PROFILES_ACTIVE`, por ejemplo: `SPRING_PROFILES_ACTIVE=dev,oauth2`.

  Sin el perfil `oauth2` activo, esas rutas simplemente no existen (la app arranca igual y el login por password no se ve afectado). El registro de clientes vive en `src/main/resources/application-oauth2.yml`, separado a propósito del resto de la config para que nunca sea un requisito de arranque.
- **Cómo enviar el token:** depende del header `X-Client-Type`:
  - `X-Client-Type: WEB` (o ausente) → el JWT se setea como cookie `httpOnly` llamada `jwt`.
  - `X-Client-Type: DESKTOP` → el JWT se espera en `Authorization: Bearer <token>`.
- Los DTOs de entrada/salida usan **snake_case** (`spring.jackson.property-naming-strategy: SNAKE_CASE`): `first_name`, `due_date`, `image_id`, etc., no camelCase.

## Mapa de endpoints

Todas las rutas cuelgan de `/api`. Resumen por recurso (ver los `*RestAdapter` en `infrastructure/input/controller/rest` para el detalle completo de cada uno):

| Recurso | Base | Operaciones |
|---|---|---|
| Auth | `/api/auth/v1` | `signup`, `login`, `logout` |
| Usuarios | `/api/users/v1` | listar (admin), obtener por id, perfil de sesión, actualizar, borrar |
| Proyectos | `/api/projects/v1` | CRUD, buscar por código de invitación/creador/miembro, agregar miembro |
| Tareas | `/api/tasks/v1` | CRUD, buscar por proyecto/asignado/estado, adjuntar archivo |
| Imágenes | `/api/images/v1` | listar, obtener, subir (multipart, admin), borrar (admin) |
| Archivos | `/api/files/v1` | listar (admin), obtener, subir (multipart, admin), borrar |
| Notificaciones | `/api/notification/v1` | no leídas por usuario, marcar como leída, marcar todas como leídas |
| WebSocket | `/api/ws` (STOMP + SockJS) | push de notificaciones a `/user/{userId}/notification` |

## Docker

```bash
docker-compose up --build
```

Usa el `.env` de la raíz automáticamente (`env_file: .env`) y expone la API en `http://localhost:80` (mapea el puerto 80 del host al 8080 del contenedor).

> Si eliges `STORAGE_TYPE=local` dentro de Docker, los archivos se guardan **dentro del contenedor** y se pierden al recrearlo. Para persistirlos, monta un volumen: añade `volumes: ["./storage-data:/root/storage-data"]` al servicio en `docker-compose.yml`.

## Tests

```bash
./mvnw test
```

## Consideraciones y limitaciones conocidas

Transparencia sobre el estado actual del proyecto:

- **Email configurado pero no usado:** `EMAIL_USERNAME`/`EMAIL_PASSWORD` son obligatorias por el *binding* de `EmailProperties`, pero ningún caso de uso actual envía correos todavía (el bean `JavaMailSender` existe, no está conectado a ninguna funcionalidad).
- **Cobertura de tests limitada:** por ahora hay tests unitarios solo para el adaptador de storage local; el resto del negocio (services, controllers) no tiene tests automatizados aún.
- **Autorización basada en rol, no en pertenencia:** los endpoints validan rol (`ADMIN`/`USER`) pero no verifican si el usuario autenticado es dueño/miembro del recurso que está editando (proyecto, tarea). Tenerlo en cuenta si vas a exponer esto fuera de un entorno de confianza.

## Licencia

Este proyecto está bajo la Licencia MIT.
