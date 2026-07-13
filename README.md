# 🏢 ERP Lite

Sistema ERP ligero construido con **Spring Boot 4** y **arquitectura hexagonal**, orientado a la gestión de productos y pedidos. Diseñado como proyecto multi-módulo de Gradle para reforzar la separación de responsabilidades a nivel de compilación.

---

## 🧱 Arquitectura

El proyecto sigue el patrón de **Puertos y Adaptadores (Hexagonal)** distribuido en módulos independientes:

```
erp-common ──► erp-domain ──► erp-application ──► erp-infrastructure ──► erp-api
```

| Módulo | Responsabilidad |
|---|---|
| `erp-common` | Utilidades compartidas, excepciones y tipos comunes |
| `erp-domain` | Agregados, entidades de dominio, value objects e interfaces de puerto |
| `erp-application` | Casos de uso y servicios de aplicación |
| `erp-infrastructure` | Adaptadores de salida: JPA (PostgreSQL), MongoDB, Redis |
| `erp-api` ⭐ | **Punto de entrada de la aplicación** |

> ⭐ **`erp-api`** es el único módulo con `@SpringBootApplication` y el archivo `application.yml`. Concentra toda la configuración de arranque de Spring Boot y los adaptadores de entrada (controllers REST). Los demás módulos compilan como JARs de librería — ninguno puede iniciarse por sí solo.

---

## 🗄️ Persistencia Políglota

Tres bases de datos con responsabilidades bien delimitadas:

| Base de datos | Uso | Datos |
|---|---|---|
| 🐘 **PostgreSQL 17** | Datos transaccionales (escritura) | Productos, Pedidos |
| 🍃 **MongoDB 8** | Documentos y modelo de lectura | Catálogos, Logs de auditoría |
| ⚡ **Redis 7** | Caché de catálogos | — |

---

## 🛠️ Stack Tecnológico

- **Java 25**
- **Spring Boot 4.1.0**
- **Gradle** (multi-módulo)
- **Lombok** — reducción de boilerplate
- **MapStruct** — mapeo entre entidades y objetos de dominio
- **Docker Compose** — infraestructura local

---

## 🚀 Levantando el entorno local

### 1. Iniciar la infraestructura

```bash
docker compose up -d
```

Esto levanta PostgreSQL, MongoDB, Redis y Redis Commander. Los scripts de inicialización se ejecutan automáticamente en el primer arranque.

### 2. Compilar el proyecto

```bash
./gradlew build
```

### 3. Ejecutar la aplicación

```bash
./gradlew :erp-api:bootRun
```

La aplicación estará disponible en `http://localhost:9090`.

---

## 🐳 Servicios del Docker Compose

| Servicio | Puerto | Credenciales |
|---|---|---|
| PostgreSQL | `5432` | `postgresql / secret` |
| MongoDB | `27017` | `mongo / secret` |
| Redis | `6379` | `secret` |
| Redis Commander UI | `8081` | `mzrt / secret` |

---

## 🧪 Ejecutar Tests

```bash
# Todos los módulos
./gradlew test

# Módulo específico
./gradlew :erp-domain:test

# Clase específica
./gradlew :erp-application:test --tests "com.mzrt.erp_lite.SomeServiceTest"
```
