# Compose Components

[![Kotlin](https://img.shields.io/badge/Kotlin-2.3.0-purple.svg)](https://kotlinlang.org/)
[![Compose BOM](https://img.shields.io/badge/Compose%20BOM-2025.12.01-green.svg)](https://developer.android.com/jetpack/compose)
[![Material 3](https://img.shields.io/badge/Material%203-Ready-blue.svg)](https://m3.material.io/)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)
[![License](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)

Una librería de componentes de UI altamente personalizables para **Jetpack Compose**, construida sobre **Material 3**. Ofrece opciones de personalización avanzadas (tamaños, colores, formas) que van más allá de las configuraciones estándar de Material 3.

---

## ✨ Características

- 🎨 **Personalización avanzada**: Control total sobre colores, tamaños y formas
- 🧩 **Basado en Material 3**: Integración nativa con el sistema de diseño de Material
- ⚡ **Fácil de usar**: API intuitiva y compatible con los componentes existentes
- 📱 **Compatible con API 24+**: Soporte para una amplia gama de dispositivos

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

## 📁 Estructura del Proyecto

```
composecomponents/
├── app/                          # Aplicación de demostración
├── component/                    # Módulo de la librería
│   └── src/main/java/com/blipblipcode/component/
│       ├── slider/               # SliderComponent y utilidades
│       │   ├── SliderComponent.kt
│       │   ├── SliderDefaults.kt
│       │   ├── SliderColorsDefaults.kt
│       │   └── SliderSizeDefaults.kt
│       ├── linear/               # LinearProgressIndicatorComponents
│       │   └── LinearProgressIndicatorComponents.kt
│       └── range/                # RangeSliderComponent
│           ├── RangeSliderComponent.kt
│           └── RangeSliderDefaults.kt
└── gradle/
    └── libs.versions.toml        # Catálogo de versiones
```

---

## 🚀 Instalación

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
| Kotlin | 2.3.0+ |
| Compose BOM | 2025.12.01+ |
| Min SDK | 24 (Android 7.0) |
| Target SDK | 36 |
| JVM Target | 17 |

---

## 🤝 Contribuciones

¡Las contribuciones son bienvenidas! Si deseas contribuir:

1. Haz un Fork del proyecto
2. Crea una rama para tu feature (`git checkout -b feature/nueva-funcionalidad`)
3. Realiza tus cambios y haz commit (`git commit -m 'Añade nueva funcionalidad'`)
4. Push a la rama (`git push origin feature/nueva-funcionalidad`)
5. Abre un Pull Request

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
