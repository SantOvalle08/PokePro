# ⚡ PokeApp TallerAPI

Aplicación móvil desarrollada en Android con **Jetpack Compose** que consume la API pública **PokeAPI** (`https://pokeapi.co/`) cumpliendo con todos los requerimientos de consumo de servicios REST, filtrado avanzado, paginación infinita, indicador de conectividad y arquitectura en capas.

---

## 🚀 Características Principales
- **Consumo de PokeAPI vía Retrofit**: Integración con múltiples endpoints (`/pokemon`, `/pokemon/{name}`, `/type`, `/type/{name}`).
- **Interfaz de Usuario (Jetpack Compose)**:
  - Pantalla principal (Home) con diseño temático de Pokémon, botones rectangulares y diseño retro.
  - Pantalla de listado con paginación infinita (scroll infinito).
  - Barra de búsqueda de Pokémon por nombre y filtros por tipo mediante chips horizontales interactivos.
  - Pantalla de detalle con estadísticas base, tipos, peso, altura y habilidades.
- **Indicador de Conectividad en Tiempo Real**: Observador de red (`ConnectivityManager` con `NET_CAPABILITY_VALIDATED`) que muestra el estado Online/Offline y mecanismo de reintento automático/manual.

---

## 🛠️ Arquitectura
El proyecto sigue una arquitectura en capas limpia (**MVVM + Repositorio**):
- **UI**: Pantallas y componentes construidos con Jetpack Compose.
- **ViewModel**: Gestión de estado reactivo mediante `StateFlow` y manejo de lógica de negocio y filtrado no destructivo.
- **Repository**: Abstracción del acceso a datos de red (Retrofit).
- **Network**: Clientes y servicios Retrofit para PokeAPI.

---

## ⚙️ Instrucciones de Ejecución
1. Clona o descarga el repositorio en tu máquina.
2. Abre **Android Studio** (versión Hedgehog o superior recomendada).
3. Selecciona **File > Open** y abre la carpeta raíz del proyecto (`TallerAPI`).
4. Espera a que Gradle sincronice las dependencias automáticamente.
5. Conecta un dispositivo físico Android o inicia un ecumlador (API 28 o superior).
6. Haz clic en el botón **Run** (▶️) para compilar y ejecutar la aplicación.

---

## 👥 Autores
- Santiago Ovalle
- Juan Esteban Cuervo
