# Bitácora de compilación — Prueba y Verás

## 25/08/2026 — Inicio del proyecto (Parte 1: dominio y datos)

Scaffold creado siguiendo `01-PROMPT-MAESTRO.md` sección 7 y 7.1. Versiones
copiadas de `numeropolis/` y `fabrica-de-historias/` (ya verificadas
compilando en este mismo entorno, ver sus propios `BUILD_REPORT.md`):
Gradle 9.3.1, AGP 9.1.1, Kotlin 2.4.0, KSP 2.3.10, Compose BOM 2026.08.00,
Room 2.8.4, JDK 17, compileSdk/targetSdk 37, minSdk 24.

## 25/08/2026 — Parte 1 cerrada: dominio y datos, 100 % verificados

Sigue el plan `.superpowers/plans/2026-08-25-pruebayveras-dominio-y-datos.md`.
Esta parte no incluye ninguna pantalla todavía (`MainActivity` es un
marcador de posición) — cubre `domain/` y `data/` completos, tal como los
describe `fichas/30-CIENCIALAB.md`.

### `./gradlew clean testDebugUnitTest` — 84 tests, 0 fallos, 0 errores

```
BUILD SUCCESSFUL in 27s
30 actionable tasks: 30 executed
```

| Suite | Qué prueba | Tests |
|---|---|---|
| `MotorPruebaJustaTest` | dos montajes difieren en exactamente una variable | 8 |
| `MotorFlotabilidadTest` | densidad del agua salada vs. densidad del huevo (Isla de la Marea) | 6 |
| `MotorCaidaTest` | caída libre y factor de paracaídas (Isla del Viento) | 5 |
| `MotorGerminacionTest` | crecimiento según agua y luz (Isla del Jardín) | 6 |
| `MotorEcoTest` | retardo e intensidad del eco (Isla de la Cueva) | 5 |
| `MotorEstaticaTest` | papelitos atraídos según frotadas (Isla del Faro) | 5 |
| `MotorDisolucionTest` | tiempo de disolución según temperatura (Isla de las Olas) | 4 |
| `MotorFriccionTest` | distancia según superficie de rampa (Isla del Risco) | 6 |
| `MotorMagnetismoTest` | grosor de obstáculo vs. tipo de material (Isla del Imán) | 5 |
| `MotorAbsorcionLuzTest` | temperatura ganada según color (Isla del Reflejo) | 5 |
| `MotorCuadernoDatosTest` | tendencia real desde los datos guardados | 6 |
| `MotorProgresoTest` | desbloqueos, Chirimbolo completo, racha | 8 |
| `AppDatabaseTest` | Room en memoria con Robolectric | 3 |
| `SemillaContractTest` | cantidades semilla contra la ficha (9 islas, 27 retos, 9 piezas, 12 insignias, 30 frases) | 8 |
| `CienciaLabRepositoryTest` | sembrado idempotente, tendencia real, confirmar pieza | 4 |

### `./gradlew lintDebug` — 0 errores, 13 avisos

Todos los avisos son esperables en esta etapa: versiones de librería ancladas a
propósito (igual que `numeropolis` y `fabrica-de-historias`, para no arriesgar
una combinación no probada), `MissingApplicationIcon` porque el icono real
sale de Gemini en la Parte 2, y `allowBackup` deprecado (cosmético, no afecta
la regla de privacidad de la sección 9: la app no declara `INTERNET` ni ningún
otro permiso).

### Un error real encontrado y corregido durante la construcción

**Room rechaza una clase `Converters` vacía.** El plan original dejaba un
`Converters.kt` sin funciones "por si una migración futura lo necesita". KSP
lo rechazó de inmediato: *"Class is referenced as a converter but it does not
have any converter functions."* Ninguna de las 8 entidades usa un tipo que
Room no soporte de forma nativa, así que se eliminó el archivo y la anotación
`@TypeConverters` de `AppDatabase` en vez de rellenarlo con una función que no
se usa — el mismo criterio de "no simular una función que no existe" que pide
la sección 1 del prompt maestro, aplicado a una clase de infraestructura.

## Qué falta (Parte 2, plan aparte)

Tema y paleta, los componentes reutilizables no genéricos de la sección 3.1
(mesa de doble montaje, formas propias, navegación diegética), las 14
pantallas, la integración del arte generado en Gemini, y la verificación
completa con `assembleDebug` + `lintDebug` + pruebas de Compose con
Robolectric (sección 10.1). No se declara terminada la app hasta que la
Parte 2 también esté verde.
