# Compose Components

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.10-purple.svg)](https://kotlinlang.org/)
[![Compose BOM](https://img.shields.io/badge/Compose%20BOM-2026.02.00-green.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Ready-blue.svg)](https://m3.material.io/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![CI](https://img.shields.io/badge/CI-Release%20Pipeline-blueviolet.svg)](.github/workflows/release.yml)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Una librería de componentes de UI altamente personalizables para **Jetpack Compose**, construida sobre **Material 3**. Ofrece opciones de personalización avanzadas (tamaños, colores, formas, **tint selectivo por capa**) que van más allá de las configuraciones estándar de Material 3.

---

## 📋 Tabla de Contenidos

- [✨ Características](#-características)
- [📦 Componentes Disponibles](#-componentes-disponibles)
  - [SliderComponent](#1-slidercomponent)
  - [LinearProgressIndicatorComponents](#2-linearprogressindicatorcomponents)
  - [RangeSliderComponent](#3-rangeslidercomponent)
  - [IconComponents (con `tintCap`)](#4-iconcomponents-con-tintcap)
  - [ImageComponents (con `tintCap`)](#5-imagecomponents-con-tintcap)
- [🎨 Sistema de Colores](#-sistema-de-colores)
- [🧪 Tests](#-tests)
- [📁 Estructura del Proyecto](#-estructura-del-proyecto)
- [🚀 Instalación](#-instalación)
- [📋 Requisitos](#-requisitos)
- [🤝 Contribuciones](#-contribuciones)
- [📄 Licencia](#-licencia)

---

## ✨ Características

- 🎨 **Personalización avanzada**: Control total sobre colores, tamaños y formas
- 🧩 **Basado en Material 3**: Integración nativa con el sistema de diseño de Material
- ⚡ **Fácil de usar**: API intuitiva y compatible con los componentes existentes
- 🖌️ **Tinte selectivo por capa (`tintCap`)**: Pinta solo las capas que quieras de un `ImageVector` y preserva el resto
- 🧪 **Cubierto por tests**: Suite de tests unitarios (JVM) e instrumentados (Compose UI tests)
- 📱 **Compatible con API 24+**: Soporte para una amplia gama de dispositivos
- 🚀 **Release automatizado**: Pipeline de CI que publica AAR + release + JitPack al mergear a `master`

---

## 📦 Componentes Disponibles

### 1. SliderComponent

Un deslizador altamente personalizable con control granular sobre su apariencia.

**Propiedades personalizables:**
| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `thumbSize` | `DpSize` | Tamaño del pulgar del slider |
| `trackHeight` | `Dp` | Altura de la pista |
| `tickSize` | `Dp` | Tamaño de las marcas de paso |
| `colors` | `SliderColorsDefaults` | Colores personalizados para cada estado |

**Ejemplo de uso:**

```kotlin
var sliderValue by remember { mutableFloatStateOf(0.5f) }

SliderComponent(
    value = sliderValue,
    onValueChange = { sliderValue = it },
    thumbSize = DpSize(24.dp, 24.dp),
    trackHeight = 12.dp,
    tickSize = 8.dp,
    colors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primaryContainer,
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceVariant
    ),
    steps = 4
)
```

---

### 2. LinearProgressIndicatorComponents

Una barra de progreso lineal con mayor control visual y soporte para rangos personalizados.

**Propiedades personalizables:**
| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `width` | `Dp` | Ancho del indicador |
| `height` | `Dp` | Alto del indicador |
| `range` | `ClosedFloatingPointRange<Float>` | Rango de valores (ej: `0f..100f`) |
| `strokeCap` | `StrokeCap` | Estilo de los extremos de la línea |
| `gapSize` | `Dp` | Espacio entre indicador y pista |
| `drawStopIndicator` | `DrawScope.() -> Unit` | Indicador de parada personalizado |

**Ejemplo de uso:**

```kotlin
LinearProgressIndicatorComponents(
    progress = { 0.7f },
    width = 280.dp,
    height = 12.dp,
    range = 0f..1f,
    color = MaterialTheme.colorScheme.primary,
    trackColor = MaterialTheme.colorScheme.surfaceVariant,
    strokeCap = StrokeCap.Round,
    gapSize = 4.dp
)
```

---

### 3. RangeSliderComponent

Un deslizador de rango para seleccionar intervalos de valores, con consistencia visual respecto a los demás componentes.

**Propiedades personalizables:**
| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `state` | `RangeSliderState` | Estado del slider de rango |
| `thumbSize` | `DpSize` | Tamaño de ambos pulgares |
| `trackHeight` | `Dp` | Altura de la pista |
| `tickSize` | `Dp` | Tamaño de las marcas |
| `startThumb` / `endThumb` | `@Composable` | Pulgares personalizados |
| `track` | `@Composable` | Pista personalizada |

**Ejemplo de uso:**

```kotlin
val rangeSliderState = remember {
    RangeSliderState(
        activeRangeStart = 0.2f,
        activeRangeEnd = 0.8f,
        steps = 3
    )
}

RangeSliderComponent(
    state = rangeSliderState,
    thumbSize = DpSize(20.dp, 20.dp),
    trackHeight = 10.dp,
    tickSize = 6.dp,
    colors = SliderDefaults.colors(
        thumbColor = MaterialTheme.colorScheme.primary,
        activeTrackColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.8f),
        inactiveTrackColor = MaterialTheme.colorScheme.surfaceContainerLow
    )
)
```

---

### 4. IconComponents (con `tintCap`)

Wrapper sobre `androidx.compose.material3.Icon` que añade el parámetro `tintCap` para controlar qué capas (layers) de un `ImageVector` reciben el color de `tint`. Las capas no afectadas conservan sus colores originales.

> 💡 **¿Por qué?** Cuando tiñes un `ImageVector` complejo (logos, ilustraciones, íconos con partes de marca) normalmente **todo** el vector se vuelve del color del `tint`. Con `tintCap` puedes pintar **solo** las capas que sí deben cambiar de color y dejar intactas las que representan la identidad visual (p.ej. el fondo o un detalle de marca).

**Propiedades personalizables:**
| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `imageVector` | `ImageVector` | Vector a renderizar |
| `contentDescription` | `String?` | Descripción para accesibilidad |
| `modifier` | `Modifier` | Modificador estándar |
| `tint` | `Color` | Color a aplicar (por defecto `LocalContentColor.current`) |
| `tintCap` | `TintCap` | Alcance del tint (ver tabla abajo, por defecto `TintCap.All`) |

**Variantes de `TintCap`:**
| Variante | Descripción |
|----------|-------------|
| `TintCap.All` | Pinta **todas** las capas con `tint` (default para `Icon`, equivale al comportamiento estándar de Compose) |
| `TintCap.Undefined` | **No aplica** ninguna transformación; el vector se renderiza con sus colores originales |
| `TintCap.index(n)` | Pinta **solo** la capa top-level en el índice `n` |
| `TintCap.range(rango)` | Pinta **todas** las capas cuyo índice esté dentro de `rango` (ej: `0..2`) |
| `TintCap.layers(1, 3)` | Pinta **solo** las capas top-level en los índices indicados |

> Una "capa" es cada nodo de primer nivel del `ImageVector` raíz (ya sea un `VectorGroup` o un `VectorPath` directo). Si la capa es un grupo, todo su contenido se pinta con el mismo criterio.

**Ejemplos de uso:**

```kotlin
// Default: pinta todas las capas
IconComponents(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null,
    tint = Color.Red
)

// Pinta solo la capa top-level en el índice 1
IconComponents(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null,
    tint = Color.Red,
    tintCap = TintCap.index(1)
)

// Pinta el rango 0..2 y respeta el resto
IconComponents(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null,
    tint = Color.Red,
    tintCap = TintCap.range(0..2)
)

// Pinta múltiples capas específicas
IconComponents(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null,
    tint = Color.Red,
    tintCap = TintCap.layers(1, 3)
)

// Respeta los colores originales del vector ignorando tint
IconComponents(
    imageVector = Icons.Filled.Favorite,
    contentDescription = null,
    tint = Color.Red, // se ignora por estar Undefined
    tintCap = TintCap.Undefined
)
```

#### Fixture incluido: `Icons.MapTruck`

El módulo incluye un `ImageVector` de camión multi-capa pensado para ejercitar `tintCap`:

```
Índice 0 → wheels (grupo con 2 neumáticos)   #424242
Índice 1 → body   (cama del camión)          #E53935
Índice 2 → cab    (cabina + ventana)         #1E88E5
Índice 3 → cargo  (caja de carga)            #43A047
```

Úsalo para prototipar y validar el comportamiento de `tintCap` sin necesidad de un asset externo:

```kotlin
IconComponents(
    imageVector = Icons.MapTruck,
    contentDescription = "Truck",
    tint = Color.Yellow,
    tintCap = TintCap.layers(0, 3)  // solo neumáticos y carga en amarillo
)
```

---

### 5. ImageComponents (con `tintCap`)

Wrapper sobre `androidx.compose.foundation.Image` con la misma potencia de `tintCap` que `IconComponents`. Pensado para vectores con varias capas donde queremos preservar colores originales (logos, ilustraciones, etc.).

**Propiedades personalizables:**
| Propiedad | Tipo | Descripción |
|-----------|------|-------------|
| `imageVector` | `ImageVector` | Vector a renderizar |
| `contentDescription` | `String?` | Descripción para accesibilidad |
| `modifier` | `Modifier` | Modificador estándar |
| `alignment` | `Alignment` | Alineación dentro del espacio disponible |
| `contentScale` | `ContentScale` | Estrategia de escalado (default `ContentScale.Fit`) |
| `alpha` | `Float` | Opacidad (default `DefaultAlpha`) |
| `colorFilter` | `ColorFilter?` | Filtro de color opcional adicional |
| `tint` | `Color?` | Color a aplicar (opcional) |
| `tintCap` | `TintCap` | Alcance del tint (default `TintCap.Undefined`) |

**Ejemplo de uso:**

```kotlin
// Logo con fondo original y un solo trazo tintado
ImageComponents(
    imageVector = myBrandLogo,
    contentDescription = "Logo",
    modifier = Modifier.size(120.dp),
    tint = MaterialTheme.colorScheme.primary,
    tintCap = TintCap.index(0)
)

// Todas las capas pintadas con tint
ImageComponents(
    imageVector = myBrandLogo,
    contentDescription = "Logo",
    modifier = Modifier.size(120.dp),
    tint = MaterialTheme.colorScheme.primary,
    tintCap = TintCap.All
)

// Colores originales del vector intactos (sin transformación)
ImageComponents(
    imageVector = myBrandLogo,
    contentDescription = "Logo",
    modifier = Modifier.size(120.dp),
    tintCap = TintCap.Undefined
)
```

---

## 🎨 Sistema de Colores

Todos los componentes utilizan `SliderColorsDefaults` para una gestión coherente de colores:

```kotlin
SliderColorsDefaults(
    thumbColor = Color,              // Color del pulgar
    activeTrackColor = Color,        // Color de la pista activa
    activeTickColor = Color,         // Color de las marcas activas
    inactiveTrackColor = Color,      // Color de la pista inactiva
    inactiveTickColor = Color,       // Color de las marcas inactivas
    disabledThumbColor = Color,      // Color del pulgar deshabilitado
    disabledActiveTrackColor = Color,
    disabledActiveTickColor = Color,
    disabledInactiveTrackColor = Color,
    disabledInactiveTickColor = Color
)
```

---

## 🧪 Tests

Cada componente está cubierto por tests. Para ejecutarlos:

```bash
# Tests unitarios (JVM) — rápidos, no requieren emulador
./gradlew :component:testDebugUnitTest

# Tests instrumentados (Compose UI tests) — requieren emulador o dispositivo
./gradlew :app:connectedDebugAndroidTest
```

**Cobertura:**

| Componente | Unit tests | Instrumented UI tests |
|------------|:----------:|:---------------------:|
| `SliderComponent` | — | — |
| `LinearProgressIndicatorComponents` | — | — |
| `RangeSliderComponent` | — | — |
| `TintCap` | ✅ 9 tests | ✅ vía `Icon` / `Image` |
| `ImageVectorTinter` | ✅ 7 tests | ✅ vía `Icon` / `Image` |
| `IconComponents` (con `tintCap`) | — | ✅ 6 tests |
| `ImageComponents` (con `tintCap`) | — | ✅ 5 tests |

Los UI tests renderizan el fixture `Icons.MapTruck` (4 capas top-level con colores distinguibles) y muestrean píxeles del bitmap capturado para verificar que cada variante de `tintCap` pinta exactamente las capas correctas.

---

## 📁 Estructura del Proyecto

```
composecomponents/
├── app/                                          # Aplicación de demostración
│   └── src/main/java/com/blipblipcode/compose_components/
│       └── MainActivity.kt                       # Incluye el fixture Icons.MapTruck
├── component/                                    # Módulo de la librería
│   └── src/main/java/com/blipblipcode/component/
│       ├── slider/                               # SliderComponent y utilidades
│       │   ├── SliderComponent.kt
│       │   ├── SliderDefaults.kt
│       │   ├── SliderColorsDefaults.kt
│       │   └── SliderSizeDefaults.kt
│       ├── linear/                               # LinearProgressIndicatorComponents
│       │   └── LinearProgressIndicatorComponents.kt
│       ├── range/                                # RangeSliderComponent
│       │   ├── RangeSliderComponent.kt
│       │   └── RangeSliderDefaults.kt
│       └── image/                                # IconComponents e ImageComponents con tintCap
│           ├── TintCap.kt                        # Sealed class (All / Undefined / Index / Range / Layers)
│           ├── ImageVectorTinter.kt              # Lógica interna de re-tintado selectivo
│           ├── Icon.kt                           # Wrapper de Material3 Icon → IconComponents
│           ├── Image.kt                          # Wrapper de Foundation Image → ImageComponents
│           └── MapTruck.kt                       # Fixture ImageVector de 4 capas
│   └── src/test/                                 # Tests unitarios (JVM)
│       └── java/com/blipblipcode/component/image/
│           ├── TintCapTest.kt                    # 9 tests
│           └── ImageVectorTinterTest.kt          # 7 tests
│   └── src/androidTest/                          # Tests instrumentados (Compose UI)
│       └── java/com/blipblipcode/component/image/
│           ├── IconTintCapTest.kt                # 6 tests
│           └── ImageTintCapTest.kt               # 5 tests
├── .github/workflows/
│   ├── workflows/
│   │   ├── ci.yml                                # CI: tests on every PR to master (open/synchronize)
│   │   └── release.yml                           # Release: build AAR + tag + GitHub Release + JitPack on PR close
│   ├── CODEOWNERS                                # Code owners del repo (para branch protection)
│   └── branch-protection/
│       └── master.json                           # Config de protección aplicada a master (reproducible vía gh api)
└── gradle/
    └── libs.versions.toml                        # Catálogo de versiones
```

---

## 🚀 Instalación

### Desde JitPack (release publicado)

Cada merge a `master` publica automáticamente un nuevo tag + AAR en GitHub Releases y dispara una build en JitPack.

Agrega el repositorio de JitPack en tu `settings.gradle.kts`:

```kotlin
dependencyResolutionManagement {
    repositories {
        maven { url = uri("https://jitpack.io") }
    }
}
```

Y luego la dependencia en el módulo de tu app:

```kotlin
dependencies {
    implementation("com.github.LeandroLCD:compose-components:<TAG>")
}
```

Reemplaza `<TAG>` por el tag publicado (ej: `v0.1.0`). Los tags y el changelog están en la pestaña [Releases](../../releases) del repositorio.

> ⚠️ La primera vez que importes el tag, JitPack necesita compilar el módulo; puede tardar unos minutos. Builds subsiguientes son instantáneas.

### Proyecto local

Incluye el módulo `:component` en tus dependencias de Gradle:

```kotlin
dependencies {
    implementation(project(":component"))
}
```

### Configuración del proyecto

Asegúrate de tener habilitado Compose en tu `build.gradle.kts`:

```kotlin
android {
    buildFeatures {
        compose = true
    }
}
```

---

## 📋 Requisitos

| Requisito | Versión mínima |
|-----------|----------------|
| Android Studio | Ladybug o superior |
| Kotlin | 2.3.10+ |
| Compose BOM | 2026.02.00+ |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |
| JVM Target | 17 |
| AGP | 9.0.0+ |

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si deseas contribuir:

1. Haz un Fork del proyecto
2. Crea una rama desde `master` para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -m 'feat: añade nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request hacia `master`

El pipeline de CI correrá tests unitarios + instrumentados (API 36) y, al mergear, publicará un nuevo release.

### 🔒 Protección de `master`

La rama `master` está protegida y solo recibe cambios vía Pull Request:

- ✅ Pull request obligatorio antes de mergear
- ✅ 1 aprobación de code review
- ✅ Revisión de **code owner** requerida (definido en [`.github/CODEOWNERS`](.github/CODEOWNERS))
- ✅ Reviews stale se descartan ante nuevos pushes
- ✅ Historial lineal (squash o rebase — no merge commits)
- ✅ Force-push y borrado de rama deshabilitados
- ✅ Conversaciones sin resolver bloquean el merge
- ✅ Reglas aplicadas incluso a administradores (`enforce_admins: true`)


---

## 📄 Licencia

Este proyecto está bajo la Licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más detalles.

---

## 📬 Contacto

Si tienes preguntas o sugerencias, no dudes en abrir un [Issue](../../issues) en el repositorio.

---

<p align="center">
  Hecho con ❤️ usando Jetpack Compose y Material 3
</p>
